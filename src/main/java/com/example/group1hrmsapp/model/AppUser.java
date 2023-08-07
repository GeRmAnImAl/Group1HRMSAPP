package com.example.group1hrmsapp.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents an application user with credentials and associated employee details.
 */
@Entity
@Table(name = "users")
public class AppUser implements Serializable {
    @Id
    private String userName;
    @Column(name = "password")
    private String password;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "access_level")
    private AccessLevel accessLevel;
    @Column(name = "first_access")
    private boolean firstTimeLogin;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;

    /**
     * Sets the username of the application user.
     *
     * @param userName the desired username for the user.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Retrieves the username of the application user.
     *
     * @return the current username.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Retrieves the password of the application user.
     *
     * @return the current password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Updates the password for the application user.
     *
     * @param password the new password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Checks if it's the user's first time logging in.
     *
     * @return true if it's the user's first login, otherwise false.
     */
    public boolean isFirstTimeLogin() {
        return firstTimeLogin;
    }

    /**
     * Sets the flag to indicate whether it's the user's first time logging in.
     *
     * @param firstTimeLogin the first-time login flag value.
     */
    public void setFirstTimeLogin(boolean firstTimeLogin) {
        this.firstTimeLogin = firstTimeLogin;
    }

    /**
     * Retrieves the employee associated with the application user.
     *
     * @return the associated employee object.
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Sets the employee associated with the application user.
     *
     * @param employee the employee object to associate.
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * Retrieves the access level of the application user.
     *
     * @return the current access level.
     */
    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    /**
     * Sets the access level for the application user.
     *
     * @param accessLevel the desired access level for the user.
     */
    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    /**
     * Provides a string representation of the application user.
     *
     * @return a string detailing the username and associated employee details.
     */
    @Override
    public String toString(){
        return "UserName: " + this.userName + " | Associated Employee: " + this.employee.getFullName() + "  | ID: " + this.employee.getId();
    }
}
