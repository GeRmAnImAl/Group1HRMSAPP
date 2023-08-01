package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.*;
import com.example.group1hrmsapp.repository.*;
import org.hibernate.jdbc.Work;
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

    @Override
    public List<Payment> getAllPayments(){
        return paymentRepository.findAll();
    }

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

    @Override
    public void savePayment(Payment payment) {
        paymentRepository.save(payment);
    }

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

    @Override
    public boolean processPayroll(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(()-> new RuntimeException("No payment with that ID"));
        payment.setPaymentStatus(PaymentStatus.PAID);
        archivePayment(payment);



        return true;
    }

    @Override
    public Employee getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        AppUser loggedInUser = userRepository.findById(loggedInUsername)
                .orElseThrow(()-> new RuntimeException("No user logged in"));

        return loggedInUser.getEmployee();
    }

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
