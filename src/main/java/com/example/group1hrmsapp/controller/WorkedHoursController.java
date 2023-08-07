package com.example.group1hrmsapp.controller;

import com.example.group1hrmsapp.model.Employee;
import com.example.group1hrmsapp.model.WorkedHours;
import com.example.group1hrmsapp.service.EmployeeService;
import com.example.group1hrmsapp.service.WorkedHoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller responsible for managing worked hours operations.
 */
@Controller
public class WorkedHoursController {
    @Autowired
    private WorkedHoursService workedHoursService;
    @Autowired
    private EmployeeService employeeService;

    /**
     * Display the list of all worked hours.
     *
     * @param model the Spring UI model.
     * @return the view name for the worked hours list.
     */
    @GetMapping("/workedHoursList")
    public String viewWorkedHoursPage(Model model){
        model.addAttribute("listWorkedHours", workedHoursService.getAllWorkedHours());
        return "worked_hours_list";
    }

    /**
     * Show the form for recording new worked hours.
     *
     * @param model the Spring UI model.
     * @return the view name for the worked hours creation form.
     */
    @GetMapping("/showWorkedHoursForm")
    public String createWorkedHours(Model model) {
        WorkedHours workedHours = new WorkedHours();
        model.addAttribute("workedHours", workedHours);
        return "new_worked_hours";
    }

    /**
     * Save the details of worked hours.
     *
     * @param workedHours the worked hours entity to save.
     * @return redirect view name after saving worked hours.
     */
    @PostMapping("/saveWorkedHours")
    public String saveWorkedHours(@ModelAttribute("workedHours") WorkedHours workedHours){
        workedHoursService.createWorkedHours(workedHours);
        return "redirect:/workedHoursList";
    }

    /**
     * Cancel the specific worked hours record.
     *
     * @param workedHoursId the ID of the worked hours record.
     * @param model the Spring UI model.
     * @return redirect view name after cancellation.
     */
    @GetMapping("/cancelWorkedHours/{workedHoursId}")
    public String cancelWorkedHours(@PathVariable Long workedHoursId, Model model) {
        model.addAttribute("workedHoursId", workedHoursService.cancelWorkedHours(workedHoursId));
        return "redirect:/workedHoursList";
    }

    /**
     * Approve the specific worked hours record.
     *
     * @param workedHoursId the ID of the worked hours record.
     * @param managerId the ID of the manager approving the record.
     * @param model the Spring UI model.
     * @return redirect view name after approval.
     */
    @GetMapping("/approveWorkedHours/{workedHoursId}/{managerId}")
    public String approveWorkedHours(@PathVariable Long workedHoursId, @PathVariable Long managerId, Model model) {
        model.addAttribute("request", workedHoursService.approveWorkedHours(workedHoursId, managerId));
        return "redirect:/workedHoursList";
    }

    /**
     * Reject the specific worked hours record.
     *
     * @param workedHoursId the ID of the worked hours record.
     * @param managerId the ID of the manager rejecting the record.
     * @param model the Spring UI model.
     * @return redirect view name after rejection.
     */
    @GetMapping("/rejectWorkedHours/{workedHoursId}/{managerId}")
    public String rejectWorkedHours(@PathVariable Long workedHoursId, @PathVariable Long managerId, Model model) {
        model.addAttribute("request", workedHoursService.rejectWorkedHours(workedHoursId, managerId));
        return "redirect:/workedHoursList";
    }

    /**
     * View paginated records of worked hours.
     *
     * @param pageNo current page number.
     * @param pageSize number of records per page.
     * @param sortField the field to sort by.
     * @param sortDir the direction of sort (asc/desc).
     * @param model the Spring UI model.
     * @return the view name for the paginated worked hours list.
     */
    @GetMapping("/workedHoursPage")
    public String findPaginated(
            @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(name = "size", defaultValue = "10") int pageSize,
            @RequestParam(name = "sortField", defaultValue = "requestDate") String sortField,
            @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
            Model model
    ) {
        Page<WorkedHours> page = workedHoursService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<WorkedHours> workedHours = page.getContent();
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages(); // Calculate total pages

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);

        model.addAttribute("workedHours", workedHours);
        return "worked_hours_list";
    }

    /**
     * Filter the worked hours based on provided criteria.
     *
     * @param employeeId the ID of the employee.
     * @param startDateStr the start date of the filter.
     * @param endDateStr the end date of the filter.
     * @param workedHoursStatus the status of the worked hours.
     * @param model the Spring UI model.
     * @return the view name after filtering worked hours.
     */
    @PostMapping("/filterWorkedHours")
    public String filterWorkedHours(
            @RequestParam(name = "employeeId", required = false) Long employeeId,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDateStr,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDateStr,
            @RequestParam(name = "workedHoursStatus", required = false) String workedHoursStatus,
            Model model
    ) {
        Employee employee = null;
        if(employeeId != null){
            employee = employeeService.getEmployeeById(employeeId);
        }
        // Prepare the Specification for filtering
        Specification<WorkedHours> spec = workedHoursService.prepareSpecification(employee, startDateStr, endDateStr, workedHoursStatus);

        // Fetch the paginated and filtered TimeOffRequests
        Page<WorkedHours> page = workedHoursService.findFilteredAndPaginated(spec, PageRequest.of(0, 10)); // Adjust the PageRequest as needed

        List<WorkedHours> filteredWorkedHours = page.getContent();

        // Pass the filtered requests to the view
        model.addAttribute("listWorkedHours", filteredWorkedHours);

        // Add pagination information to the model
        model.addAttribute("currentPage", 1);
        model.addAttribute("pageSize", 10);
        model.addAttribute("sortField", "requestDate");
        model.addAttribute("sortDir", "asc");
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());

        return "worked_hours_list";
    }

    /**
     * Clear any active filters on the worked hours list.
     *
     * @param redirectAttributes attributes to be added to redirect view.
     * @return redirect view name after clearing filters.
     */
    @GetMapping("/clearWorkedHoursFilters")
    public String clearFilters(RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("startDate", "");
        redirectAttributes.addAttribute("endDate", "");
        redirectAttributes.addAttribute("employeeId", "");
        redirectAttributes.addAttribute("timeOffStatus", "");

        return "redirect:/workedHoursList";
    }
}
