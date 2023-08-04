package com.example.group1hrmsapp.controller;

import com.example.group1hrmsapp.model.Benefit;
import com.example.group1hrmsapp.model.Employee;
import com.example.group1hrmsapp.model.SpecialType;
import com.example.group1hrmsapp.model.TrainingModule;
import com.example.group1hrmsapp.service.TrainingModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TrainingModuleController {
    @Autowired
    private TrainingModuleService trainingModuleService;

    @GetMapping("/trainingsList")
    public String viewTrainingPage(Model model){
        Employee loggedInEmployee = trainingModuleService.getLoggedInUser();

        model.addAttribute("listTrainings", trainingModuleService.getAllTrainingModules());
        model.addAttribute("loggedInEmployee", loggedInEmployee);
        return "training_list";
    }

    @GetMapping("/showNewTrainingForm")
    public String showNewTrainingForm(Model model){
        TrainingModule trainingModule = new TrainingModule();

        model.addAttribute("trainingModule", trainingModule);
        return "new_training";
    }

    @GetMapping("/showUpdateTrainingForm/{id}")
    public String showUpdateTrainingForm(@PathVariable(value = "id") Long id, Model model){
        TrainingModule trainingModule = trainingModuleService.getTrainingModuleById(id);

        model.addAttribute("trainingModule", trainingModule);
        return "update_training";
    }

    @GetMapping("/deleteTraining/{id}")
    public String deleteTraining(@PathVariable(value = "id") Long id){
        this.trainingModuleService.deleteTrainingModuleById(id);
        return "redirect:/trainingsList";
    }

    @GetMapping("/trainingDisplay/{id}")
    public String trainingDisplay(@PathVariable Long id, Model model){
        model.addAttribute(trainingModuleService.getTrainingModuleById(id));
        return "display_training";
    }

    @PostMapping("/filterTrainings")
    public String filterTrainings(
            @RequestParam(name = "moduleName", required = false) String moduleName,
            @RequestParam(name = "assigned", required = false) Boolean assigned,
            @RequestParam(name = "completed", required = false) Boolean completed,
            Model model
    ) {

        // Prepare the Specification for filtering
        Specification<TrainingModule> spec = trainingModuleService.prepareSpecification(moduleName, assigned, completed);

        // Fetch the paginated and filtered TimeOffRequests
        Page<TrainingModule> page = trainingModuleService.findFilteredAndPaginated(spec, PageRequest.of(0, 10)); // Adjust the PageRequest as needed

        List<TrainingModule> filteredTrainings = page.getContent();

        // Pass the filtered requests to the view
        model.addAttribute("listTrainings", filteredTrainings);

        // Add pagination information to the model
        model.addAttribute("currentPage", 1);
        model.addAttribute("pageSize", 10);
        model.addAttribute("sortField", "requestDate");
        model.addAttribute("sortDir", "asc");
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());

        Employee employee = trainingModuleService.getLoggedInUser();
        model.addAttribute("loggedInEmployee", employee);

        return "training_list";
    }

    @GetMapping("/clearTrainingsFilters")
    public String clearFilters(RedirectAttributes redirectAttributes) {
        // Redirect to the filter page after clearing the filters
        // You can set any default filter values you want here
        redirectAttributes.addAttribute("moduleName", "");
        redirectAttributes.addAttribute("assigned", "");
        redirectAttributes.addAttribute("completed", "");

        return "redirect:/trainingsList";
    }
}

