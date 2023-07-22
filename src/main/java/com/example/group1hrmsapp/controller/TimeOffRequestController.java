package com.example.group1hrmsapp.controller;

import com.example.group1hrmsapp.model.Employee;
import com.example.group1hrmsapp.model.TimeOffRequest;
import com.example.group1hrmsapp.service.EmployeeService;
import com.example.group1hrmsapp.service.TimeOffRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.util.List;

@Controller
public class TimeOffRequestController {

    @Autowired
    private TimeOffRequestService timeOffRequestService;
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/timeOffRequestList")
    public String viewTimeOffRequestPage(Model model){
        model.addAttribute("listTimeOffRequests", timeOffRequestService.getAllTimeOffRequests());
        return "time_off_request_list";
    }

    @GetMapping("/showTimeOffRequestForm")
    public String createTimeOffRequest(Model model) {
        TimeOffRequest timeOffRequest = new TimeOffRequest();
        model.addAttribute("request", timeOffRequest);
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "new_time_off_request";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Employee.class, "employee", new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                Employee employee = employeeService.getEmployeeById(Long.valueOf(text));
                setValue(employee);
            }
        });
    }

    @PostMapping("/saveRequest")
    public String saveTimeOffRequest(@ModelAttribute("request") TimeOffRequest request){
        if (request.getApprovers().isEmpty()) {
            request.setApprovers(null);
        }
        timeOffRequestService.createTimeOffRequest(request);
        return "redirect:/timeOffRequestList";
    }

    @PostMapping("/cancelRequest/{requestId}")
    public String cancelTimeOffRequest(@PathVariable Long requestId, Model model) {
        model.addAttribute("request", timeOffRequestService.cancelTimeOffRequest(requestId));
        return "redirect:/timeOffRequestList";
    }

    @PostMapping("/approve/{requestId}/{managerId}")
    public String approveTimeOffRequest(@PathVariable Long requestId, @PathVariable Long managerId, Model model) {
        model.addAttribute("request", timeOffRequestService.approveTimeOffRequest(requestId, managerId));
        return "redirect:/timeOffRequestList";
    }

    @PostMapping("/reject/{requestId}/{managerId}")
    public String rejectTimeOffRequest(@PathVariable Long requestId, @PathVariable Long managerId, Model model) {
        model.addAttribute("request", timeOffRequestService.rejectTimeOffRequest(requestId, managerId));
        return "redirect:/timeOffRequestList";
    }

    @GetMapping("/requestPage")
    public String findPaginated(
            @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(name = "size", defaultValue = "10") int pageSize,
            @RequestParam(name = "sortField", defaultValue = "requestDate") String sortField,
            @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
            Model model
    ) {
        Page<TimeOffRequest> page = timeOffRequestService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<TimeOffRequest> requests = page.getContent();
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages(); // Calculate total pages

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);

        model.addAttribute("requests", requests);
        return "time_off_request_list";
    }
}
