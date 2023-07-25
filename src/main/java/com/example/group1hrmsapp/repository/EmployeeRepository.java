package com.example.group1hrmsapp.repository;

import com.example.group1hrmsapp.model.AppUser;
import com.example.group1hrmsapp.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;


import java.util.List;
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

    Optional<Employee> findByAppUser(AppUser user);

    /**
     * This method returns a list of employees that match a specific special type.
     * @param specialType The specific type of employee to find in the database. This corresponds to the 'special_type' column in the 'employees' table.
     * @return A list of Employee objects that match the specified special type. Returns an empty list if no such employees are found.
     */
    /*@Query(value = "SELECT * FROM employees WHERE special_type = :specialType", nativeQuery = true)
    List<Employee> findBySpecialType(@Param("specialType") String specialType);*/

}
