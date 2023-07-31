package com.example.group1hrmsapp.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * The WorkedHours class represents an employee's worked hours in the HRMS application.
 */
@Entity
@Table(name = "hours")
public class WorkedHours implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Employee employee;
    @Column(name = "hours_worked")
    private double hoursWorked;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "start_date")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "end_date")
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ApprovalStatus approvalStatus;

    /**
     * Get the ID of the WorkedHours.
     * @return id of the WorkedHours.
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the ID of the WorkedHours.
     * @param id the id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the employee associated with the WorkedHours.
     * @return employee of the WorkedHours.
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Set the employee associated with the WorkedHours.
     * @param employee the employee to set.
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * Get the hours worked by the employee.
     * @return hoursWorked by the employee.
     */
    public double getHoursWorked() {
        return hoursWorked;
    }

    /**
     * Set the hours worked by the employee.
     * @param hoursWorked the hours worked to set.
     */
    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    /**
     * Get the start date of the work period.
     * @return startDate of the work period.
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Set the start date of the work period.
     * @param startDate the start date to set.
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Get the end date of the work period.
     * @return endDate of the work period.
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Set the end date of the work period.
     * @param endDate the end date to set.
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Get the approval status of the worked hours.
     * @return approvalStatus of the worked hours.
     */
    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    /**
     * Set the approval status of the worked hours.
     * @param approvalStatus the approval status to set.
     */
    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
}
