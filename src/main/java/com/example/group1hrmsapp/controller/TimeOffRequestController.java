package com.example.group1hrmsapp.controller;

import com.example.group1hrmsapp.model.Employee;
import com.example.group1hrmsapp.model.TimeOffRequest;
import com.example.group1hrmsapp.service.EmployeeService;
import com.example.group1hrmsapp.service.TimeOffRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport;
import java.util.List;

/**
 * Controller to manage time off requests in the system.
 */
@Controller
public class TimeOffRequestController {

    @Autowired
    private TimeOffRequestService timeOffRequestService;
    @Autowired
    private EmployeeService employeeService;

    /**
     * Handles the GET request to display all time off requests.
     *
     * @param model the Model object to pass attributes to the view
     * @return the name of the view
     */
    @GetMapping("/timeOffRequestList")
    public String viewTimeOffRequestPage(Model model){
        model.addAttribute("listTimeOffRequests", timeOffRequestService.getAllTimeOffRequests());
        return "time_off_request_list";
    }

    /**
     * Handles the GET request to show the form for creating a new time off request.
     *
     * @param model the Model object to pass attributes to the view
     * @return the name of the view
     */
    @GetMapping("/showTimeOffRequestForm")
    public String createTimeOffRequest(Model model) {
        TimeOffRequest timeOffRequest = new TimeOffRequest();
        model.addAttribute("request", timeOffRequest);
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "new_time_off_request";
    }

    /**
     * Initialize the WebDataBinder used for data binding from web request parameters to JavaBean objects.
     *
     * @param binder the WebDataBinder
     */
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

    /**
     * Handles the POST request to save a new time off request.
     *
     * @param request the time off request to be saved
     * @return the redirect view name
     */
    @PostMapping("/saveRequest")
    public String saveTimeOffRequest(@ModelAttribute("request") TimeOffRequest request){
        if (request.getApprover() == null || request.getApprover().isEmpty()) {
            String approvers = String.valueOf(employeeService.getEmployeeById(request.getEmployee().getManager().getId()));
            request.setApprover(approvers);
        }
        timeOffRequestService.createTimeOffRequest(request);
        return "redirect:/timeOffRequestList";
    }

    /**
     * Handles the GET request to cancel a time off request.
     *
     * @param requestId the id of the time off request to be cancelled
     * @param model the Model object to pass attributes to the view
     * @return the redirect view name
     */
    @GetMapping("/cancelRequest/{requestId}")
    public String cancelTimeOffRequest(@PathVariable Long requestId, Model model) {
        model.addAttribute("request", timeOffRequestService.cancelTimeOffRequest(requestId));
        return "redirect:/timeOffRequestList";
    }

    /**
     * Handles the GET request to approve a time off request.
     *
     * @param requestId the id of the time off request to be approved
     * @param managerId the id of the manager who is approving the request
     * @param model the Model object to pass attributes to the view
     * @return the redirect view name
     */
    @GetMapping("/approve/{requestId}/{managerId}")
    public String approveTimeOffRequest(@PathVariable Long requestId, @PathVariable Long managerId, Model model) {
        model.addAttribute("request", timeOffRequestService.approveTimeOffRequest(requestId, managerId));
        return "redirect:/timeOffRequestList";
    }

    /**
     * Handles the GET request to reject a time off request.
     *
     * @param requestId the id of the time off request to be rejected
     * @param managerId the id of the manager who is rejecting the request
     * @param model the Model object to pass attributes to the view
     * @return the redirect view name
     */
    @GetMapping("/reject/{requestId}/{managerId}")
    public String rejectTimeOffRequest(@PathVariable Long requestId, @PathVariable Long managerId, Model model) {
        model.addAttribute("request", timeOffRequestService.rejectTimeOffRequest(requestId, managerId));
        return "redirect:/timeOffRequestList";
    }

    /**
     * Handles the GET request to show a paginated list of time off requests.
     *
     * @param pageNo the current page number
     * @param pageSize the number of records per page
     * @param sortField the field by which to sort the records
     * @param sortDir the direction of the sort (asc or desc)
     * @param model the Model object to pass attributes to the view
     * @return the name of the view
     */
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

    /**
     * Handles the POST request to filter time off requests.
     *
     * @param startDate the start date of the time off
     * @param endDate the end date of the time off
     * @param timeOffType the type of the time off
     * @param timeOffStatus the status of the time off
     * @param model the Model object to pass attributes to the view
     * @return the name of the view
     */
    @PostMapping("/filterTimeOffRequests")
    public String filterTimeOffRequests(
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "timeOffType", required = false) String timeOffType,
            @RequestParam(name = "timeOffStatus", required = false) String timeOffStatus,
            Model model
    ) {
        // Prepare the Specification for filtering
        Specification<TimeOffRequest> spec = timeOffRequestService.prepareSpecification(startDate, endDate, timeOffType, timeOffStatus);

        // Fetch the paginated and filtered TimeOffRequests
        Page<TimeOffRequest> page = timeOffRequestService.findFilteredAndPaginated(spec, PageRequest.of(0, 10)); // Adjust the PageRequest as needed

        List<TimeOffRequest> filteredRequests = page.getContent();

        // Pass the filtered requests to the view
        model.addAttribute("listTimeOffRequests", filteredRequests);

        // Add pagination information to the model
        model.addAttribute("currentPage", 1);
        model.addAttribute("pageSize", 10);
        model.addAttribute("sortField", "requestDate");
        model.addAttribute("sortDir", "asc");
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());

        return "time_off_request_list";
    }

    /**
     * Handles the GET request to clear all filters applied to time off requests.
     *
     * @param redirectAttributes attributes to be added to the redirect view
     * @return the redirect view name
     */
    @GetMapping("/clearRequestFilters")
    public String clearFilters(RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("startDate", "");
        redirectAttributes.addAttribute("endDate", "");
        redirectAttributes.addAttribute("timeOffType", "");
        redirectAttributes.addAttribute("timeOffStatus", "");

        return "redirect:/timeOffRequestList";
    }


}
