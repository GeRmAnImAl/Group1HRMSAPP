package com.example.group1hrmsapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * The Manager class represents a specific type of Employee who manages other Employees.
 */
@Entity
@DiscriminatorValue(value = "MANAGER")
public class Manager extends Employee implements Serializable, Observer {
    @OneToMany(mappedBy = "manager")
    private Set<Employee> subordinates = new HashSet<>();

    /**
     * Retrieves the Employees that this Manager manages.
     * @return A Set of Employees that are managed by this Manager.
     */
    public Set<Employee> getSubordinates() {
        return subordinates;
    }

    /**
     * Sets the Employees that this Manager manages.
     * @param subordinates A Set of Employees that this Manager should manage.
     */
    public void setSubordinates(Set<Employee> subordinates) {
        this.subordinates = subordinates;
    }

    @Override
    public void update(TimeOffRequest timeOffRequest) {
        // Implement logic to notify manager about request
        System.out.println("Manager " + getId() + " notified about request: " + timeOffRequest.getId());
    }
}
