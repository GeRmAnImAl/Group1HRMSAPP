package com.example.group1hrmsapp.controller;

import com.example.group1hrmsapp.model.TimeOffRequest;
import com.example.group1hrmsapp.service.TimeOffRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/time-off-requests")
public class TimeOffRequestController {

    @Autowired
    private TimeOffRequestService timeOffRequestService;

    @PostMapping("/employees/{employeeId}")
    public String createTimeOffRequest(@PathVariable Long employeeId,
                                       @ModelAttribute List<Long> approverIds,
                                       TimeOffRequest request, Model model) {
        model.addAttribute("request", timeOffRequestService.createTimeOffRequest(employeeId, approverIds, request));
        return "timeOffRequestDetails";
    }

    @PostMapping("/{requestId}/cancel")
    public String cancelTimeOffRequest(@PathVariable Long requestId, Model model) {
        model.addAttribute("request", timeOffRequestService.cancelTimeOffRequest(requestId));
        return "timeOffRequestDetails";
    }

    @PostMapping("/{requestId}/approve/{managerId}")
    public String approveTimeOffRequest(@PathVariable Long requestId, @PathVariable Long managerId, Model model) {
        model.addAttribute("request", timeOffRequestService.approveTimeOffRequest(requestId, managerId));
        return "timeOffRequestDetails";
    }

    @PostMapping("/{requestId}/reject/{managerId}")
    public String rejectTimeOffRequest(@PathVariable Long requestId, @PathVariable Long managerId, Model model) {
        model.addAttribute("request", timeOffRequestService.rejectTimeOffRequest(requestId, managerId));
        return "timeOffRequestDetails";
    }

    @GetMapping("/{pageNo}/{pageSize}/{sortField}/{sortDirection}")
    public String findPaginated(@PathVariable int pageNo,
                                @PathVariable int pageSize,
                                @PathVariable String sortField,
                                @PathVariable String sortDirection, Model model) {
        Page<TimeOffRequest> page = timeOffRequestService.findPaginated(pageNo, pageSize, sortField, sortDirection);
        model.addAttribute("page", page);
        return "timeOffRequestsList";
    }
}
