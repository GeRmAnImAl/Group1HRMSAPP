# Group One

## Human Resource Management System

Once the application is running, visit ***localhost:8080*** in your browser. 

You will be prompted to log in. You can use the following credentials:

**User Name:** Admin

**Password:** Password123!

Once logged in you will be presented with the home page with a link to our first usecase.

Our first usecase is CRUD functionality for Employee Records.

You will be able to add, modify, and delete Employees. The data is stored in a database

hosted on AWS so there is no need to create an instance on your own machine.
# Use Case 1: Manage Employee Data
## User Account Creation
User accounts are dynamically created when a new employee is created. The convention used

to create the username and password for the new account is:

**User Name:** firstname.lastname
**Password:** Password + employee ID

However, upon their first login user's will be directed to change their password to meet the more

secure password standards. Non dynamically created passwords are required to contain at least

1 uppercase letter, 1 lower case letter, 1 numeric digit, and 1 special character. Passwords are also

encrypted before being stored in the database, and decrypted when retrieved.

### Note*
**Some Users have already had their passwords changed to meet new password standards,**

**if you are attempting to log in with a different user account and getting a bad credentials**

**error try using the password convention: "Password" + employee ID + "!"**

## User Persistence
The application will know always know what user is logged in and can retrieve the employee that

the user is associated with. This functionality is necessary for the time off request feature as well 

as features planned for the future.

# Use Case 2: Track Time and Attendance
## Time Off Request Functionality
Any user with an employee ID can create a time off request. Only the assigned manager for the

employee the time off request is assigned to can approve or reject the request. Only the employee

assigned to the time off request can cancel the time off request. Currently, all users can view all

created time off requests.

## Worked Hours Functionality
Users associated with employees (should be all users but Admin) can now submit their hours worked

for a given time frame. Only the assigned manager to the employee who submits the worked hours

can approve or reject them. Only the employee who created the worked hours can cancel them.

Currently, all users can view all submitted worked hours.

# Use Case 3: Manage Payroll and Benefits
## Benefits and Payroll Functionality
Employees can now select benefits which they wish to enroll into. HR can create, update, and delete benefits.

Benefits range from healthcare to special employee perks. Employees should keep in mind that benefits

cost money each paycheck. This is highlighted as part of the new payroll functionality. Managers can now pay

employees based on their worked hours input into the system. The system calculates payroll based on hours worked,

employee compensation amount, and enrolled benefits cost. We don't believe in taxes, so they have been omitted

until the IRS catches on!
# Use Case 4: Manage Training and Development
## Training and Development Functionality
Employees can now be trained! Users with the specialType HR can now create, update, and delete

training modules. Users with the specialType MANAGER can assign trainings to employees, and all

users can complete trainings. Trainings are informational and require users acknowledge that they read

and understand the training before they can mark it as completed. The system will track both assigned and

completed trainings for each employee.

# Implement Two Patterns
## AJ Germani
I chose to implement the Observer OOP Design Pattern using the Manager and TimeOffRequest

classes. I made changes to both of those classes as well as implemented two interfaces, Observer

and Subject.

I also chose to implement the Search Filters User Interface Pattern for the time_off_request_list.html.

Implementation of this pattern occurred in the TimeOffRequestController, TimeOffRequestRepository,

TimeOffRequestService, TimeOffRequestServiceImpl, and time_off_request_list.html.

## Nish Barot

Human Resource Management System - Update
This update introduces:

Observer OOP Design Pattern: Applied using Employee and Manager classes. Made changes in both classes and 

implemented two interfaces, Observer and Subject.

Comprehensive Employee Detail View: Displayed on employeeDetail.html, it shows detailed information about each 

employee, including name, email, job title, and more.

Search Filters User Interface Pattern: Implemented in the employee list view for better navigation and usability. 

Made necessary changes in EmployeeController, EmployeeRepository, EmployeeService, EmployeeServiceImpl, and employeeList.html.

The objective of these changes is to enhance the usability and extend the functionality of the Human Resource Management System.





## Ritvik Kothapalli

I chose to implement the Master/Detail pattern which is a User Interface Design Pattern. 

It involves presenting a list (the "master") of items and allowing the
user to select one to view additional information (the "detail"). 

