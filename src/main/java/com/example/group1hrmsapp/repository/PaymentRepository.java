package com.example.group1hrmsapp.repository;

import com.example.group1hrmsapp.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The PaymentRepository interface is a Spring Data JPA repository
 * for Payment entities. It provides methods to perform common database
 * operations for Payment entities.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    /**
     * Retrieves a payment by a specific ID number.
     *
     * @param id Long representing the payment in the database.
     * @return a Payment Object.
     */
    @Override
    Optional<Payment> findById(Long id);

    /**
     * Retrieves all payments associated with a specific employee.
     *
     * @param employeeId Long representing the employee ID.
     * @return a List of Payment Objects.
     */
    List<Payment> findByEmployeeId(Long employeeId);
}
