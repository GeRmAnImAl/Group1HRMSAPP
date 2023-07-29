package com.example.group1hrmsapp.repository;

import com.example.group1hrmsapp.model.Benefit;
import com.example.group1hrmsapp.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Benefit entity. This extends JpaRepository to provide standard database operation methods
 * and could be used to add more complex, custom operations related to Benefits in the future.
 */
@Repository
public interface BenefitRepository extends JpaRepository<Benefit, Long> {
    /**
     * Retrieves a benefit by a specific ID number.
     *
     * @param id Long representing the benefit in the database.
     * @return a Benefit Object.
     */
    @Override
    Optional<Benefit> findById(Long id);
}
