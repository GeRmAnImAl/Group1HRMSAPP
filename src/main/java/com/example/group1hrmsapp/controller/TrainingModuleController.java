package com.example.group1hrmsapp.controller;

import com.example.group1hrmsapp.model.Employee;
import com.example.group1hrmsapp.model.TrainingModule;
import com.example.group1hrmsapp.service.EmployeeService;
import com.example.group1hrmsapp.service.TrainingModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.logging.Logger;

@Controller
public class TrainingModuleController {
    private static final Logger LOGGER = Logger.getLogger(TrainingModuleController.class.getName());

    @Autowired
    private TrainingModuleService trainingModuleService;
    @Autowired
    private EmployeeService employeeService;

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

    @PostMapping("/saveTrainingModule")
    public String saveTrainingModule(@ModelAttribute("training") TrainingModule training){
        trainingModuleService.saveTraining(training);

        return "redirect:/trainingsList";
    }

    @PostMapping("/updateTrainingModule")
    public String updateTrainingModule(@ModelAttribute("training") TrainingModule training){

        trainingModuleService.updateTrainingModule(training);
        return "redirect:/trainingsList";
    }

    @GetMapping("/showUpdateTrainingForm/{trainingId}")
    public String showUpdateTrainingForm(@PathVariable(value = "trainingId") Long trainingId, Model model){
        TrainingModule trainingModule = trainingModuleService.getTrainingModuleById(trainingId);

        model.addAttribute("trainingModule", trainingModule);
        return "update_training";
    }


    @GetMapping("/deleteTraining/{trainingId}")
    public String deleteTraining(@PathVariable(value = "trainingId") Long trainingId){
        this.trainingModuleService.deleteTrainingModuleById(trainingId);
        return "redirect:/trainingsList";
    }

    @GetMapping("/showAssignTraining/{trainingId}")
    public String showAssignTraining(@PathVariable(value = "trainingId") Long trainingId, Model model){
        TrainingModule trainingModule = trainingModuleService.getTrainingModuleById(trainingId);
        List<Employee> employees = employeeService.getAllEmployees();

        model.addAttribute("employees", employees);
        model.addAttribute("trainingModule", trainingModule);

        return "assign_training";
    }

    @PostMapping("/assignTraining")
    public String assignTraining(@RequestParam("employeeIds") List<Long> employeeIds, @RequestParam("trainingId") Long trainingId){
        trainingModuleService.assignTraining(employeeIds, trainingId);
        return "redirect:/trainingsList";
    }


    @GetMapping("/trainingDisplay/{trainingId}")
    public String trainingDisplay(@PathVariable Long trainingId, Model model){
        model.addAttribute("training", trainingModuleService.getTrainingModuleById(trainingId));
        return "display_training";
    }

    @PostMapping("/markTrainingComplete")
    public String markTrainingComplete(
            @RequestParam(value = "confirmRead", defaultValue = "false") Boolean confirmRead,
            @RequestParam("trainingId") Long trainingId) {
        LOGGER.info("trainingID: " + trainingId);
        LOGGER.info("confirmRead: " + confirmRead);
        if(!confirmRead){
            return "redirect:/trainingDisplay/" + trainingId;
        }
        Employee loggedInEmployee = trainingModuleService.getLoggedInUser();
        TrainingModule trainingModule = trainingModuleService.getTrainingModuleById(trainingId);
        trainingModuleService.markTrainingAsCompleted(loggedInEmployee,trainingModule);
        return "redirect:/trainingsList";
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

