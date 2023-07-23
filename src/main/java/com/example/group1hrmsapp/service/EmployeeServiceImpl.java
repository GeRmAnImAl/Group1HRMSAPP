package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.Employee;
import com.example.group1hrmsapp.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * A service class that implements the EmployeeService interface to provide core employee-related functionalities.
 * This service is responsible for handling all operations related to Employee objects, such as retrieving, saving,
 * and deleting employees from the repository.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Retrieves all Employee Objects.
     * @return a list of Employee Objects.
     */
    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    /**
     * Retrieves an employee by a specific ID number.
     * @param id Long representing the employee in the database.
     * @return an Employee Object.
     */
    @Override
    public Employee getEmployeeById(Long id) {
        Optional<Employee> optional = employeeRepository.findById(id);
        Employee employee = null;
        if(optional.isPresent()){
            employee = optional.get();
        }
        else{
            throw new RuntimeException("Employee not found for id :: " + id);
        }
        return employee;
    }

    /**
     * Saves an Employee to the database.
     * @param employee Employee Object to be saved to the database.
     */
    @Override
    public void saveEmployee(Employee employee) {
        this.employeeRepository.save(employee);
    }

    /**
     * Deletes an Employee from the database.
     * @param id Long representing the employee in the database.
     */
    @Override
    public void deleteEmployeeById(Long id) {
        this.employeeRepository.deleteById(id);
    }

    /**
     * Retrieves a paginated list of Employees sorted by a specific field and direction.
     * @param pageNo The page number to retrieve, starting from 1.
     * @param pageSize The number of Employees per page.
     * @param sortField The field by which to sort the Employees.
     * @param sortDirection The direction to sort the Employees. This should be 'ASC' for ascending order or 'DESC' for descending order.
     * @return A Page of Employees based on the given page number and size, sorted by the given field and direction.
     */
    @Override
    public Page<Employee> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo -1, pageSize, sort);
        return this.employeeRepository.findAll(pageable);
    }
}
