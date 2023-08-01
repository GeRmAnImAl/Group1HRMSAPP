package com.example.group1hrmsapp.controller;

import com.example.group1hrmsapp.model.*;
import com.example.group1hrmsapp.repository.UserRepository;
import com.example.group1hrmsapp.service.BenefitService;
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
public class BenefitController {
    @Autowired
    private BenefitService benefitService;
    @Autowired
    private UserRepository userRepository;

    /**
     * Handler for the GET request to get all Benefits.
     * @param model the model to hold attributes for the view.
     * @return the name of the view.
     */
    @GetMapping("/benefitsList")
    public String viewBenefitPage(Model model){
        Employee loggedInEmployee = benefitService.getLoggedInUser();

        model.addAttribute("listBenefits", benefitService.getAllBenefits());
        model.addAttribute("loggedInEmployee", loggedInEmployee);
        return "benefit_list";
    }

    @GetMapping("/showBenefitsForm")
    public String createBenefit(Model model) {
        Benefit benefit = new Benefit();
        model.addAttribute("benefit", benefit);
        return "new_benefit";
    }

    @PostMapping("/saveBenefit")
    public String saveBenefit(@ModelAttribute("benefit") Benefit benefit){
        benefitService.createBenefit(benefit);
        return "redirect:/benefitsList";
    }

    @GetMapping("/enrollInBenefit/{benefitId}")
    public String enrollInBenefit(@PathVariable Long benefitId, Model model) {
        boolean result = benefitService.enrollInBenefit(benefitId);
        model.addAttribute("message", result ? "Successfully enrolled in benefit" : "Failed to enroll in benefit");
        return "enroll_or_withdraw_benefit_result";
    }

    @GetMapping("/withdrawFromBenefit/{benefitId}")
    public String withdrawFromBenefit(@PathVariable Long benefitId, Model model) {
        boolean result = benefitService.withdrawFromBenefit(benefitId);
        model.addAttribute("message", result ? "Successfully withdrawn from benefit" : "Failed to withdraw from benefit");
        return "enroll_or_withdraw_benefit_result";
    }

    @GetMapping("/deleteBenefit/{id}")
    public String deleteBenefit(@PathVariable(value = "id") Long id){
        this.benefitService.deleteBenefitById(id);
        return "redirect:/benefitsList";
    }

    @GetMapping("/benefitDisplay/{benefitId}")
    public String benefitDisplay(@PathVariable Long benefitId, Model model){
        model.addAttribute(benefitService.getBenefitById(benefitId));
        return "display_benefit";
    }

    @PostMapping("/filterBenefits")
    public String filterBenefits(
            @RequestParam(name = "coverageType", required = false) String coverageType,
            @RequestParam(name = "benefitName", required = false) String benefitName,
            @RequestParam(name = "benefitCost", required = false) Double benefitCost,
            @RequestParam(name = "coverageProvider", required = false) String coverageProvider,
            Model model
    ) {

        // Prepare the Specification for filtering
        Specification<Benefit> spec = benefitService.prepareSpecification(coverageType, benefitName, benefitCost, coverageProvider);

        // Fetch the paginated and filtered TimeOffRequests
        Page<Benefit> page = benefitService.findFilteredAndPaginated(spec, PageRequest.of(0, 10)); // Adjust the PageRequest as needed

        List<Benefit> filteredBenefits = page.getContent();

        // Pass the filtered requests to the view
        model.addAttribute("listBenefits", filteredBenefits);

        // Add pagination information to the model
        model.addAttribute("currentPage", 1);
        model.addAttribute("pageSize", 10);
        model.addAttribute("sortField", "requestDate");
        model.addAttribute("sortDir", "asc");
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());

        Employee employee = benefitService.getLoggedInUser();
        model.addAttribute("loggedInEmployee", employee);

        return "benefit_list";
    }

    @GetMapping("/clearBenefitsFilters")
    public String clearFilters(RedirectAttributes redirectAttributes) {
        // Redirect to the filter page after clearing the filters
        // You can set any default filter values you want here
        redirectAttributes.addAttribute("coverageType", "");
        redirectAttributes.addAttribute("benefitName", "");
        redirectAttributes.addAttribute("cost", "");
        redirectAttributes.addAttribute("provider", "");

        return "redirect:/benefitsList";
    }
}
