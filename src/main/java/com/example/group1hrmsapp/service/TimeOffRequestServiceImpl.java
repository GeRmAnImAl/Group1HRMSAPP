package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.*;
import com.example.group1hrmsapp.repository.EmployeeRepository;
import com.example.group1hrmsapp.repository.TimeOffRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import javax.persistence.criteria.Predicate;
import java.util.stream.Collectors;

@Service
public class TimeOffRequestServiceImpl implements TimeOffRequestService{
    @Autowired
    private TimeOffRequestRepository timeOffRequestRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<TimeOffRequest> getAllTimeOffRequests() {
        return timeOffRequestRepository.findAll();
    }

    public void createTimeOffRequest(TimeOffRequest timeOffRequest) {
        Employee employee = employeeRepository.findById(timeOffRequest.getEmployee().getId()).orElseThrow(() -> new NoSuchElementException("Employee not found"));

        Manager approver = employee.getManager();
        List<Manager> approvers = new ArrayList<>();
        approvers.add(approver);

        timeOffRequest.setEmployee(employee);
        timeOffRequest.setApprovers(approvers);
        timeOffRequest.setTimeOffStatus(TimeOffStatus.PENDING);

        // Save the time off request
        timeOffRequestRepository.save(timeOffRequest);
    }

    public TimeOffRequest cancelTimeOffRequest(Long requestId) {
        TimeOffRequest request = timeOffRequestRepository.findById(requestId).orElseThrow(() -> new NoSuchElementException("TimeOffRequest not found"));

        request.setTimeOffStatus(TimeOffStatus.CANCELLED);

        return timeOffRequestRepository.save(request);
    }

    public TimeOffRequest approveTimeOffRequest(Long requestId, Long managerId) {
        TimeOffRequest request = timeOffRequestRepository.findById(requestId).orElseThrow(() -> new NoSuchElementException("TimeOffRequest not found"));

        Employee manager = employeeRepository.findById(managerId).orElseThrow(() -> new NoSuchElementException("Manager not found"));
        if (!"Manager".equals(manager.getSpecialType())) {
            throw new RuntimeException("This employee is not a manager");
        }

        // Check if this manager is in the list of approvers
        if (request.getApprovers().contains(manager)) {
            request.setTimeOffStatus(TimeOffStatus.APPROVED);
        } else {
            throw new RuntimeException("This manager is not authorized to approve this request");
        }

        return timeOffRequestRepository.save(request);
    }

    public TimeOffRequest rejectTimeOffRequest(Long requestId, Long managerId) {
        TimeOffRequest request = timeOffRequestRepository.findById(requestId).orElseThrow(() -> new NoSuchElementException ("TimeOffRequest not found"));

        Employee manager = employeeRepository.findById(managerId).orElseThrow(() -> new NoSuchElementException ("Manager not found"));
        if (!"Manager".equals(manager.getSpecialType())) {
            throw new RuntimeException("This employee is not a manager");
        }

        // Check if this manager is in the list of approvers
        if (request.getApprovers().contains(manager)) {
            request.setTimeOffStatus(TimeOffStatus.REJECTED);
        } else {
            throw new RuntimeException("This manager is not authorized to reject this request");
        }

        return timeOffRequestRepository.save(request);
    }

    @Override
    public Page<TimeOffRequest> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return timeOffRequestRepository.findAll(pageable);
    }

    @Override
    public Page<TimeOffRequest> findFilteredAndPaginated(Specification<TimeOffRequest> spec, Pageable pageable) {
        return timeOffRequestRepository.findAll(spec, pageable);
    }

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
