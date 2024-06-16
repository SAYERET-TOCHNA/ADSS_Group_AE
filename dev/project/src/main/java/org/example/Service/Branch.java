package org.example.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;

import org.example.Business.Employee;
import org.example.Business.Enums.ShiftTime;
import org.example.Business.Enums.Role;
import org.example.Business.Enums.EmploymentType;
import org.example.Utilities.Trio;

public class Branch {

    private static int NextBranchId = 0;

    private final int branchId;

    private ShiftService shiftManager;
    private EmployeeService empManager;

    private String HRManagerId;
    private ArrayList<String> loggedInUserIds;

// ------------------ construction -----------------------

    public Branch(Employee HRManager){
        branchId = NextBranchId;
        shiftManager = new ShiftService();
        empManager = new EmployeeService();
        empManager.addEmployee(HRManager);
        HRManagerId = HRManager.getId();
        loggedInUserIds = new ArrayList<String>();
        Branch.NextBranchId ++;
    }

// ------------------ methods ------------------------------

    /// add a new employee to the system
    public Employee addNewEmployee(String name, String eId, int branchId, EmploymentType employmentType, int salary, String bankAccountId, String requestingUserId)
    {
        if(!isEmployeeLoggedIn(requestingUserId))
            throw new IllegalArgumentException("requesting user " + requestingUserId + " is not logged in.");
        if(!requestingUserId.equals(this.HRManagerId))
            throw new IllegalArgumentException("user " + requestingUserId + " doesn't have permission to remove Employee from Shift");
        
        return empManager.addEmployee(name, eId, branchId, employmentType, salary, bankAccountId);
    }

    /// remove an employee from the system
    public Employee removeEmployee(String eId, String requestingUserId)
    {
        if(!isEmployeeLoggedIn(requestingUserId))
            throw new IllegalArgumentException("requesting user " + requestingUserId + " is not logged in.");
        if(!requestingUserId.equals(this.HRManagerId))
            throw new IllegalArgumentException("user " + requestingUserId + " doesn't have permission to remove Employee from Shift");
        
        return empManager.removeEmployee(eId);
    }

    public void setEmployeePassword(String eId, String oldPassword , String newPassword){
        if(!loggedInUserIds.contains(eId))
            throw new IllegalArgumentException("employee " + eId + " is not logged in");
        if(!empManager.getEmployee(eId).checkPassword(oldPassword))
            throw new IllegalArgumentException("old password is incorrect");
        empManager.setPassword(eId , newPassword);
    }

    /// log in an employee to the system
    public Employee logIn(String name , String eId, String password){
        Employee employee = empManager.getEmployee(eId); //might throw exception. which is ok.
        if (!employee.getName().equals(name))
            throw new IllegalArgumentException("credentials does not match a user in the system.");
        if (!employee.checkPassword(password))
            throw new IllegalArgumentException("incorrect password.");
        if (isEmployeeLoggedIn(eId))
            throw new IllegalArgumentException("employee " + eId + " is already logged in");
        this.loggedInUserIds.add(eId);   
        return employee; 
    }

    /// log out an employee from the system
    public Employee logOut(String eId)
    {
        Employee employee = empManager.getEmployee(eId); //might throw exception. which is ok.
        for( String id : loggedInUserIds )  
            if(id.equals(eId))
            {
                loggedInUserIds.remove(id);
                return employee;
            }
        throw new IllegalArgumentException("user is already logged out");
    }

    /// check if an employee is logged in to the system
    public boolean isEmployeeLoggedIn(String eId)
    {
        if(!empManager.hasEmlpoyee(eId))
            throw new IllegalArgumentException("failed to check if employee is logged in, employee " + eId + " is not in the system.");

        for( String id: loggedInUserIds ) 
            if(id.equals(eId))
                return true;
        return false;
    }

