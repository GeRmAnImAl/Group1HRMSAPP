package com.example.group1hrmsapp.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "special_type", discriminatorType = DiscriminatorType.STRING)
@Table(name="employees")
public class Employee implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "firstname")
    private String firstName;
    @Column(name = "lastname")
    private String lastName;
    @Column(name = "dob")
    private LocalDate dateOfBirth;
    @Column(name = "gender")
    private String gender;
    @Column(name = "phone")
    private String phoneNumber;
    @Column(name = "email")
    private String email;
    @Column(name = "address")
    private String address;
    @Column(name = "title")
    private String jobTitle;
    @Column(name = "department")
    private String department;
    @Column(name = "empl_type")
    private String employmentType;
    @Column(name = "pay")
    private double hourlyRate;
    @Column(name = "vacation")
    private double vacationTime;
    @Column(name = "sick")
    private double sickTime;
    @Column(name = "pto")
    private double paidTimeOff;
    @Column(name = "enrolled")
    private boolean enrolledInBenefits;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinTable(name = "employee_manager",
            joinColumns = {@JoinColumn(name = "id")},
            inverseJoinColumns = {@JoinColumn(name = "lastname")})
    private Manager manager;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getVacationTime() {
        return vacationTime;
    }

    public void setVacationTime(double vacationTime) {
        this.vacationTime = vacationTime;
    }

    public double getSickTime() {
        return sickTime;
    }

    public void setSickTime(double sickTime) {
        this.sickTime = sickTime;
    }

    public double getPaidTimeOff() {
        return paidTimeOff;
    }

    public void setPaidTimeOff(double paidTimeOff) {
        this.paidTimeOff = paidTimeOff;
    }

    public boolean isEnrolledInBenefits() {
        return enrolledInBenefits;
    }

    public void setEnrolledInBenefits(boolean enrolledInBenefits) {
        this.enrolledInBenefits = enrolledInBenefits;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
}
