package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.*;
import com.example.group1hrmsapp.repository.EmployeeRepository;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class WorkedHoursServiceImpl implements WorkedHoursService{
    @Autowired
    private WorkedHoursRepository workedHoursRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public List<WorkedHours> getAllWorkedHours() {
        return workedHoursRepository.findAll();
    }

    @Override
    public void createWorkedHours(WorkedHours workedHours) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        AppUser loggedInUser = userRepository.findById(loggedInUsername)
                .orElseThrow(()-> new RuntimeException("No user logged in"));
        Employee loggedInEmployee = loggedInUser.getEmployee();
        workedHours.setEmployee(loggedInEmployee);
        workedHours.setApprovalStatus(ApprovalStatus.PENDING);

        workedHoursRepository.save(workedHours);
    }

    @Override
    public WorkedHours cancelWorkedHours(Long workedHoursId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        AppUser loggedInUser = userRepository.findById(loggedInUsername)
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

    @Override
    public WorkedHours approveWorkedHours(Long workedHoursId, Long managerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        AppUser loggedInUser = userRepository.findById(loggedInUsername)
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

        return workedHoursRepository.save(workedHours);
    }

    @Override
    public WorkedHours rejectWorkedHours(Long workedHoursId, Long managerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        AppUser loggedInUser = userRepository.findById(loggedInUsername)
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

    @Override
    public Page<WorkedHours> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return workedHoursRepository.findAll(pageable);
    }

    @Override
    public Page<WorkedHours> findFilteredAndPaginated(Specification<WorkedHours> spec, Pageable pageable) {
        return workedHoursRepository.findAll(spec, pageable);
    }

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
