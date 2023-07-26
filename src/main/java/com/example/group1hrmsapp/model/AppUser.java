package com.example.group1hrmsapp.model;

import jdk.jfr.BooleanFlag;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;

import java.io.Serializable;

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
    @BooleanFlag
    @Column(name = "first_access")
    private boolean firstTimeLogin;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isFirstTimeLogin() {
        return firstTimeLogin;
    }

    public void setFirstTimeLogin(boolean firstTimeLogin) {
        this.firstTimeLogin = firstTimeLogin;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }


    @Override
    public String toString(){
        return "UserName: " + this.userName + " | Associated Employee: " + this.employee.getFullName() + "  | ID: " + this.employee.getId();
    }
}
