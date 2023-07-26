# Group One

## Human Resource Management System

Once the application is running, visit ***localhost:8080*** in your browser. 

You will be prompted to log in. You can use the following credentials:

**User Name:** Admin

**Password:** Password123!

Once logged in you will be presented with the home page with a link to our first usecase.

Our first usecase is CRUD functionality for Employee Recrods.

You will be able to add, modify, and delete Employees. The data is stored in a database

hosted on AWS so there is no need to create an instance on your own machine.

## User Account Creation
User accounts are dynamically created when a new employee is created. The convention used

to create the username and password for the new account is:

**User Name:** firstname.lastname
**Password:** Password + employee ID

However, upon their first login user's will be directed to change their password to meet the more

secure password standards. Non dynamically created passwords are required to contain at least

1 uppercase letter, 1 lower case letter, 1 numeric digit, and 1 special character. Passwords are also

encrypted before being stored in the database, and decrypted when retrieved.

## User Persistance
The application will know always know what user is logged in and can retrieve the employee that

the user is associated with. This functionality is necessary for the time off request feature as well 

as features planned for the future.

## Time Off Request Functionality
Any user with an employee ID can create a time off request. Only the assigned manager for the

employee the time off request is assigned to can approve or reject the request. Only the employee

assigned to the time off request can cancel the time off request. Currently, all users can view all

created time off requests.

# Implement Two Patterns
## AJ Germani
I chose to implement the Observer OOP Design Pattern using the Manager and TimeOffRequest

classes. I made changes to both of those classes as well as implemented two interfaces, Observer

and Subject.

I also chose to implement the Search Filters User Interface Pattern for the time_off_request_list.html.

Implementation of this pattern occured in the TimeOffRequestController, TimeOffRequestRepository,

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
