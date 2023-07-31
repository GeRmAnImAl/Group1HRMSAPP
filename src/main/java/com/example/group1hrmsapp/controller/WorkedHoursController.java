package com.example.group1hrmsapp.controller;

import com.example.group1hrmsapp.model.Employee;
import com.example.group1hrmsapp.model.TimeOffRequest;
import com.example.group1hrmsapp.model.WorkedHours;
import com.example.group1hrmsapp.service.WorkedHoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class WorkedHoursController {
    @Autowired
    private WorkedHoursService workedHoursService;

    @GetMapping("/workedHoursList")
    public String viewWorkedHoursPage(Model model){
        model.addAttribute("listWorkedHours", workedHoursService.getAllWorkedHours());
        return "worked_hours_list";
    }

    @GetMapping("/showWorkedHoursForm")
    public String createWorkedHours(Model model) {
        WorkedHours workedHours = new WorkedHours();
        model.addAttribute("workedHours", workedHours);
        return "new_worked_hours";
    }

    @PostMapping("/saveWorkedHours")
    public String saveWorkedHours(@ModelAttribute("workedHours") WorkedHours workedHours){
        workedHoursService.createWorkedHours(workedHours);
        return "redirect:/workedHoursList";
    }

    @GetMapping("/cancelWorkedHours/{workedHoursId}")
    public String cancelWorkedHours(@PathVariable Long workedHoursId, Model model) {
        model.addAttribute("workedHoursId", workedHoursService.cancelWorkedHours(workedHoursId));
        return "redirect:/workedHoursList";
    }

    @GetMapping("/approve/{workedHoursId}/{managerId}")
    public String approveWorkedHours(@PathVariable Long workedHoursId, @PathVariable Long managerId, Model model) {
        model.addAttribute("request", workedHoursService.approveWorkedHours(workedHoursId, managerId));
        return "redirect:/workedHoursList";
    }

    @GetMapping("/reject/{workedHoursId}/{managerId}")
    public String rejectWorkedHours(@PathVariable Long workedHoursId, @PathVariable Long managerId, Model model) {
        model.addAttribute("request", workedHoursService.rejectWorkedHours(workedHoursId, managerId));
        return "redirect:/workedHoursList";
    }

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

    @PostMapping("/filterTimeOffRequests")
    public String filterTimeOffRequests(
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "employeeId", required = false) String employeeId,
            @RequestParam(name = "timeOffStatus", required = false) String timeOffStatus,
            Model model
    ) {
        // Prepare the Specification for filtering
        Specification<WorkedHours> spec = workedHoursService.prepareSpecification(employeeId, startDate, endDate, timeOffStatus);

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

    @GetMapping("/clearWorkedHoursFilters")
    public String clearFilters(RedirectAttributes redirectAttributes) {
        // Redirect to the filter page after clearing the filters
        // You can set any default filter values you want here
        redirectAttributes.addAttribute("startDate", "");
        redirectAttributes.addAttribute("endDate", "");
        redirectAttributes.addAttribute("employeeId", "");
        redirectAttributes.addAttribute("timeOffStatus", "");

        return "redirect:/workedHoursList";
    }
}
