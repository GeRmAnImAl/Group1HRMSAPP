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

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service class for managing TimeOffRequests.
 */
@Service
public class TimeOffRequestServiceImpl implements TimeOffRequestService{
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
    public void createTimeOffRequest(TimeOffRequest timeOffRequest) {
        Employee employee = employeeRepository.findById(timeOffRequest.getEmployee().getId())
                .orElseThrow(() -> new NoSuchElementException("Employee not found"));

        Employee approver = employeeRepository.findById(employee.getManager())
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
    public TimeOffRequest cancelTimeOffRequest(Long requestId) {
        TimeOffRequest request = timeOffRequestRepository.findById(requestId).orElseThrow(() -> new NoSuchElementException("TimeOffRequest not found"));

        request.setTimeOffStatus(TimeOffStatus.CANCELLED);

        return timeOffRequestRepository.save(request);
    }

    /**
     * Approve a TimeOffRequest
     * @param requestId of the request to be approved
     * @param managerId of the manager approving the request
     * @return Approved TimeOffRequest
     */
    public TimeOffRequest approveTimeOffRequest(Long requestId, Long managerId) {
        // Fetch the currently logged in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();  // assuming the principal's name is the username

        // Fetch the AppUser entity associated with the username
        AppUser loggedInUser = userRepository.findById(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("No user logged in"));

        // Fetch the Employee entity associated with the AppUser
        Employee loggedInEmployee = employeeRepository.findByAppUser(loggedInUser)
                .orElseThrow(() -> new RuntimeException("Logged in user has no associated employee"));

        // Check if the logged in employee is the same as the manager
        if (!loggedInEmployee.getId().equals(managerId)) {
            throw new RuntimeException("Logged in user is not the manager for this request");
        }

        TimeOffRequest request = timeOffRequestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("TimeOffRequest not found"));

        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new NoSuchElementException("Manager not found"));

        if (!"Manager".equals(manager.getSpecialType())) {
            throw new RuntimeException("This employee is not a manager");
        }

        // Check if this manager is the approver
        if (String.valueOf(manager.getId()).equals(request.getApprover())) {
            request.setTimeOffStatus(TimeOffStatus.APPROVED);
        } else {
            throw new RuntimeException("This manager is not authorized to approve this request");
        }

        return timeOffRequestRepository.save(request);
    }


    /**
     * Reject a TimeOffRequest
     * @param requestId of the request to be rejected
     * @param managerId of the manager rejecting the request
     * @return Rejected TimeOffRequest
     */
    public TimeOffRequest rejectTimeOffRequest(Long requestId, Long managerId) {
        TimeOffRequest request = timeOffRequestRepository.findById(requestId).orElseThrow(() -> new NoSuchElementException ("TimeOffRequest not found"));

        Employee manager = employeeRepository.findById(managerId).orElseThrow(() -> new NoSuchElementException ("Manager not found"));
        if (!"Manager".equals(manager.getSpecialType())) {
            throw new RuntimeException("This employee is not a manager");
        }

        // Check if this manager is in the list of approvers
        if (request.getApprover().contains(String.valueOf(manager.getId()))) {
            request.setTimeOffStatus(TimeOffStatus.REJECTED);
        } else {
            throw new RuntimeException("This manager is not authorized to reject this request");
        }

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

            if (startDate != null) {
                LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), start));
            }

            if (endDate != null) {
                LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), end));
            }

            if (timeOffType != null) {
                predicates.add(criteriaBuilder.equal(root.get("timeOffType"), TimeOffType.valueOf(timeOffType)));
            }

            if (timeOffStatus != null) {
                predicates.add(criteriaBuilder.equal(root.get("timeOffStatus"), TimeOffStatus.valueOf(timeOffStatus)));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
