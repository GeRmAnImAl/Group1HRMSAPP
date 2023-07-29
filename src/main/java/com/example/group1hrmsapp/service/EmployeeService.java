package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.Employee;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Service interface for managing Employee entities.
 * Declares methods for standard CRUD operations and additional operations such as pagination and sorting.
 */
public interface EmployeeService {
    /**
     * Retrieves all Employee Objects.
     * @return a list of Employee Objects.
     */
    List<Employee> getAllEmployees();

    /**
     * Retrieves an employee by a specific ID number.
     * @param id Long representing the employee in the database.
     * @return an Employee Object.
     */
    Employee getEmployeeById(Long id);

    /**
     * Saves an Employee to the database.
     * @param employee Employee Object to be saved to the database.
     */
    void saveEmployee(Employee employee);

    /**
     * Updates an existing Employee in the database.
     * @param employee Employee Object to be updated in the database.
     */
    void updateEmployee(Employee employee);

    /**
     * Deletes an Employee from the database.
     * @param id Long representing the employee in the database.
     */
    void deleteEmployeeById(Long id);

    /**
     * Retrieves a paginated list of Employees sorted by a specific field and direction.
     * @param pageNo The page number to retrieve, starting from 1.
     * @param pageSize The number of Employees per page.
     * @param sortField The field by which to sort the Employees.
     * @param sortDirection The direction to sort the Employees. This should be 'ASC' for ascending order or 'DESC' for descending order.
     * @return A Page of Employees based on the given page number and size, sorted by the given field and direction.
     */
    Page<Employee> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
}
