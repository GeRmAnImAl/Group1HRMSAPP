package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.Employee;
import com.example.group1hrmsapp.model.Payment;
import com.example.group1hrmsapp.model.WorkedHours;

import java.util.List;

/**
 * Interface providing the contract for payment-related operations within the HRMS application.
 */
public interface PaymentService {

    /**
     * Retrieves a list of all payments in the system.
     *
     * @return List of {@link Payment} objects.
     */
    List<Payment> getAllPayments();

    /**
     * Retrieves a payment by its unique identifier.
     *
     * @param paymentId The unique identifier of the payment.
     * @return A {@link Payment} object if found, otherwise null.
     */
    Payment getPaymentById(Long paymentId);

    /**
     * Persists the given payment in the system.
     *
     * @param payment The payment to be saved.
     */
    void savePayment(Payment payment);

    /**
     * Deletes the payment with the given identifier from the system.
     *
     * @param paymentId The unique identifier of the payment to be deleted.
     */
    void deletePaymentById(Long paymentId);

    /**
     * Processes the payroll for the payment with the given identifier.
     *
     * @param paymentId The unique identifier of the payment to be processed.
     * @return True if the payroll processing was successful, otherwise false.
     */
    boolean processPayroll(Long paymentId);

    /**
     * Retrieves the currently logged-in user's associated Employee record.
     *
     * @return The associated {@link Employee} object for the currently logged-in user.
     */
    Employee getLoggedInUser();

    /**
     * Archives the provided payment in the system.
     *
     * @param payment The payment to be archived.
     */
    void archivePayment(Payment payment);

    /**
     * Calculates the total cost for the given payment based on the provided worked hours.
     *
     * @param payment     The payment for which the cost is being calculated.
     * @param workedHours The hours worked by the employee.
     * @return The calculated cost as a Double value.
     */
    Double calculateCost(Payment payment, WorkedHours workedHours);
}
