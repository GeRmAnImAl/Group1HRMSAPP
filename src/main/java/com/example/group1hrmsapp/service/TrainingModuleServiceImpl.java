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

@Service
public class TrainingModuleServiceImpl implements TrainingModuleService{
    @Autowired
    private TrainingModuleRepository trainingModuleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmployeeService employeeService;

    private static final Logger LOGGER = Logger.getLogger(TrainingModuleController.class.getName());



    @Override
    public List<TrainingModule> getAllTrainingModules() {
        return trainingModuleRepository.findAll();
    }

    @Override
    public TrainingModule getTrainingModuleById(Long id) {
        TrainingModule trainingModule = trainingModuleRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Training Module not found for id :: " + id));
        LOGGER.info("Module ID from ServiceImpl: " + trainingModule.getId());
        return trainingModule;
    }

    @Override
    public TrainingModule createTrainingModule(TrainingModule trainingModule) {
        TrainingModule newTraining = trainingModule;
        trainingModuleRepository.save(newTraining);

        return newTraining;
    }

    @Override
    public void updateTrainingModule(TrainingModule trainingModule) {
        TrainingModule originalTraining = trainingModuleRepository.findById(trainingModule.getId())
                .orElseThrow(()-> new RuntimeException("No Training Module with That ID"));

        originalTraining.setModuleName(trainingModule.getModuleName());
        originalTraining.setModuleInfo(trainingModule.getModuleInfo());

        trainingModuleRepository.save(originalTraining);
    }


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

    @Override
    public void markTrainingAsCompleted(Employee employee, TrainingModule trainingModule) {
        employee.updateTrainingStatus(trainingModule, true);
        employeeService.updateEmployee(employee);
    }

    @Override
    public Employee getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        AppUser loggedInUser = userRepository.findByUserName(loggedInUsername)
                .orElseThrow(()-> new RuntimeException("No user logged in"));

        return loggedInUser.getEmployee();
    }

    @Override
    public Page<TrainingModule> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo -1, pageSize, sort);
        return this.trainingModuleRepository.findAll(pageable);
    }


    @Override
    public Page<TrainingModule> findFilteredAndPaginated(Specification<TrainingModule> spec, Pageable pageable) {
        return trainingModuleRepository.findAll(spec, pageable);
    }

    @Override
    public Specification<TrainingModule> prepareSpecification(String moduleName, Boolean assigned, Boolean completed) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(moduleName != null && !moduleName.isEmpty()){
                String partialTrainingName = "%" + moduleName + "%";
                predicates.add(criteriaBuilder.like(root.get("moduleName"), moduleName));
            }

            if (assigned != null) {
                predicates.add(criteriaBuilder.equal(root.get("assigned"), assigned));
            }

            if (completed != null) {
                predicates.add(criteriaBuilder.equal(root.get("completed"), completed));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

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

    @Override
    public void saveTraining(TrainingModule training) {
        trainingModuleRepository.save(training);
    }
}
