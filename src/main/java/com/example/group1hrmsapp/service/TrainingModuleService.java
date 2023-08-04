package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.Benefit;
import com.example.group1hrmsapp.model.Employee;
import com.example.group1hrmsapp.model.TrainingModule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface TrainingModuleService {
    List<TrainingModule> getAllTrainingModules();
    TrainingModule getTrainingModuleById(Long id);
    TrainingModule createTrainingModule(TrainingModule trainingModule);
    void updateTrainingModule(TrainingModule trainingModule);
    void deleteTrainingModuleById(Long id);
    List<TrainingModule> getCompletedTrainingModulesByEmployee(Employee employee);
    void markTrainingAsCompleted(Employee employee, TrainingModule trainingModule);
    Employee getLoggedInUser();
    Page<TrainingModule> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
    Page<TrainingModule> findFilteredAndPaginated(Specification<TrainingModule> spec, Pageable pageable);
    Specification<TrainingModule> prepareSpecification(String moduleName, Boolean assigned, Boolean completed);

    void assignTraining(List<Long> employeeIds, Long trainingId);

    void saveTraining(TrainingModule training);
}
