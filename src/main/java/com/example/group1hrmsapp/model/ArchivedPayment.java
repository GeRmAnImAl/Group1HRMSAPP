package com.example.group1hrmsapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The ArchivedPayment class represents an archived payment made to an employee in the HRMS application.
 */
@Entity
@Table(name = "archived_payments")
public class ArchivedPayment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Employee employee;
    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkedHours> workedHoursList = new ArrayList<>();
    @Column(name = "amount")
    private Double paymentAmount;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus paymentStatus;

    public ArchivedPayment() {}

    public ArchivedPayment(Payment payment) {
        this.id = payment.getId();
        this.employee = payment.getEmployee();
        this.workedHoursList = payment.getWorkedHoursList();
        this.paymentAmount = payment.getPaymentAmount();
        this.paymentStatus = payment.getPaymentStatus();
    }

    /**
     * Get the ID of the Payment.
     * @return id of the Payment.
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the ID of the Payment.
     * @param id the id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the Employee associated with the Payment.
     * @return employee associated with the Payment.
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Set the Employee associated with the Payment.
     * @param employee the Employee to set.
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * Get the list of WorkedHours associated with the Payment.
     * @return workedHoursList associated with the Payment.
     */
    public List<WorkedHours> getWorkedHoursList() {
        return workedHoursList;
    }

    /**
     * Set the list of WorkedHours associated with the Payment.
     * @param workedHoursList the list of WorkedHours to set.
     */
    public void setWorkedHoursList(List<WorkedHours> workedHoursList) {
        this.workedHoursList = workedHoursList;
    }

    /**
     * Get the amount of the Payment.
     * @return paymentAmount of the Payment.
     */
    public Double getPaymentAmount() {
        return paymentAmount;
    }

    /**
     * Set the amount of the Payment.
     * @param paymentAmount the amount to set.
     */
    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
