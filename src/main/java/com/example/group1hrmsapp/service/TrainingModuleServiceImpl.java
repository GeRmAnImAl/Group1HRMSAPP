package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.controller.TrainingModuleController;
import com.example.group1hrmsapp.model.*;
import com.example.group1hrmsapp.repository.TrainingModuleRepository;
import com.example.group1hrmsapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Service implementation for managing training module operations.
 * Provides functionalities to manage training modules such as fetching all training modules,
 * getting a module by ID, creating, updating, and deleting a module. Also, includes functionalities
 * related to marking trainings as completed, getting completed trainings of an employee, and other relevant
 * operations.
 */
@Service
public class TrainingModuleServiceImpl implements TrainingModuleService{
    @Autowired
    private TrainingModuleRepository trainingModuleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmployeeService employeeService;

    private static final Logger LOGGER = Logger.getLogger(TrainingModuleController.class.getName());

    /**
     * Retrieves all training modules from the repository.
     *
     * @return List of all {@link TrainingModule} objects.
     */
    @Override
    public List<TrainingModule> getAllTrainingModules() {
        return trainingModuleRepository.findAll();
    }

    /**
     * Fetches a specific training module by its ID.
     * Throws a runtime exception if the training module is not found.
     *
     * @param id The unique identifier of the training module.
     * @return {@link TrainingModule} object.
     */
    @Override
    public TrainingModule getTrainingModuleById(Long id) {
        TrainingModule trainingModule = trainingModuleRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Training Module not found for id :: " + id));
        LOGGER.info("Module ID from ServiceImpl: " + trainingModule.getId());
        return trainingModule;
    }

    /**
     * Creates a new training module and stores it in the repository.
     *
     * @param trainingModule The training module to be created.
     * @return The saved {@link TrainingModule} object.
     */
    @Override
    public TrainingModule createTrainingModule(TrainingModule trainingModule) {
        TrainingModule newTraining = trainingModule;
        trainingModuleRepository.save(newTraining);

        return newTraining;
    }

    /**
     * Updates a specific training module.
     * Throws a runtime exception if the training module is not found.
     *
     * @param trainingModule The training module to be updated.
     */
    @Override
    public void updateTrainingModule(TrainingModule trainingModule) {
        TrainingModule originalTraining = trainingModuleRepository.findById(trainingModule.getId())
                .orElseThrow(()-> new RuntimeException("No Training Module with That ID"));

        originalTraining.setModuleName(trainingModule.getModuleName());
        originalTraining.setModuleInfo(trainingModule.getModuleInfo());

        trainingModuleRepository.save(originalTraining);
    }

    /**
     * Deletes a training module with the specified ID.
     * Throws a runtime exception if the training module is not found.
     *
     * @param id The unique identifier of the training module to be deleted.
     */
    @Override
    public void deleteTrainingModuleById(Long id){
        Optional<TrainingModule> optionalTrainingModule = trainingModuleRepository.findById(id);
        if (optionalTrainingModule.isPresent()) {
            TrainingModule trainingModuleToDelete = optionalTrainingModule.get();

            trainingModuleRepository.delete(trainingModuleToDelete);
        } else {
            throw new RuntimeException("Training Module not found for id :: " + id);
        }
    }

    /**
     * Retrieves a list of completed training modules for a given employee.
     *
     * @param employee The employee whose completed training modules are to be retrieved.
     * @return List of completed {@link TrainingModule} objects.
     */
    @Override
    public List<TrainingModule> getCompletedTrainingModulesByEmployee(Employee employee) {
        if(employee == null){
            throw new IllegalArgumentException("Employee is null");
        }

        List<TrainingModule> trainingModuleList = new ArrayList<>();
        for(Map.Entry<TrainingModule, Boolean> entry : employee.getAssignedTrainings().entrySet()){
            if(Boolean.TRUE.equals(entry.getValue())){
                trainingModuleList.add(entry.getKey());
            }
        }

        return trainingModuleList;
    }

