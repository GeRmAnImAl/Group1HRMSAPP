package com.example.group1hrmsapp.repository;

import com.example.group1hrmsapp.model.ArchivedPayment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for accessing archived payment data from the database.
 * This interface extends JpaRepository to inherit common CRUD operations.
 * Additional methods for custom queries can be added if necessary.
 */
public interface ArchivedPaymentRepository extends JpaRepository<ArchivedPayment, Long> {
}
