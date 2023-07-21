package com.example.group1hrmsapp.model;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@DiscriminatorValue(value = "HR")
public class HRProfessional extends Employee implements Serializable {
    @Column(name = "access_level")
    @Enumerated(EnumType.STRING)
    private AccessLevel accessLevel;

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }
}

