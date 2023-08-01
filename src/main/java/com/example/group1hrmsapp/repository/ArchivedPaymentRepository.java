package com.example.group1hrmsapp.repository;

import com.example.group1hrmsapp.model.ArchivedPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchivedPaymentRepository extends JpaRepository<ArchivedPayment, Long> {
}
