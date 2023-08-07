package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.*;
import com.example.group1hrmsapp.repository.EmployeeRepository;
import com.example.group1hrmsapp.repository.PaymentRepository;
import com.example.group1hrmsapp.repository.UserRepository;
import com.example.group1hrmsapp.repository.WorkedHoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.*;

/**
 * Service implementation for managing WorkedHours entities in the HRMS application.
 * This class provides operations related to creating, fetching, updating, deleting, and paginating
 * the worked hours of employees.
 */
@Service
public class WorkedHoursServiceImpl implements WorkedHoursService{
    @Autowired
    private WorkedHoursRepository workedHoursRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentService paymentService;

    /**
     * Retrieves all WorkedHours entities from the repository.
     * @return List of all WorkedHours entries stored in the repository.
     */
    @Override
    public List<WorkedHours> getAllWorkedHours() {
        return workedHoursRepository.findAll();
    }

    /**
     * Persists the provided WorkedHours entity into the repository after setting the relevant details from the logged-in user.
     * @param workedHours The WorkedHours object to be saved.
     */
    @Override
    public void createWorkedHours(WorkedHours workedHours) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        AppUser loggedInUser = userRepository.findByUserName(loggedInUsername)
                .orElseThrow(()-> new RuntimeException("No user logged in"));
        Employee loggedInEmployee = loggedInUser.getEmployee();
        workedHours.setEmployee(loggedInEmployee);
        workedHours.setApprovalStatus(ApprovalStatus.PENDING);

