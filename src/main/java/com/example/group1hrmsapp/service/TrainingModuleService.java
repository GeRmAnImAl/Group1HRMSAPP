package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.Employee;
import com.example.group1hrmsapp.model.TrainingModule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Service interface to manage training module operations within the HRMS application.
 */
public interface TrainingModuleService {

    /**
     * Retrieves all training modules.
     *
     * @return List of all {@link TrainingModule} objects.
     */
    List<TrainingModule> getAllTrainingModules();

    /**
     * Retrieves a specific training module by its ID.
     *
     * @param id The unique identifier of the training module.
     * @return {@link TrainingModule} object.
     */
    TrainingModule getTrainingModuleById(Long id);

    /**
     * Creates a new training module.
     *
     * @param trainingModule The training module to be created.
     * @return The created {@link TrainingModule} object.
     */
    TrainingModule createTrainingModule(TrainingModule trainingModule);

    /**
     * Updates a specific training module.
     *
     * @param trainingModule The training module to be updated.
     */
    void updateTrainingModule(TrainingModule trainingModule);

    /**
     * Deletes a training module by its ID.
     *
     * @param id The unique identifier of the training module to be deleted.
     */
    void deleteTrainingModuleById(Long id);

    /**
     * Retrieves all training modules completed by a specific employee.
     *
     * @param employee The employee whose completed training modules are to be retrieved.
     * @return List of completed {@link TrainingModule} objects.
     */
    List<TrainingModule> getCompletedTrainingModulesByEmployee(Employee employee);

    /**
     * Marks a specific training module as completed for an employee.
     *
     * @param employee The employee who completed the training module.
     * @param trainingModule The completed training module.
     */
    void markTrainingAsCompleted(Employee employee, TrainingModule trainingModule);

    /**
     * Retrieves the currently logged-in user's associated Employee record.
     *
     * @return The associated {@link Employee} object for the currently logged-in user.
     */
    Employee getLoggedInUser();

    /**
     * Retrieves a paginated list of training modules.
     *
     * @param pageNo The page number.
     * @param pageSize The size of the page.
     * @param sortField The field to sort by.
     * @param sortDirection The direction of the sort (e.g., "asc" or "desc").
     * @return A {@link Page} of {@link TrainingModule} objects.
     */
    Page<TrainingModule> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

    /**
     * Retrieves a paginated and filtered list of training modules.
     *
     * @param spec The specification criteria used to filter training modules.
     * @param pageable The pagination information.
     * @return A {@link Page} of {@link TrainingModule} objects.
     */
    Page<TrainingModule> findFilteredAndPaginated(Specification<TrainingModule> spec, Pageable pageable);

    /**
     * Prepares the specification for filtering training modules.
     *
     * @param moduleName Name of the module.
     * @param assigned Flag indicating if the training is assigned.
     * @param completed Flag indicating if the training is completed.
     * @return A {@link Specification} of {@link TrainingModule}.
     */
    Specification<TrainingModule> prepareSpecification(String moduleName, Boolean assigned, Boolean completed);

    /**
     * Assigns a training module to a list of employees.
     *
     * @param employeeIds List of employee IDs to whom the training is to be assigned.
     * @param trainingId The ID of the training module to be assigned.
     */
    void assignTraining(List<Long> employeeIds, Long trainingId);

    /**
     * Persists a training module in the system.
     *
     * @param training The training module to be saved.
     */
    void saveTraining(TrainingModule training);
}
