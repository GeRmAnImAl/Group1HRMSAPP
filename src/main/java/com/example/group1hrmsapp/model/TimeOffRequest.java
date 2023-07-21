package com.example.group1hrmsapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "time_off_requests")
public class TimeOffRequest implements Serializable {
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
    @ManyToMany
    @JoinTable(name = "TimeOffRequest_Approver",
            joinColumns = @JoinColumn(name = "time_off_request_id"),
            inverseJoinColumns = @JoinColumn(name = "approver_id"))
    private List<Manager> approvers;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public TimeOffType getTimeOffType() {
        return timeOffType;
    }

    public void setTimeOffType(TimeOffType timeOffType) {
        this.timeOffType = timeOffType;
    }

    public TimeOffStatus getTimeOffStatus() {
        return timeOffStatus;
    }

    public void setTimeOffStatus(TimeOffStatus timeOffStatus) {
        this.timeOffStatus = timeOffStatus;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public List<Manager> getApprovers() {
        return approvers;
    }

    public void setApprovers(List<Manager> approvers) {
        this.approvers = approvers;
    }
}