    /// add a new shift to the branch's schedule
    public void addShift(LocalDate date, ShiftTime time, EnumMap<Role,Integer> requiredEmployees, String requestingUserId)
    {
        if(!isEmployeeLoggedIn(requestingUserId))
            throw new IllegalArgumentException("requesting user " + requestingUserId + " is not logged in.");
        if(!requestingUserId.equals(this.HRManagerId))
            throw new IllegalArgumentException("user " + requestingUserId + " doesn't have permission to remove Employee from Shift");
        
        shiftManager.addShift(date, time, requiredEmployees); //might throw exception. which is ok.
    }

    /// remove a shift from the branch's schedule
    public void removeShift(LocalDate date, ShiftTime time, String requestingUserId)
    {
        if(!isEmployeeLoggedIn(requestingUserId))
            throw new IllegalArgumentException("requesting user " + requestingUserId + " is not logged in.");
        if(!requestingUserId.equals(this.HRManagerId))
            throw new IllegalArgumentException("user " + requestingUserId + " doesn't have permission to remove Employee from Shift");
        
        shiftManager.removeShift(date, time); //might throw exception. which is ok.
    }

    /// used by employee to submit availability for a shift
    public void submitAvailability(String eId, LocalDate date, ShiftTime time)
    {
        if(!empManager.hasEmlpoyee(eId))
            throw new IllegalArgumentException("failed to submit availability, employee " + eId + " is not in the system.");
        if(!isEmployeeLoggedIn(eId))
            throw new IllegalArgumentException("failed to submit shift, employee " + eId + " is not logged in.");

        shiftManager.addAvailability(eId, date, time);
    }

    /// used by employee to deSubmit availability for a shift
    public void deSubmitAvailability(String eId, LocalDate date, ShiftTime time)
    {
        if(!empManager.hasEmlpoyee(eId))
            throw new IllegalArgumentException("failed to submit availability, employee " + eId + " is not in the system.");
        if(!isEmployeeLoggedIn(eId))
            throw new IllegalArgumentException("failed to submit shift, employee " + eId + " is not logged in.");
            
        shiftManager.removeAvailability(eId, date, time);
    }

    /// add an employee to a shift
    public void addEmployeeToShift(String EmployeeId, LocalDate date, ShiftTime time, Role role, String requestingUserId)
    {
        if(!empManager.hasEmlpoyee(EmployeeId))
            throw new IllegalArgumentException("failed to submit availability, employee " + EmployeeId + " is not in the system.");
        if(!isEmployeeLoggedIn(requestingUserId))
            throw new IllegalArgumentException("requesting user " + requestingUserId + " is not logged in.");
        if(!requestingUserId.equals(this.HRManagerId))
            throw new IllegalArgumentException("user " + requestingUserId + " doesn't have permission to add Employee to Shift");

        if(!empManager.isSuitableForRole(EmployeeId , role))
            throw new IllegalArgumentException("employee " + EmployeeId + " is not suitable for role " + role.toString() + " in shift " + date.toString() + " " + time.toString());

        try {
            shiftManager.addEmployeeToShift(EmployeeId, date, time, role); //might throw exception. which is ok.
            empManager.addShift(EmployeeId , date, time, role);
        } catch (Exception e) {
            throw e;
        }
    }

    /// remove an employee from a shift
    public void removeEmployeeFromShift(String EmployeeId, LocalDate date, ShiftTime time, Role role, String requestingUserId)
    {
        if(!empManager.hasEmlpoyee(EmployeeId))
            throw new IllegalArgumentException("failed to submit availability, employee " + EmployeeId + " is not in the system.");
        if(!isEmployeeLoggedIn(requestingUserId))
            throw new IllegalArgumentException("requesting user " + requestingUserId + " is not logged in.");
        if(!requestingUserId.equals(this.HRManagerId))
            throw new IllegalArgumentException("user " + requestingUserId + " doesn't have permission to remove Employee from Shift");
        
        try {
            shiftManager.removeEmployeeFromShift(EmployeeId, date, time, role); //might throw exception. which is ok.
            empManager.removeShift(EmployeeId , date, time);
        } catch (Exception e) {
            throw e;
        }
        
    }

