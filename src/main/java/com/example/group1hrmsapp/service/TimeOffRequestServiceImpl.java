package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.*;
import com.example.group1hrmsapp.repository.EmployeeRepository;
import com.example.group1hrmsapp.repository.TimeOffRequestRepository;
import com.example.group1hrmsapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service class for managing TimeOffRequests.
 */
@Service
public class TimeOffRequestServiceImpl implements TimeOffRequestService{
    private static final Logger logger = LoggerFactory.getLogger(TimeOffRequestServiceImpl.class);

    @Autowired
    private TimeOffRequestRepository timeOffRequestRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieve all TimeOffRequests
     * @return List of TimeOffRequests
     */
    @Override
    public List<TimeOffRequest> getAllTimeOffRequests() {
        return timeOffRequestRepository.findAll();
    }

    /**
     * Create a new TimeOffRequest
     * @param timeOffRequest to be created
     */
    @Transactional
    public void createTimeOffRequest(TimeOffRequest timeOffRequest) {
        Employee employee = employeeRepository.findById(timeOffRequest.getEmployee().getId())
                .orElseThrow(() -> new NoSuchElementException("Employee not found"));

        Employee approver = employeeRepository.findById(employee.getManager().getId())
                .orElseThrow(() -> new NoSuchElementException("Approver not found"));

        String approvers = String.valueOf(approver.getId());

        timeOffRequest.setEmployee(employee);
        timeOffRequest.setApprover(approvers);
        timeOffRequest.setTimeOffStatus(TimeOffStatus.PENDING);
        timeOffRequest.registerObserver(approver);

        timeOffRequestRepository.save(timeOffRequest);
    }

    /**
     * Cancel a TimeOffRequest
     * @param requestId of the request to be cancelled
     * @return Cancelled TimeOffRequest
     */
    @Transactional
    public TimeOffRequest cancelTimeOffRequest(Long requestId) {
        TimeOffRequest request = timeOffRequestRepository.findById(requestId).orElseThrow(() -> new NoSuchElementException("TimeOffRequest not found"));

        request.setTimeOffStatus(TimeOffStatus.CANCELLED);

        return timeOffRequestRepository.save(request);
    }

    /**
     * Approve a TimeOffRequest
     * @param requestId of the request to be approved
     * @param userId of the manager approving the request
     * @return Approved TimeOffRequest
     */
    @Transactional
    public TimeOffRequest approveTimeOffRequest(Long requestId, Long userId) {
        // Fetch the currently logged-in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();  // assuming the principal's name is the username

        // Fetch the AppUser entity associated with the username
        AppUser loggedInUser = userRepository.findById(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("No user logged in"));

        // Fetch the Employee entity associated with the AppUser
        Employee loggedInEmployee = loggedInUser.getEmployee();

        TimeOffRequest request = timeOffRequestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("TimeOffRequest not found"));

        Employee requestEmployee = request.getEmployee();

        if (requestEmployee == null) {
            throw new RuntimeException("No employee associated with the time off request");
        }

        Employee requestEmployeeManager = requestEmployee.getManager();

        if (requestEmployeeManager == null || !requestEmployeeManager.getId().equals(loggedInEmployee.getId())) {
            throw new RuntimeException("You are not allowed to approve this request");
        }

        if (loggedInEmployee.getSpecialType() != SpecialType.MANAGER) {
            throw new RuntimeException("This employee is not a manager");
        }

        // Check if this manager is the approver
        if (!request.getApprover().equals(String.valueOf(loggedInEmployee.getId()))) {
            throw new RuntimeException("This manager is not authorized to approve this request");
        }

        request.setTimeOffStatus(TimeOffStatus.APPROVED);

        return timeOffRequestRepository.save(request);
    }



    /**
     * Reject a TimeOffRequest
     * @param requestId of the request to be rejected
     * @param userId of the manager rejecting the request
     * @return Rejected TimeOffRequest
     */
    @Transactional
    public TimeOffRequest rejectTimeOffRequest(Long requestId, Long userId) {
        // Fetch the currently logged-in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();  // assuming the principal's name is the username

        // Fetch the AppUser entity associated with the username
        AppUser loggedInUser = userRepository.findById(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("No user logged in"));

        // Fetch the Employee entity associated with the AppUser
        Employee loggedInEmployee = loggedInUser.getEmployee();

        TimeOffRequest request = timeOffRequestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("TimeOffRequest not found"));

        Employee requestEmployee = request.getEmployee();

        if (requestEmployee == null) {
            throw new RuntimeException("No employee associated with the time off request");
        }

        Employee requestEmployeeManager = requestEmployee.getManager();

        if (requestEmployeeManager == null || !requestEmployeeManager.getId().equals(loggedInEmployee.getId())) {
            throw new RuntimeException("You are not allowed to approve this request");
        }

        if (loggedInEmployee.getSpecialType() != SpecialType.MANAGER) {
            throw new RuntimeException("This employee is not a manager");
        }

        // Check if this manager is the approver
        if (!request.getApprover().equals(String.valueOf(loggedInEmployee.getId()))) {
            throw new RuntimeException("This manager is not authorized to approve this request");
        }

        request.setTimeOffStatus(TimeOffStatus.REJECTED);

        return timeOffRequestRepository.save(request);
    }

    /**
     * Get paginated TimeOffRequests
     * @param pageNo current page number
     * @param pageSize number of records per page
     * @param sortField field to be sorted by
     * @param sortDirection direction of sort
     * @return Paginated TimeOffRequests
     */
    @Override
    public Page<TimeOffRequest> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return timeOffRequestRepository.findAll(pageable);
    }

    /**
     * Find TimeOffRequests with provided filters and paginate them
     * @param spec specification for the filtering of requests
     * @param pageable pagination details
     * @return Paginated and filtered TimeOffRequests
     */
    @Override
    public Page<TimeOffRequest> findFilteredAndPaginated(Specification<TimeOffRequest> spec, Pageable pageable) {
        return timeOffRequestRepository.findAll(spec, pageable);
    }

    /**
     * Prepare a Specification for TimeOffRequests using provided filters
     * @param startDate beginning of the date range
     * @param endDate end of the date range
     * @param timeOffType type of time off
     * @param timeOffStatus status of time off
     * @return Prepared Specification
     */
    public Specification<TimeOffRequest> prepareSpecification(String startDate, String endDate, String timeOffType, String timeOffStatus) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (startDate != null && !startDate.isEmpty()) { // Add a check for empty startDate
                LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), start));
            }

            if (endDate != null && !endDate.isEmpty()) { // Add a check for empty endDate
                LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), end));
            }

            if (timeOffType != null && !timeOffType.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("timeOffType"), TimeOffType.valueOf(timeOffType.toUpperCase())));
            } else {
                predicates.add(criteriaBuilder.isNotNull(root.get("timeOffType"))); // Filter for non-null timeOffType
            }

            if (timeOffStatus != null && !timeOffStatus.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("timeOffStatus"), TimeOffStatus.valueOf(timeOffStatus.toUpperCase())));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
