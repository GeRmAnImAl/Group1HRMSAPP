package com.example.group1hrmsapp.repository;

import com.example.group1hrmsapp.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Override
    Optional<Employee> findById(Long id);
    @Query(value = "SELECT * FROM employees WHERE special_type = :specialType", nativeQuery = true)
    List<Employee> findBySpecialType(@Param("specialType") String specialType);

}
