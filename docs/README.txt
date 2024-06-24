Guy Yehoshua 326529229
Erez Shiri 214509952

External Libraries used: JDBC for integrating sqlite DB



User Manual for the Employee Management System CLI
Overview
This document provides instructions on how to navigate and utilize the console-based user interface for the employee management system. This system allows administrative users and regular employees to manage shift schedules, employee roles, and other related tasks.
Getting Started
Launching the System
To start the system, run the Main class. Upon launch, you will be greeted with: Welcome to The SuperLi system!
Logging In
1. Choose a Branch: Initially, you will be prompted to log into a branch by entering a branch ID (0-8).
2. Employee Authentication: Enter your employee name and ID to authenticate. Depending on your role (HR manager or regular employee), you will have access to different functionalities.

 Using the System Common Actions for All Users
● Submit Availability for Shift: Enter the date and time to submit your availability for shifts.
● Remove Availability for Shift: Enter the date and time to remove your availability.
Administrative Actions
If you log in as an HR manager (admin), you will have additional options:
● Add/Remove Shift to/from Schedule: Manage the scheduling of shifts by adding or removing them.
● Add/Remove Employee to/from Shift: Assign or unassign employees to specific shifts.
● Manage Employee Roles and Details: Add new employees to the branch, assign roles,
set salaries, or terminate employment.
● Shift Reports: Generate reports showing employees assigned to shifts, required roles
for shifts, and available employees for shifts.
● System Configuration: Set the last date for submitting shift availabilities.

 Action Manual
1. Submit Availability for Shift
● Purpose: Allows an employee to declare their availability to work on a specific shift.
● Procedure:
1. Choose this option from the menu.
2. Enter the date of the shift (format: yyyy-mm-dd).
3. Specify the shift time (M for Morning or E for Evening).
● Feedback: The system will confirm the successful submission of your availability. 2. Remove Availability for Shift
● Purpose: Enables an employee to withdraw previously submitted availability for a specific shift.
● Procedure:
1. Select this action.
2. Input the date and time of the shift for which you want to remove your availability.
● Feedback: Confirmation of the removal of your availability.
3. Change password
● Purpose: Let the employee choose his own secure password
● Procedure:
1. Select this action
2. Input your old-password for verification & new-password
● Feedback: Confirmation for the password change
4. View My Shifts
● Purpose: Let the employee view his shift history & upcoming shifts
● Procedure:
3. Select this action
4. View shifts
● Feedback: shift history will be displayed

 Additional Administrative Actions (For HR Managers)
Administrative users have access to more complex functionalities, including managing shifts, employees, and system settings.
5. Add a Shift to Schedule
● Purpose: To schedule a new shift and define the required roles and number of employees needed.
● Procedure:
1. Enter the date and time for the new shift.
2. For each role, specify the number of employees required.
● Feedback: Confirmation that the shift has been added to the schedule. 6. Remove a Shift from Schedule
● Purpose: Allows removal of a previously scheduled shift.
● Procedure:
1. Input the date and time of the shift to be removed. ● Feedback: Confirmation of shift removal.
7. Add Employee to Shift
● Purpose: Assign an employee to a specific shift.
● Procedure:
1. Enter the shift's date and time.
2. Input the employee's ID and select their role for the shift.
● Feedback: Notification of successful assignment.
8. Remove Employee from Shift
● Purpose: De-assign an employee from a shift.
● Procedure:
1. Specify the shift's date and time.
2. Enter the employee's ID.
● Feedback: Confirmation of the employee's removal from the shift.

 9. Print Employees in Shift
● Purpose: Generate a list of all employees scheduled for a specific shift.
● Procedure:
1. Input the shift date and time.
● Feedback: Displays the list of assigned employees.
10. Print Required Employees for Shift
● Purpose: Show the number and roles of employees required for a particular shift.
● Procedure:
1. Select the shift date and time.
● Feedback: Outputs the requirements for the shift.
11. Print Available Employees for Shift
● Purpose: List all employees who have submitted their availability for a specific shift.
● Procedure:
1. Choose the shift date and time.
● Feedback: Displays the names and roles of available employees.
12. Add/Recruit New Employee to Branch
● Purpose: Hire and add a new employee to the system.
● Procedure:
1. Enter the employee’s name, ID, employment type, salary, and bank account ID. ● Feedback: Confirmation of the employee being added to the system.
13. Remove/Fire Employee from Branch
● Purpose: Terminate an employee's position within the branch.
● Procedure:
1. Provide the employee's ID.
● Feedback: Notification of employee termination.

 14. Assign New Role to Employee
● Purpose: Update or change an employee's role within the organization.
● Procedure:
1. Input the employee's ID and select the new role. ● Feedback: Confirmation of role assignment.
15. Set Last Date for Submitting Shifts
● Purpose: Establish a deadline for all employees to submit their availability.
● israelite:
1. Enter the final date for shift submission.
● Feedback: Acknowledgment that the date has been set.
16. Log Out (5 for non Hr Manager)
● Purpose: Safely end the user session.
● Procedure: Choose this option to log out.
● Feedback: Confirmation that you have been logged out.
17. Exit System (6 for non Hr Manager)
● Purpose: Completely shut down the system.
● Procedure: Select this to close the application.
● Feedback: "Exiting System, Goodbye!"

 Navigation
● Action Selection: After logging in, the system will display a list of available actions based on your role. Input the number corresponding to your desired action.
● Logging Out and Exiting: You can log out or exit the system by selecting the appropriate action from the menu.
Error Handling
The system provides feedback when actions fail, such as during login or when invalid inputs are entered. Follow the prompts to correct any issues.
e.g Failed to log in. Invalid employee ID.
Initialization
We Initialize the system with 9 Branches (0-8) & 9 corresponding HR managers.
For the i’th branch, The Managers username is “name{i}” , Id is i & default password is
i.
E.g for branch 5, the HR manager’s username is ‘branch5’ , Id is ‘5’ , & default
password is 5.
In addition to that we added some more employees, shifts, and availabilities, 
These are available to view in the instructions.pdf file.