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

/**
 * Controller responsible for handling training module related actions.
 */
@Controller
public class TrainingModuleController {
    private static final Logger LOGGER = Logger.getLogger(TrainingModuleController.class.getName());

    @Autowired
    private TrainingModuleService trainingModuleService;
    @Autowired
    private EmployeeService employeeService;

    /**
     * Handler for the GET request to view all training modules.
     * @param model the model to hold attributes for the view.
     * @return the name of the view.
     */
    @GetMapping("/trainingsList")
    public String viewTrainingPage(Model model){
        Employee loggedInEmployee = trainingModuleService.getLoggedInUser();

        model.addAttribute("listTrainings", trainingModuleService.getAllTrainingModules());
        model.addAttribute("loggedInEmployee", loggedInEmployee);
        return "training_list";
    }

    /**
     * Display form to add a new training module.
     * @param model the model to hold attributes for the view.
     * @return the name of the view.
     */
    @GetMapping("/showNewTrainingForm")
    public String showNewTrainingForm(Model model){
        TrainingModule trainingModule = new TrainingModule();

        model.addAttribute("trainingModule", trainingModule);
        return "new_training";
    }

    /**
     * Save the provided training module.
     * @param training the training module to save.
     * @return redirect path to training modules list.
     */
    @PostMapping("/saveTrainingModule")
    public String saveTrainingModule(@ModelAttribute("training") TrainingModule training){
        trainingModuleService.saveTraining(training);

        return "redirect:/trainingsList";
    }

    /**
     * Update the provided training module.
     * @param training the training module to update.
     * @return redirect path to training modules list.
     */
    @PostMapping("/updateTrainingModule")
    public String updateTrainingModule(@ModelAttribute("training") TrainingModule training){

        trainingModuleService.updateTrainingModule(training);
        return "redirect:/trainingsList";
    }

    /**
     * Display form to update a training module identified by the given ID.
     * @param trainingId the ID of the training module to update.
     * @param model the model to hold attributes for the view.
     * @return the name of the view.
     */
    @GetMapping("/showUpdateTrainingForm/{trainingId}")
    public String showUpdateTrainingForm(@PathVariable(value = "trainingId") Long trainingId, Model model){
        TrainingModule trainingModule = trainingModuleService.getTrainingModuleById(trainingId);

        model.addAttribute("trainingModule", trainingModule);
        return "update_training";
    }

    /**
     * Delete the training module identified by the given ID.
     * @param trainingId the ID of the training module to delete.
     * @return redirect path to training modules list.
     */
    @GetMapping("/deleteTraining/{trainingId}")
    public String deleteTraining(@PathVariable(value = "trainingId") Long trainingId){
        this.trainingModuleService.deleteTrainingModuleById(trainingId);
        return "redirect:/trainingsList";
    }

    /**
     * Display form to assign a training module to employees.
     * @param trainingId the ID of the training module to assign.
     * @param model the model to hold attributes for the view.
     * @return the name of the view.
     */
    @GetMapping("/showAssignTraining/{trainingId}")
    public String showAssignTraining(@PathVariable(value = "trainingId") Long trainingId, Model model){
        TrainingModule trainingModule = trainingModuleService.getTrainingModuleById(trainingId);
        List<Employee> employees = employeeService.getAllEmployees();

        model.addAttribute("employees", employees);
        model.addAttribute("trainingModule", trainingModule);

        return "assign_training";
    }

    /**
     * Assign the specified training module to selected employees.
     * @param employeeIds the IDs of the employees to assign the training to.
     * @param trainingId the ID of the training module to assign.
     * @return redirect path to training modules list.
     */
    @PostMapping("/assignTraining")
    public String assignTraining(@RequestParam("employeeIds") List<Long> employeeIds, @RequestParam("trainingId") Long trainingId){
        trainingModuleService.assignTraining(employeeIds, trainingId);
        return "redirect:/trainingsList";
    }

    /**
     * Display the details of a specific training module.
     * @param trainingId the ID of the training module to display.
     * @param model the model to hold attributes for the view.
     * @return the name of the view.
     */
    @GetMapping("/trainingDisplay/{trainingId}")
    public String trainingDisplay(@PathVariable Long trainingId, Model model){
        model.addAttribute("training", trainingModuleService.getTrainingModuleById(trainingId));
        return "display_training";
    }

    /**
     * Mark a specific training module as completed by the logged-in user.
     * @param confirmRead a flag indicating whether the training was confirmed as read.
     * @param trainingId the ID of the training module to mark as completed.
     * @return redirect path to training modules list.
     */
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

    /**
     * Filter the training modules based on provided criteria.
     * @param moduleName the name of the training module.
     * @param assigned a flag indicating if the training module is assigned.
     * @param completed a flag indicating if the training module is completed.
     * @param model the model to hold attributes for the view.
     * @return the name of the view.
     */
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

    /**
     * Clear all active training module filters.
     * @param redirectAttributes attributes to redirect.
     * @return redirect path to training modules list.
     */
    @GetMapping("/clearTrainingsFilters")
    public String clearFilters(RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("moduleName", "");
        redirectAttributes.addAttribute("assigned", "");
        redirectAttributes.addAttribute("completed", "");

        return "redirect:/trainingsList";
    }
}

