package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.*;
import com.example.group1hrmsapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Implementation class to manage payment-related operations within the HRMS application.
 */
@Service
public class PaymentServiceImpl implements PaymentService{
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private ArchivedPaymentRepository archivedPaymentRepository;
    @Autowired
    private WorkedHoursService workedHoursService;
    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves a list of all payments in the system.
     *
     * @return List of {@link Payment} objects.
     */
    @Override
    public List<Payment> getAllPayments(){
        return paymentRepository.findAll();
    }

    /**
     * Retrieves a payment by its unique identifier.
     *
     * @param paymentId The unique identifier of the payment.
     * @return A {@link Payment} object if found.
     * @throws RuntimeException if the payment is not found.
     */
    @Override
    public Payment getPaymentById(Long paymentId) {
        Optional<Payment> optional = paymentRepository.findById(paymentId);
        Payment payment = null;
        if(optional.isPresent()){
            payment = optional.get();
        }
        else{
            throw new RuntimeException("Payment not found for id :: " + paymentId);
        }
        return payment;
    }

    /**
     * Persists the given payment in the system.
     *
     * @param payment The payment to be saved.
     */
    @Override
    public void savePayment(Payment payment) {
        paymentRepository.save(payment);
    }

    /**
     * Deletes the payment with the given identifier from the system.
     *
     * @param paymentId The unique identifier of the payment to be deleted.
     * @throws RuntimeException if the payment is not found.
     */
    @Override
    public void deletePaymentById(Long paymentId){
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        if (optionalPayment.isPresent()){
            Payment paymentToDelete = optionalPayment.get();
            paymentRepository.delete(paymentToDelete);
        }
        else{
            throw new RuntimeException("Payment not found for id :: " + paymentId);
        }
    }

    /**
     * Processes the payroll for the payment with the given identifier.
     *
     * @param paymentId The unique identifier of the payment to be processed.
     * @return True if the payroll processing was successful.
     * @throws RuntimeException if the payment is not found.
     */
    @Override
    public boolean processPayroll(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(()-> new RuntimeException("No payment with that ID"));
        payment.setPaymentStatus(PaymentStatus.PAID);
        archivePayment(payment);



        return true;
    }

    /**
     * Retrieves the currently logged-in user's associated Employee record.
     *
     * @return The associated {@link Employee} object for the currently logged-in user.
     * @throws RuntimeException if the user is not logged in.
     */
    @Override
    public Employee getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        AppUser loggedInUser = userRepository.findByUserName(loggedInUsername)
                .orElseThrow(()-> new RuntimeException("No user logged in"));

        return loggedInUser.getEmployee();
    }

    /**
     * Archives the provided payment in the system.
     *
     * @param payment The payment to be archived.
     */
    @Transactional
    @Override
    public void archivePayment(Payment payment) {
        ArchivedPayment archivedPayment = new ArchivedPayment(payment);
        archivedPaymentRepository.save(archivedPayment);

        Iterator<WorkedHours> workedHoursIterator = payment.getWorkedHoursList().iterator();
        while (workedHoursIterator.hasNext()) {
            WorkedHours workedHours = workedHoursIterator.next();
            workedHoursIterator.remove(); // remove from payment side
            workedHoursService.deleteWorkedHoursById(workedHours.getId());
        }
        deletePaymentById(payment.getId());
    }

    /**
     * Calculates the total cost for the given payment based on the provided worked hours.
     *
     * @param payment     The payment for which the cost is being calculated.
     * @param workedHours The hours worked by the employee.
     * @return The calculated cost as a Double value.
     */
    @Override
    public Double calculateCost(Payment payment, WorkedHours workedHours){
        Double hours = workedHours.getHoursWorked();
        Employee employeeToPay = workedHours.getEmployee();
        Double benefitCostToDeduct = 0.0;
        Double paymentAmount = 0.0;
        if(payment.getPaymentAmount() != null){
            paymentAmount = payment.getPaymentAmount();
        }

        for(Map.Entry<Benefit, LocalDate> entry : employeeToPay.getBenefitList().entrySet()){
            benefitCostToDeduct += entry.getKey().getBenefitCost();
        }

        return paymentAmount + (hours * employeeToPay.getHourlyRate()) - benefitCostToDeduct;
    }
}
