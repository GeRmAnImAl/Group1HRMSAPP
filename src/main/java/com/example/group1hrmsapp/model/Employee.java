package com.example.group1hrmsapp.model;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * The Employee class represents an employee in the HRMS application.
 */
@Entity
@Table(name="employees")
public class Employee implements Serializable, Observer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;
    @Enumerated(EnumType.STRING)
    @Column(name = "special_type")
    private SpecialType specialType;
    @Enumerated(EnumType.STRING)
    @Column(name = "access_level")
    private AccessLevel accessLevel;
    @OneToMany(mappedBy = "manager")
    private Set<Employee> subordinates = new HashSet<>();
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private AppUser appUser;
    @ElementCollection
    @CollectionTable(name = "employee_benefits", joinColumns = @JoinColumn(name = "employee_id"))
    @MapKeyColumn(name = "benefit_id")
    @Column(name = "benefit_date")
    private Map<Benefit, LocalDate> benefitList = new HashMap<>();
    @ElementCollection
    @CollectionTable(name = "assigned_trainings", joinColumns = @JoinColumn(name = "employee_id"))
    @MapKeyJoinColumn(name = "training_id")
    @Column(name = "assigned")
    private Map<TrainingModule, Boolean> assignedTrainings = new HashMap<>();


    /**
     * Retrieves the ID of this Employee.
     * @return A long representing the ID of this Employee.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of this Employee.
     * @param id A long containing the ID to set for this Employee.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retrieves the user linked to this Employee.
     * @return The AppUser linked to this Employee.
     */
    public AppUser getAppUser() {
        return appUser;
    }

    /**
     * Sets the AppUser for this Employee.
     * @param appUser The AppUser to set for this Employee.
     */
    public void setAppUser(AppUser appUser) {
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
    public Employee getManager() {
        return manager;
    }

    /**
     * Sets the Manager for this Employee.
     * @param manager The Manager to set for this Employee.
     */
    public void setManager(Employee manager) {
        this.manager = manager;
    }

    /**
     * Retrieves the full name of this Employee.
     * @return A String representing the full name of this Employee.
     */
    public String getFullName(){
        return firstName + " " + lastName;
    }

    /**
     * Retrieves the special type of this Employee.
     * @return A String representing the special type of this Employee (e.g., "EMPLOYEE", "MANAGER", "HR").
     */
    public SpecialType getSpecialType() {
        return this.specialType;
    }

    /**
     * Sets the special type for this Employee.
     * @param specialType The special type to set for this Employee.
     */
    public void setSpecialType(SpecialType specialType) {
        this.specialType = specialType;
    }


    /**
     * Retrieves the access level of this HR professional.
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

    public Map<Benefit, LocalDate> getBenefitList() {
        return benefitList;
    }

    public void setBenefitList(Map<Benefit, LocalDate> benefitList) {
        this.benefitList = benefitList;
    }

    /**
     * This method is implemented from the Observer interface to receive updates about time-off requests.
     * @param timeOffRequest The time-off request to notify the manager about.
     */
    @Override
    public void update(TimeOffRequest timeOffRequest) {
        // Implement logic to notify manager about request
        System.out.println("Manager " + getId() + " notified about request: " + timeOffRequest.getId());
    }

    public void addSubordinate(Employee employee) {
        this.subordinates.add(employee);
        employee.setManager(this);
    }

    public void removeSubordinate(Employee employee){
        this.subordinates.remove(employee);
    }

    /**
     * Retrieves the list of completed training modules for this Employee.
     * @return A Map containing TrainingModule objects and their completion status. If the value is true,
     * the training module has been completed by the Employee, otherwise it has not been completed.
     */
    public Map<TrainingModule, Boolean> getAssignedTrainings() {
        return assignedTrainings;
    }

    /**
     * Sets the list of completed training modules for this Employee.
     * @param completedTrainings A Map containing TrainingModule objects and their completion status to set for the Employee.
     */
    public void setAssignedTrainings(Map<TrainingModule, Boolean> completedTrainings) {
        this.assignedTrainings = completedTrainings;
    }

    /**
     * Updates the completion status of a specific training module for this Employee.
     * @param trainingModule The TrainingModule whose status should be updated.
     * @param status The new completion status for the TrainingModule.
     */
    public void updateTrainingStatus(TrainingModule trainingModule, Boolean status) {
        this.assignedTrainings.put(trainingModule, status);
    }

    /**
     * Retrieves the completion status of a specific training module for this Employee.
     * @param trainingModule The TrainingModule whose status should be retrieved.
     * @return The completion status of the specified TrainingModule. If the TrainingModule has not been added to
     * the Employee's completedTrainings Map, it will return false by default.
     */
    public Boolean getTrainingStatus(TrainingModule trainingModule) {
        return this.assignedTrainings.getOrDefault(trainingModule, false);
    }

    /**
     * Provides a String representation of the Employee object,
     * detailing the employee's full name, ID, and associated username.
     * @return A String detailing the employee's full name, ID, and associated username.
     */
    @Override
    public String toString(){
        return "Employee Name: " + this.getFullName() + " | Employee ID: " + this.getId() + " | Associated AppUser: " + this.getAppUser().getUserName();
    }

}
