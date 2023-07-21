package com.example.group1hrmsapp.repository;

import com.example.group1hrmsapp.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Employee entity. This extends JpaRepository to provide standard database operation methods
 * and could be used to add more complex, custom operations related to Employees in the future.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    /**
     * Retrieves an employee by a specific ID number.
     * @param id Long representing the employee in the database.
     * @return an Employee Object.
     */
    @Override
    Optional<Employee> findById(Long id);
}
