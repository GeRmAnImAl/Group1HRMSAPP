# Group One

## Human Resource Management System

Once the application is running, visit ***localhost:8080*** in your browser. 

You will be prompted to log in. You can use the following credentials:

**User Name:** TestUser

**Password:** Password123

Once logged in you will be presented with the home page with a link to our first usecase.

Our first usecase is CRUD functionality for Employee Recrods.

You will be able to add, modify, and delete Employees. The data is stored in a database

hosted on AWS so there is no need to create an instance on your own machine.

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
