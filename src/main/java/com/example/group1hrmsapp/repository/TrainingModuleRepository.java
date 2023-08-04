package com.example.group1hrmsapp.repository;

import com.example.group1hrmsapp.model.TrainingModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainingModuleRepository extends JpaRepository<TrainingModule, Long>, JpaSpecificationExecutor<TrainingModule> {
    /**
     * Retrieves a TrainingModule by a specific ID number.
     *
     * @param id Long representing the TrainingModule in the database.
     * @return a TrainingModule Object.
     */
    @Override
    Optional<TrainingModule> findById(Long id);
}
