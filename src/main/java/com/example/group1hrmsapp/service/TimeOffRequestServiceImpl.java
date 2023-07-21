package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.Employee;
import com.example.group1hrmsapp.model.Manager;
import com.example.group1hrmsapp.model.TimeOffRequest;
import com.example.group1hrmsapp.repository.EmployeeRepository;
import com.example.group1hrmsapp.repository.TimeOffRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TimeOffRequestServiceImpl implements TimeOffRequestService{
    @Autowired
    private TimeOffRequestRepository timeOffRequestRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public TimeOffRequest createTimeOffRequest(Long employeeId, List<Long> approverIds, TimeOffRequest request) {
        // Get the employee
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        // Get the approvers
        List<Manager> approvers = managerRepository.findAllById(approverIds);

        // Set the employee and approvers to the request
        request.setEmployee(employee);
        request.setApprovers(approvers);
        request.setTimeOffStatus(TimeOffStatus.PENDING); // Set status to Pending when request is created

        // Save the time off request
        return timeOffRequestRepository.save(request);
    }

    public TimeOffRequest cancelTimeOffRequest(Long requestId) {
        // Get the time off request
        TimeOffRequest request = timeOffRequestRepository.findById(requestId).orElseThrow(() -> new ResourceNotFoundException("TimeOffRequest not found"));

        // Cancel the request
        request.setTimeOffStatus(TimeOffStatus.CANCELLED);

        // Save the changes
        return timeOffRequestRepository.save(request);
    }

    public TimeOffRequest approveTimeOffRequest(Long requestId, Long managerId) {
        // Get the time off request
        TimeOffRequest request = timeOffRequestRepository.findById(requestId).orElseThrow(() -> new ResourceNotFoundException("TimeOffRequest not found"));

        // Get the manager
        Manager manager = managerRepository.findById(managerId).orElseThrow(() -> new ResourceNotFoundException("Manager not found"));

        // Check if this manager is in the list of approvers
        if (request.getApprovers().contains(manager)) {
            // Approve the request
            request.setTimeOffStatus(TimeOffStatus.APPROVED);
        } else {
            throw new RuntimeException("This manager is not authorized to approve this request");
        }

        // Save the changes
        return timeOffRequestRepository.save(request);
    }

    public TimeOffRequest rejectTimeOffRequest(Long requestId, Long managerId) {
        // Get the time off request
        TimeOffRequest request = timeOffRequestRepository.findById(requestId).orElseThrow(() -> new ResourceNotFoundException("TimeOffRequest not found"));

        // Get the manager
        Manager manager = managerRepository.findById(managerId).orElseThrow(() -> new ResourceNotFoundException("Manager not found"));

        // Check if this manager is in the list of approvers
        if (request.getApprovers().contains(manager)) {
            // Reject the request
            request.setTimeOffStatus(TimeOffStatus.REJECTED);
        } else {
            throw new RuntimeException("This manager is not authorized to reject this request");
        }

        // Save the changes
        return timeOffRequestRepository.save(request);
    }
}
