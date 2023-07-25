package com.example.group1hrmsapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This entity class represents a time off request made by an employee in the HRMS (Human Resource Management System) app.
 * It is also a subject in the observer pattern, which means it can be observed by other objects.
 */
@Entity
@Table(name = "time_off_requests")
public class TimeOffRequest implements Serializable, Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Employee employee;
    @Column
    private String startDate;
    @Column
    private String endDate;
    @Column
    private String requestDate;
    @Column
    @Enumerated(EnumType.STRING)
    private TimeOffType timeOffType;
    @Column
    @Enumerated(EnumType.STRING)
    private TimeOffStatus timeOffStatus;
    @Column
    private double duration;

    @Transient
    private List<Observer> observers = new ArrayList<>();

    @Column
    private String approver = "";

    /**
     * Gets the id of this TimeOffRequest.
     * @return The id of this TimeOffRequest.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the id of this TimeOffRequest.
     * @param id The id to set.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the Employee who made this TimeOffRequest.
     * @return The Employee who made this TimeOffRequest.
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Sets the Employee who made this TimeOffRequest.
     * @param employee The Employee to set.
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * Gets the start date of this TimeOffRequest.
     * @return The start date of this TimeOffRequest.
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of this TimeOffRequest.
     * @param startDate The start date to set.
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the end date of this TimeOffRequest.
     * @return The end date of this TimeOffRequest.
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of this TimeOffRequest.
     * @param endDate The end date to set.
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the request date of this TimeOffRequest.
     * @return The request date of this TimeOffRequest.
     */
    public String getRequestDate() {
        return requestDate;
    }

    /**
     * Sets the request date of this TimeOffRequest.
     * @param requestDate The request date to set.
     */
    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    /**
     * Gets the type of this TimeOffRequest.
     * @return The type of this TimeOffRequest.
     */
    public TimeOffType getTimeOffType() {
        return timeOffType;
    }

    /**
     * Sets the type of this TimeOffRequest.
     * @param timeOffType The type to set.
     */
    public void setTimeOffType(TimeOffType timeOffType) {
        this.timeOffType = timeOffType;
    }

    /**
     * Gets the status of this TimeOffRequest.
     * @return The status of this TimeOffRequest.
     */
    public TimeOffStatus getTimeOffStatus() {
        return timeOffStatus;
    }

    /**
     * Sets the status of this TimeOffRequest.
     * @param timeOffStatus The status to set.
     */
    public void setTimeOffStatus(TimeOffStatus timeOffStatus) {
        this.timeOffStatus = timeOffStatus;
    }

    /**
     * Gets the duration of this TimeOffRequest.
     * @return The duration of this TimeOffRequest.
     */
    public double getDuration() {
        return duration;
    }

    /**
     * Sets the duration of this TimeOffRequest.
     * @param duration The duration to set.
     */
    public void setDuration(double duration) {
        this.duration = duration;
    }

    /**
     * Register an Observer to be notified when this TimeOffRequest changes.
     * @param observer The Observer to register.
     */
    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Remove an Observer from being notified when this TimeOffRequest changes.
     * @param observer The Observer to remove.
     */
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notify all registered Observers that this TimeOffRequest has changed.
     */
    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }

    /**
     * Method that's called when a TimeOffRequest has been updated. It then notifies all Observers.
     */
    public void requestUpdated() {
        notifyObservers();
    }

    /**
     * Gets the List of Managers who can approve this TimeOffRequest.
     * @return The List of Managers who can approve this TimeOffRequest.
     */
    public String getApprover() {
        return approver;
    }

    /**
     * Sets the List of Managers who can approve this TimeOffRequest and also adds them as Observers.
     * @param approvers The List of Managers to set.
     */
    public void setApprover(String approvers) {
        this.approver = approvers;
    }
}

