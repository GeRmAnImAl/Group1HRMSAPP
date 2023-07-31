package com.example.group1hrmsapp.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The WorkedHours class represents an employee's worked hours in the HRMS application.
 */
@Entity
@Table(name = "hours")
public class WorkedHours implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "employeeId")
    private Long employeeId;
    @Column(name = "hours_worked")
    private double hoursWorked;
    @Column(name = "start_date")
    private String startDate;
    @Column(name = "end_date")
    private String endDate;
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
     * Get the employee ID associated with the WorkedHours.
     * @return employeeId of the WorkedHours.
     */
    public Long getEmployeeId() {
        return employeeId;
    }

    /**
     * Set the employee ID associated with the WorkedHours.
     * @param employeeId the employee id to set.
     */
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
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
    public String getStartDate() {
        return startDate;
    }

    /**
     * Set the start date of the work period.
     * @param startDate the start date to set.
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * Get the end date of the work period.
     * @return endDate of the work period.
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Set the end date of the work period.
     * @param endDate the end date to set.
     */
    public void setEndDate(String endDate) {
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
