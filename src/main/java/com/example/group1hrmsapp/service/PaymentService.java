package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.Employee;
import com.example.group1hrmsapp.model.Payment;
import com.example.group1hrmsapp.model.WorkedHours;

import java.util.List;

public interface PaymentService {
    List<Payment> getAllPayments();
    Payment getPaymentById(Long paymentId);
    void savePayment(Payment payment);
    void deletePaymentById(Long paymentId);
    boolean processPayroll(Long paymentId);
    Employee getLoggedInUser();
    void archivePayment(Payment payment);
    Double calculateCost(Payment payment, WorkedHours workedHours);
}
