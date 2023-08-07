package com.example.group1hrmsapp.repository;

import com.example.group1hrmsapp.model.TrainingModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for accessing training module data from the database.
 * This interface extends JpaRepository to inherit common CRUD operations and
 * JpaSpecificationExecutor for complex queries or criteria based operations.
 * Additional methods for custom queries can be added if necessary.
 */
@Repository
public interface TrainingModuleRepository extends JpaRepository<TrainingModule, Long>, JpaSpecificationExecutor<TrainingModule> {
    /**
     * Retrieves a TrainingModule by a specific ID number.
     * @param id The unique identifier representing the TrainingModule in the database.
     * @return an Optional containing the found TrainingModule or empty if not found.
     */
    @Override
    Optional<TrainingModule> findById(Long id);
}
