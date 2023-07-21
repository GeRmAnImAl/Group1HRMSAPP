package com.example.group1hrmsapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue(value = "MANAGER")
public class Manager extends Employee implements Serializable, Observer {
    @OneToMany(mappedBy = "manager")
    private Set<Employee> subordinates = new HashSet<>();

    public Set<Employee> getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(Set<Employee> subordinates) {
        this.subordinates = subordinates;
    }

    @Override
    public void update(TimeOffRequest timeOffRequest) {
        // Implement logic to notify manager about request
        System.out.println("Manager " + getId() + " notified about request: " + timeOffRequest.getId());
    }
}