    /// assign new Role to employee
    public void assignRoleToEmployee(String EmployeeId, Role role, String requestingUserId)
    {
        if(!isEmployeeLoggedIn(requestingUserId))
            throw new IllegalArgumentException("requesting user " + requestingUserId + " is not logged in.");
        if(!requestingUserId.equals(this.HRManagerId))
            throw new IllegalArgumentException("user " + requestingUserId + " doesn't have permission to remove Employee from Shift");
        
        empManager.assignRoleToEmployee(EmployeeId, role); //might throw exception. which is ok.
    }

    /// set the last date for submitting availability for a shift
    public void setLastDateForSubmitting(LocalDate date, String requestingUserId)
    {
        if(!isEmployeeLoggedIn(requestingUserId))
            throw new IllegalArgumentException("requesting user " + requestingUserId + " is not logged in.");
        if(!requestingUserId.equals(this.HRManagerId))
            throw new IllegalArgumentException("user " + requestingUserId + " doesn't have permission to remove Employee from Shift");
        
        shiftManager.setLastDateForSubmitting(date); //might throw exception. which is ok.
    }


    /// get all employee ids for a shift
    public String getEmployeesForShiftStr(LocalDate date, ShiftTime time, String requestingUserId)
    {
        if(!isEmployeeLoggedIn(requestingUserId))
            throw new IllegalArgumentException("requesting user " + requestingUserId + " is not logged in.");
        if(!requestingUserId.equals(this.HRManagerId))
            throw new IllegalArgumentException("user " + requestingUserId + " doesn't have permission to view this information");

        StringBuilder str = new StringBuilder();
        EnumMap<Role , ArrayList<String>> employeesMap = shiftManager.getEmployeesForShift(date, time); //might throw exception. which is ok.
        for(Role role : employeesMap.keySet()){ 
            str.append(role.toString() + ": ");
            for(String eId : employeesMap.get(role)){
                str.append(eId + ", ");
            }
            str.append("\n");
        }
        return str.toString();
    }

    /// get all required employees for a shift
    public String getRequiredEmployeesForShiftStr(LocalDate date, ShiftTime time, String requestingUserId)
    {

        if(!isEmployeeLoggedIn(requestingUserId))
            throw new IllegalArgumentException("requesting user " + requestingUserId + " is not logged in.");
        if(!requestingUserId.equals(this.HRManagerId))
            throw new IllegalArgumentException("user " + requestingUserId + " doesn't have permission to view this information");

        StringBuilder str = new StringBuilder();
        EnumMap<Role , Integer> requiredEmployeesMap = shiftManager.getRequiredEmployeesForShift(date, time); //might throw exception. which is ok.
        for(Role role : requiredEmployeesMap.keySet()){ 
            str.append(role.toString() + ": " + requiredEmployeesMap.get(role) + "\n");
        }
        return str.toString();
    }
    
    /// get all available employees for a shift
    public String getAvailableEmployeesStr(LocalDate date, ShiftTime time, String requestingUserId)
    {

        if(!isEmployeeLoggedIn(requestingUserId))
            throw new IllegalArgumentException("requesting user " + requestingUserId + " is not logged in.");
        if(!requestingUserId.equals(this.HRManagerId))
            throw new IllegalArgumentException("user " + requestingUserId + " doesn't have permission to view this information");

        StringBuilder str = new StringBuilder();
        ArrayList<String> employeeList = shiftManager.getAvailableEmployeesForShift(date, time); //might throw exception. which is ok.
        for(String eId : employeeList){
            Employee employee = empManager.getEmployee(eId); //might throw exception.
            str.append(eId + ", " + employee.getName() + ", available Roles: "+employee.getRoles().toString()+ "\n");
        }
        return str.toString();
    }

    /// get all Shifts for an employee from a certain date
    public ArrayList<Trio<LocalDate,ShiftTime,Role>> getShiftsForEmployee(String eId)
    {
        if(!isEmployeeLoggedIn(eId))
            throw new IllegalArgumentException("user " + eId + " is not logged in.");
        return empManager.getShifts(eId);
    }


    
    

    

}