        workedHoursRepository.save(workedHours);
    }

    /**
     * Cancels a specific WorkedHours entity based on the provided workedHoursId.
     * @param workedHoursId The ID of the WorkedHours entity to be canceled.
     * @return Updated WorkedHours entity with ApprovalStatus set to CANCELLED.
     */
    @Override
    public WorkedHours cancelWorkedHours(Long workedHoursId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        AppUser loggedInUser = userRepository.findByUserName(loggedInUsername)
                .orElseThrow(()-> new RuntimeException("No user logged in"));
        Employee loggedInEmployee = loggedInUser.getEmployee();
        WorkedHours workedHours = workedHoursRepository.findById(workedHoursId)
                .orElseThrow(()-> new NoSuchElementException("WorkedHours not found"));

        if (loggedInEmployee.getId() != workedHours.getEmployee().getId()){
            throw new RuntimeException("You are not allowed to cancel these hours");
        }

        workedHours.setApprovalStatus(ApprovalStatus.CANCELLED);

        return workedHoursRepository.save(workedHours);
    }

    /**
     * Deletes a specific WorkedHours entity based on the provided ID.
     * @param id ID of the WorkedHours entity to be deleted.
     */
    @Override
    public void deleteWorkedHoursById(Long id) {
        Optional<WorkedHours> optionalWorkedHours = workedHoursRepository.findById(id);
        if(optionalWorkedHours.isPresent()){
            WorkedHours workedHoursToDelete = optionalWorkedHours.get();
            workedHoursRepository.delete(workedHoursToDelete);
        }
        else{
            throw new RuntimeException("WorkedHours not found for id :: " + id);
        }
    }

    /**
     * Approves a specific WorkedHours entity based on the provided workedHoursId.
     * @param workedHoursId The ID of the WorkedHours entity to be approved.
     * @param managerId The ID of the manager who approves the hours.
     * @return Updated WorkedHours entity with ApprovalStatus set to APPROVED.
     */
    @Override
    public WorkedHours approveWorkedHours(Long workedHoursId, Long managerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        AppUser loggedInUser = userRepository.findByUserName(loggedInUsername)
                .orElseThrow(()-> new RuntimeException("No user logged in"));
        Employee loggedInEmployee = loggedInUser.getEmployee();
        WorkedHours workedHours = workedHoursRepository.findById(workedHoursId)
                .orElseThrow(()-> new NoSuchElementException("WorkedHours not found"));
        Employee hoursEmployee = employeeRepository.findById(workedHours.getEmployee().getId())
                .orElseThrow(()-> new RuntimeException("The employee associated with these hours does not exist."));
        Employee hoursEmployeeManager = hoursEmployee.getManager();

        if(hoursEmployeeManager == null || !hoursEmployeeManager.getId().equals(loggedInEmployee.getId())){
            throw new RuntimeException("You are not authorized to approve these hours");
        }
        if(loggedInEmployee.getSpecialType() != SpecialType.MANAGER){
            throw new RuntimeException("Logged in user is not a manager");
        }

        workedHours.setApprovalStatus(ApprovalStatus.APPROVED);

        List<Payment> paymentList = paymentRepository.findAll();
        boolean paymentFound = false;

        if (!paymentList.isEmpty()) {
            for (Payment payment : paymentList) {
                if (Objects.equals(workedHours.getEmployee().getId(), payment.getEmployee().getId())) {
                    payment.getWorkedHoursList().add(workedHours);
                    payment.setPaymentAmount(paymentService.calculateCost(payment, workedHours));
                    paymentRepository.save(payment);
                    paymentFound = true;
                    workedHours.setPayment(payment);
                    break;
                }
            }
        }

        if(paymentList.isEmpty() || !paymentFound) {
            Payment newPayment = new Payment();
            newPayment.setEmployee(workedHours.getEmployee());
            newPayment.getWorkedHoursList().add(workedHours);
            workedHours.setPayment(newPayment);



            newPayment.setPaymentAmount(paymentService.calculateCost(newPayment, workedHours));
            newPayment.setPaymentStatus(PaymentStatus.PENDING);
            paymentRepository.save(newPayment);
        }

        return workedHoursRepository.save(workedHours);
    }

    /**
     * Rejects a specific WorkedHours entity based on the provided workedHoursId.
     * @param workedHoursId The ID of the WorkedHours entity to be rejected.
     * @param managerId The ID of the manager who rejects the hours.
     * @return Updated WorkedHours entity with ApprovalStatus set to REJECTED.
     */
    @Override
    public WorkedHours rejectWorkedHours(Long workedHoursId, Long managerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        AppUser loggedInUser = userRepository.findByUserName(loggedInUsername)
                .orElseThrow(()-> new RuntimeException("No user logged in"));
        Employee loggedInEmployee = loggedInUser.getEmployee();
        WorkedHours workedHours = workedHoursRepository.findById(workedHoursId)
                .orElseThrow(()-> new NoSuchElementException("WorkedHours not found"));
        Employee hoursEmployee = employeeRepository.findById(workedHours.getEmployee().getId())
                .orElseThrow(()-> new RuntimeException("The employee associated with these hours does not exist."));
        Employee hoursEmployeeManager = hoursEmployee.getManager();

        if(hoursEmployeeManager == null || !hoursEmployeeManager.getId().equals(loggedInEmployee.getId())){
            throw new RuntimeException("You are not authorized to approve these hours");
        }
        if(loggedInEmployee.getSpecialType() != SpecialType.MANAGER){
            throw new RuntimeException("Logged in user is not a manager");
        }

        workedHours.setApprovalStatus(ApprovalStatus.REJECTED);

        return workedHoursRepository.save(workedHours);
    }

    /**
     * Retrieves a paginated list of WorkedHours entities.
     * @param pageNo Current page number.
     * @param pageSize Size of the page.
     * @param sortField Field to be used for sorting.
     * @param sortDirection Direction of the sort (ASC/DESC).
     * @return A page of WorkedHours entities based on the provided criteria.
     */
    @Override
    public Page<WorkedHours> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return workedHoursRepository.findAll(pageable);
    }

    /**
     * Retrieves a paginated and filtered list of WorkedHours entities.
     * @param spec Specification object containing the filtering criteria.
     * @param pageable Pageable object containing pagination details.
     * @return A page of filtered WorkedHours entities based on the provided criteria.
     */
    @Override
    public Page<WorkedHours> findFilteredAndPaginated(Specification<WorkedHours> spec, Pageable pageable) {
        return workedHoursRepository.findAll(spec, pageable);
    }

    /**
     * Prepares a Specification for filtering WorkedHours entities.
     * @param employee The ID of the employee the hours are associated with.
     * @param startDate The start date for the worked hours.
     * @param endDate The end date for the worked hours.
     * @param approvalStatus The status of the worked hours.
     * @return Specification object for filtering WorkedHours entities.
     */
    @Override
    public Specification<WorkedHours> prepareSpecification(Employee employee, LocalDate startDate, LocalDate endDate, String approvalStatus) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(employee != null){ // Add a check for empty employee
                predicates.add(criteriaBuilder.equal(root.get("employee"), employee));
            }

            if (startDate != null) { // Add a check for null startDate
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), startDate));
            }

            if (endDate != null) { // Add a check for null endDate
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), endDate));
            }

            if (approvalStatus != null && !approvalStatus.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("approvalStatus"), ApprovalStatus.valueOf(approvalStatus.toUpperCase())));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