    /**
     * Marks a specific training module as completed for a given employee.
     *
     * @param employee The employee who completed the training module.
     * @param trainingModule The completed training module.
     */
    @Override
    public void markTrainingAsCompleted(Employee employee, TrainingModule trainingModule) {
        employee.updateTrainingStatus(trainingModule, true);
        employeeService.updateEmployee(employee);
    }

    /**
     * Retrieves the currently logged-in user's details.
     *
     * @return The {@link Employee} object representing the logged-in user.
     */
    @Override
    public Employee getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        AppUser loggedInUser = userRepository.findByUserName(loggedInUsername)
                .orElseThrow(()-> new RuntimeException("No user logged in"));

        return loggedInUser.getEmployee();
    }

    /**
     * Retrieves a paginated list of training modules sorted based on the given criteria.
     *
     * @param pageNo The page number.
     * @param pageSize The size of the page.
     * @param sortField The field to sort by.
     * @param sortDirection The direction of the sort (e.g., "asc" or "desc").
     * @return A paginated list of {@link TrainingModule} objects.
     */
    @Override
    public Page<TrainingModule> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo -1, pageSize, sort);
        return this.trainingModuleRepository.findAll(pageable);
    }

    /**
     *
     * @param spec The specification criteria used to filter training modules.
     * @param pageable The pagination information.
     * @return
     */
    @Override
    public Page<TrainingModule> findFilteredAndPaginated(Specification<TrainingModule> spec, Pageable pageable) {
        return trainingModuleRepository.findAll(spec, pageable);
    }

    /**
     * Prepares a {@link Specification} for the {@link TrainingModule} based on the given filters.
     * This specification can then be used with JPA to fetch filtered results from the database.
     * <p>
     * The filters can include:
     * <ul>
     *   <li>The name of the module</li>
     *   <li>Whether the training is assigned</li>
     *   <li>Whether the training is completed</li>
     * </ul>
     * </p>
     *
     * @param moduleName The name of the training module (partial names are matched).
     * @param assigned Flag indicating if the training is assigned (can be null).
     * @param completed Flag indicating if the training is completed (can be null).
     * @return A {@link Specification} object that can be used to filter {@link TrainingModule} entities.
     */
    @Override
    public Specification<TrainingModule> prepareSpecification(String moduleName, Boolean assigned, Boolean completed) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Employee loggedInEmployee = getLoggedInUser();
            Map<TrainingModule, Boolean> assignedTrainings = loggedInEmployee.getAssignedTrainings();

            if(moduleName != null && !moduleName.isEmpty()){
                String partialTrainingName = "%" + moduleName + "%";
                predicates.add(criteriaBuilder.like(root.get("moduleName"), moduleName));
            }

            if (assigned != null && assignedTrainings != null) {
                if (assigned) {
                    predicates.add(root.in(assignedTrainings.keySet()));
                } else {
                    predicates.add(criteriaBuilder.not(root.in(assignedTrainings.keySet())));
                }
            }

            if (completed != null && assignedTrainings != null) {
                Set<TrainingModule> completedTrainings = assignedTrainings.entrySet().stream()
                        .filter(entry -> entry.getValue().equals(completed))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toSet());

                if (!completedTrainings.isEmpty()) {
                    predicates.add(root.in(completedTrainings));
                } else {
                    // In case there are no trainings matching the 'completed' criteria,
                    // we add a predicate that always evaluates to false
                    predicates.add(criteriaBuilder.disjunction());
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Assigns a training module to multiple employees based on their IDs.
     *
     * @param employeeIds List of employee IDs to whom the training is to be assigned.
     * @param trainingId The ID of the training module to be assigned.
     */
    @Transactional
    @Override
    public void assignTraining(List<Long> employeeIds, Long trainingId) {
        TrainingModule trainingModule = this.getTrainingModuleById(trainingId);

        for(Long id : employeeIds){
            Employee employee = employeeService.getEmployeeById(id);
            employee.getAssignedTrainings().put(trainingModule, false);
            employeeService.updateEmployee(employee);
        }
    }

    /**
     * Saves the provided training module in the repository.
     *
     * @param training The training module to be saved.
     */
    @Override
    public void saveTraining(TrainingModule training) {
        trainingModuleRepository.save(training);
    }
}