It is a common pattern for many types of applications, including this HRMS system, as it provides a clean and efficient
method of displaying a large amount of data.

In this HRMS system, this pattern can be seen in use in each of the modules. For example, in the
"Employee Records" module, the master view is a list of employees, with each list
item showing basic information such as the employee's name and ID. Furthermore, this was implemented in HTML files such as index.html, new_employee.html, employee_detail.html

# Refactoring Implementations

## Lack of Secure Password Requirements
This issue has been rectafied in two ways. First, user created passwords are required to follow

strict guidelines such as to contain at least 1 uppercase letter, 1 lower case letter, 1 numeric 

digit, and 1 special character. Passwords are also  encrypted before being stored in the database, 

and decrypted when retrieved. Second the first time a user logs in to the HRMS with their auto-generated

password, they will be required to change it to meet the password standards.
## EmployeeRecordManager is a Redundant Class
This class has been replaced by the EmployeeServiceImpl class. Due to this, the EmployeeRecordManager

class has been deleted from the application. Any instances where the EmployeeRecordManager was used

has been refactored to use the EmployeeServiceImpl class.
## EmployeeRecord is a Deprecated Class
This class has been deprecated in favor of the Employee class. The Employee class is far more robust

in its implementation. Due to this the EmployeeRecord class has been deleted. Any instances where the

EmployeeRecord was used has been refactored to use the Employee class.
## Lack of Input Validation
Validation checks have been put into place to minimize the possibilities of errors cause by null or empty attributes.

These changes can be seen in the EmployeeController and TimeOffRequestController classes.
## Application Does Not Offer Self Service User Account Creation
The HRMS will now dynamically create a user account for an employee when their employee record

is created. This occurs in the saveEmployee method in the EmployeeServiceImpl class. The username

is created in the convention of firstname.lastname and simple password is created in the convention of

"Password" + employeeID. All relevant user fields are also populated at the time of its creation, such as

access level, and if first access (this is set to true by default). The first access flag works in conjunction with

the change password requirement outlined above.
## Manager & HRProfessional Objects Provide Minimal Use
These classes were found to offer minimal use and have been merged with the Employee class. Instead of having

different objects to denote these different employee types, it was decided that this could be accomplished by using

an identifier within the class. For this reason, the SpecialType Enum was created and establishes weather an

employee is of the EMPLOYEE, MANAGER, or HR type. This designation is reflected in the employee class as the

specialType attribute. The Manager and HRProfessional classes have been deleted. Anywhere they were in use has

been refactored to use the Employee class.
## Any User Can Approve or Reject a Time Off Request
Logic has been implemented to determine if a user can approve or reject a time off request in the

TimeOffRequestServiceImpl class. In the methods responsible for handling approving and rejecting time off requests

there are checks to determine three things:
- Is the logged in user associated with an existing employee?
- Is that employee a manager?
- Is that manager the manager of the employee associated with the time off request?

If all of these things are true then the user can approve, or reject the time off request.
## Any User Can Cancel a Time Off Request
Similarly to the approve and reject functionality for time off requests, logic has been implemented in the

TimeOffRequestServiceImp class to determine if a user can cancel an existing time off request. In the

cancelTimeOffRequest method there are checks to determine two things:
- Is the logged in user associated with an existing employee?
- Is that employee the employee associated with the time off request?

If these things are true, then the user can cancel the time off request. We only want the user who is associated

with the time off request to have the ability to cancel it. There is no other reason for a request to be canceled

when it can simply be rejected by a manager.
## Long Parameter List in Constructor
There are benefits to utilizing the builder pattern to construct complicated classes. However, we determined the flow

of the application sufficiently handles building objects through usage of the controller and service impl classes. Using

forms to accept user input and translating that to a Model has also enhanced this functionality. This can be reflected

in the EmployeeController, EmployeeServiceImpl, TimeOffRequestController, TimeOffRequestServiceImpl classes. It

is also reflected in the new_time_off_request, new_employee, update_employee, and change_password html files. 
## Exposing Internal Details
As we are now using password encryption there is no reason to remove the public getter for the password in the

AppUser class. The password retrieved with the getter is encrypted and requires the use of a

BCryptPasswordEncoder to be decrypted. The implementation of this can be found in the SecurityConfig and

UserServiceImpl classes.