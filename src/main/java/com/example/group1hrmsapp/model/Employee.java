package com.example.group1hrmsapp.model;

import javax.persistence.*;

import java.io.Serializable;

/**
 * The Employee class represents an employee in the HRMS application.
 */
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
    private String dateOfBirth;
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
    @OneToOne(mappedBy = "employee")
    private AppUser appUser;

    /**
     * Retrieves the ID of this Employee.
     * @return A long representing the ID of this Employee.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the ID of this Employee.
     * @param id A long containing the ID to set for this Employee.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Retrieves the user linked to this Employee.
     * @return The AppUser linked to this Employee.
     */
    public AppUser getUser() {
        return appUser;
    }

    /**
     * Sets the AppUser for this Employee.
     * @param appUser The AppUser to set for this Employee.
     */
    public void setUser(AppUser appUser) {
        this.appUser = appUser;
    }

    /**
     * Retrieves the first name of this Employee.
     * @return A String representing the first name of this Employee.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name for this Employee.
     * @param firstName The first name to set for this Employee.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Retrieves the last name of this Employee.
     * @return A String representing the last name of this Employee.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name for this Employee.
     * @param lastName The last name to set for this Employee.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Retrieves the date of birth of this Employee.
     * @return A String representing the date of birth of this Employee.
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the date of birth for this Employee.
     * @param dateOfBirth The date of birth to set for this Employee.
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Retrieves the gender of this Employee.
     * @return A String representing the gender of this Employee.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender for this Employee.
     * @param gender The gender to set for this Employee.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Retrieves the phone number of this Employee.
     * @return A String representing the phone number of this Employee.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number for this Employee.
     * @param phoneNumber The phone number to set for this Employee.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Retrieves the email address of this Employee.
     * @return A String representing the email address of this Employee.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address for this Employee.
     * @param email The email address to set for this Employee.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retrieves the address of this Employee.
     * @return A String representing the address of this Employee.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address for this Employee.
     * @param address The address to set for this Employee.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Retrieves the job title of this Employee.
     * @return A String representing the job title of this Employee.
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * Sets the job title for this Employee.
     * @param jobTitle The job title to set for this Employee.
     */
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    /**
     * Retrieves the department of this Employee.
     * @return A String representing the department of this Employee.
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the department for this Employee.
     * @param department The department to set for this Employee.
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Retrieves the employment type of this Employee.
     * @return A String representing the employment type of this Employee.
     */
    public String getEmploymentType() {
        return employmentType;
    }

    /**
     * Sets the employment type for this Employee.
     * @param employmentType The employment type to set for this Employee.
     */
    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    /**
     * Retrieves the hourly rate of this Employee.
     * @return A double representing the hourly rate of this Employee.
     */
    public double getHourlyRate() {
        return hourlyRate;
    }

    /**
     * Sets the hourly rate for this Employee.
     * @param hourlyRate The hourly rate to set for this Employee.
     */
    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    /**
     * Retrieves the vacation time of this Employee.
     * @return A double representing the vacation time of this Employee.
     */
    public double getVacationTime() {
        return vacationTime;
    }

    /**
     * Sets the vacation time for this Employee.
     * @param vacationTime The vacation time to set for this Employee.
     */
    public void setVacationTime(double vacationTime) {
        this.vacationTime = vacationTime;
    }

    /**
     * Retrieves the sick time of this Employee.
     * @return A double representing the sick time of this Employee.
     */
    public double getSickTime() {
        return sickTime;
    }

    /**
     * Sets the sick time for this Employee.
     * @param sickTime The sick time to set for this Employee.
     */
    public void setSickTime(double sickTime) {
        this.sickTime = sickTime;
    }

    /**
     * Retrieves the paid time off of this Employee.
     * @return A double representing the paid time off of this Employee.
     */
    public double getPaidTimeOff() {
        return paidTimeOff;
    }

    /**
     * Sets the paid time off for this Employee.
     * @param paidTimeOff The paid time off to set for this Employee.
     */
    public void setPaidTimeOff(double paidTimeOff) {
        this.paidTimeOff = paidTimeOff;
    }

    /**
     * Retrieves whether this Employee is enrolled in benefits.
     * @return A boolean representing whether this Employee is enrolled in benefits.
     */
    public boolean isEnrolledInBenefits() {
        return enrolledInBenefits;
    }

    /**
     * Sets whether this Employee is enrolled in benefits.
     * @param enrolledInBenefits A boolean representing whether to enroll this Employee in benefits.
     */
    public void setEnrolledInBenefits(boolean enrolledInBenefits) {
        this.enrolledInBenefits = enrolledInBenefits;
    }

    /**
     * Retrieves the Manager of this Employee.
     * @return The Manager of this Employee.
     */
    public Manager getManager() {
        return manager;
    }

    /**
     * Sets the Manager for this Employee.
     * @param manager The Manager to set for this Employee.
     */
    public void setManager(Manager manager) {
        this.manager = manager;
    }

    /**
     * Retrieves the full name of this Employee.
     * @return A String representing the full name of this Employee.
     */
    public String getFullName(){
        return firstName + " " + lastName;
    }
}
