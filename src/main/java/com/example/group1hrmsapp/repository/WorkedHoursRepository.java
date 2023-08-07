package com.example.group1hrmsapp.repository;

import com.example.group1hrmsapp.model.WorkedHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for accessing worked hours data from the database.
 * This interface extends JpaRepository to inherit common CRUD operations and
 * JpaSpecificationExecutor for complex queries or criteria based operations.
 * Additional methods for custom queries can be added if necessary.
 */
@Repository
public interface WorkedHoursRepository extends JpaRepository<WorkedHours, Long>, JpaSpecificationExecutor<WorkedHours> {
    /**
     * Retrieves a WorkedHours entry by a specific ID number.
     *
     * @param id The unique identifier representing the WorkedHours entry in the database.
     * @return an Optional containing the found WorkedHours entry or empty if not found.
     */
    @Override
    Optional<WorkedHours> findById(Long id);
}
