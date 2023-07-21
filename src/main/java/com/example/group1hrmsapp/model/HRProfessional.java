package com.example.group1hrmsapp.model;

import javax.persistence.*;

import java.io.Serializable;

/**
 * Represents a HRProfessional as an extension of the Employee class with an added access level attribute.
 */
@Entity
@DiscriminatorValue(value = "HR")
public class HRProfessional extends Employee implements Serializable {
    @Column(name = "access_level")
    @Enumerated(value = EnumType.STRING)
    private AccessLevel accessLevel;

    /**
     * Gets the access level of this HR professional.
     * @return The access level of this HR professional.
     */
    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    /**
     * Sets the access level of this HR professional.
     * @param accessLevel The access level to be set.
     */
    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }
}

