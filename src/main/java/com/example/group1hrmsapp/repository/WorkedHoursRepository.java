package com.example.group1hrmsapp.repository;

import com.example.group1hrmsapp.model.WorkedHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkedHoursRepository extends JpaRepository<WorkedHours, Long>, JpaSpecificationExecutor<WorkedHours> {
    /**
     * Retrieves a WorkedHours Object by a specific ID number.
     *
     * @param id Long representing the WorkedHours in the database.
     * @return a WorkedHours Object.
     */
    @Override
    Optional<WorkedHours> findById(Long id);
}
