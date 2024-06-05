package org.example;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.example.Business.Employee;
import org.example.Service.Branch;
import org.example.Business.Enums.EmploymentType;
import org.example.Business.Enums.Role;
import org.example.Business.Enums.ShiftTime;
import org.example.Utilities.SmartReader;


//TODO:: safe reader )))):::::: with prompt and error message and loop until valid input 
// replace all scanner with safe reader



public class Main {

    private static ArrayList<Employee> employees = new ArrayList<Employee>();
    private static HashMap<Integer , Branch> branches = new HashMap<Integer , Branch>();

    private static int loggedInBranchId = -1;
    private static String loggedInUserId = "";
    private static boolean isAdmin = false;

    private static boolean systemOn=true;

    public static void main(String[] args) {
        
        /// create 9 managers & branches
        for(int i = 0; i < 9; i++){
            Employee employee = Employee.createEmployee("name"+i, ""+i, i, EmploymentType.FULL_TIME, 25000, ""+i);
            employee.addRole(Role.HR_MANAGER);
            employees.add(employee);
            Branch branch = new Branch(employees.get(i));
            branches.put(i, branch);
        }

        System.out.println("Welcome to The SuperLi system!");
        while(systemOn){
            // log in
            try{logIn();} catch(Exception e){
                System.out.println("Failed to log in. "+e.getMessage());
                loggedInBranchId = -1;
                loggedInUserId = "";
            }
            
            while(loggedInBranchId != -1 && !loggedInUserId.equals("")){
                printActionMenu();
                try{
                int actionId = chooseAction();
                Act(actionId);
                } catch(Exception e){
                    System.out.println("Failed to perform action. "+e.getMessage());
                }
            }
        }
        
    }

    // ---------------------- General Methods ----------------------

    public static void logIn()
    {
        SmartReader reader = new SmartReader();
        // choose a branch
        while(true){
            System.out.print("Choose a branch to log in to (0-8): ");
            int branchId = reader.readInt();
            if(branchId < 0 || branchId > 8){
                System.out.println("Invalid branch id");
            }
            else{
                loggedInBranchId = branchId;
                System.out.println("Logged in to branch " + branchId);
                break;
            }
        }
            
        // choose an employee
        while(true){
            System.out.print("Enter employee name: ");
            String userName = reader.readString();
            System.out.print("Enter id: ");
            String userId = reader.readString();

            Employee e = branches.get(loggedInBranchId).logIn(userName, userId);
            loggedInUserId = e.getId();
            if(e.isSuitableForRole(Role.HR_MANAGER))
            {
                isAdmin = true;
                System.out.println("welcome Admin "+userName+"!");
                System.out.println("");
            }
            else
            {
                System.out.println("welcome "+userName+"!");
                System.out.println("");
                isAdmin = false;
            }
            break;    
        }
    }

    public static void printActionMenu(){
        System.out.println("Choose an action:");
        System.out.println("1. submit availability for shift");
        System.out.println("2. remove availability for shift");
        if(isAdmin){
            System.out.println("3. add a Shift to schduele");
            System.out.println("4. remove a Shift from schduele");
            System.out.println("5. add Employee to Shift");
            System.out.println("6. remove Employee from Shift");
            System.out.println("7. print employees in shift");
            System.out.println("8. print required employees for shift");
            System.out.println("9. print available employees for shift");
            System.out.println("10. add/recruit new Employee to Branch");
            System.out.println("11. remove/fire Employee from Branch");
            System.out.println("12. assign new Role to Employee");
            System.out.println("13. set last date for submitting shifts");
            System.out.println("14. log out");
            System.out.println("15. Exit system");
    
        }
        else{
            System.out.println("3. Log out");
            System.out.println("4. Exit system");
        }
    }

    public static int chooseAction()
    {
        int actionId = -1;
        SmartReader reader = new SmartReader();

        while(actionId==-1){
            System.out.print("your choice: ");
            actionId = reader.readInt();
            if(isAdmin && actionId >= 1 && actionId <= 15)
                break;
            else if(!isAdmin && actionId >= 1 && actionId <= 4)
                break;
            else{
                System.out.println("Invalid action choice.");
                actionId = -1;
            }
        }

        return actionId;    
    }
        
    

    public static void Act(int actionId)
    {
        if(isAdmin)
        {
            switch (actionId) {
                case 1:
                    submitAvailabilityForShift();
                    break;
                case 2:
                    removeAvailabilityForShift();
                    break;
                case 3:
                    addShiftToSchduele();
                    break;
                case 4:
                    removeShiftFromSchduele();
                    break;
                case 5:
                    addEmployeeToShift();
                    break;
                case 6:
                    removeEmployeeFromShift();
                    break;
                case 7:
                    printEmployeesInShift();
                    break;
                case 8:
                    printRequiredEmployeesForShift();
                    break;
                case 9: 
                    printAvailableEmployeesForShift();
                    break;
                case 10:
                    addNewEmployeeToBranch();
                    break;
                case 11:
                    removeEmployeeFromBranch();
                    break;
                case 12:  
                    assignNewRoleToEmployee();
                    break;  
                case 13:
                    setLastDateForSubmitting();
                    break;
                case 14:
                    logOut();
                    break;
                case 15:
                    logOut();
                    systemOn=false;
                    break;
                default:
                    System.out.println("something weird happened... Act() returned the value "+actionId+" and isAdmin is true");
                    break;
            }
        }
        else
        {
            switch (actionId) {
                case 1:
                    submitAvailabilityForShift();
                    break;
                case 2:
                    removeAvailabilityForShift();
                    break;
                case 3:
                    logOut();
                    break;
                case 4:
                    logOut();
                    systemOn=false;
                    System.out.println("Exiting System, Goodbye!");
                    break;
                default:
                    System.out.println("something weird happened... Act() returned the value "+actionId+" and isAdmin is false");
                    break;
            }

        }
            
    }

    


    // ---------------------- Actions ----------------------

    private static void submitAvailabilityForShift() {
        try
        {
            SmartReader reader = new SmartReader();
            System.out.println("Enter the date of the shift (yyyy-mm-dd): ");
            String dateStr = reader.readString();
            LocalDate date = LocalDate.parse(dateStr);

            System.out.println("Enter the time of the shift (M for MORNING/ E for EVENING): ");
            String timeStr = reader.readString();
            ShiftTime time;
            if(timeStr.equals("M") || timeStr.equals("m"))
                time = ShiftTime.MORNING;
            else if(timeStr.equals("E") || timeStr.equals("e"))
                time = ShiftTime.EVENING;
            else 
                throw new IllegalArgumentException("Failed to submit availability, Invalid time input.");
            
            branches.get(loggedInBranchId).submitAvailability(loggedInUserId, date, time);
            System.out.println("Availability to "+dateStr+" "+timeStr+" submitted successfully!");
            System.out.println("");
        }
        catch(Exception e)
        {
            System.out.println("Failed to submit availability for shift. "+e.getMessage());
        }
    }

    private static void removeAvailabilityForShift() {
        try
        {
            SmartReader reader = new SmartReader();
            System.out.println("Enter the date of the shift (yyyy-mm-dd): ");
            String dateStr = reader.readString();
            LocalDate date = LocalDate.parse(dateStr);

            System.out.println("Enter the time of the shift (M for MORNING/ E for EVENING): ");
            String timeStr = reader.readString();
            ShiftTime time;
            if(timeStr.equals("M") || timeStr.equals("m"))
                time = ShiftTime.MORNING;
            else if(timeStr.equals("E") || timeStr.equals("e"))
                time = ShiftTime.EVENING;
            else 
                throw new IllegalArgumentException("Failed to remove availability, Invalid time input.");
            
            branches.get(loggedInBranchId).deSubmitAvailability(loggedInUserId, date, time);
            System.out.println("Availability to "+dateStr+" "+timeStr+" removed successfully!");
            System.out.println("");
        }
        catch(Exception e)
        {
            System.out.println("Failed to remove availability for shift. "+e.getMessage());
        }
    }

    private static void addShiftToSchduele() {
        try
        {
            SmartReader reader = new SmartReader();
            System.out.println("Enter the date of the shift (yyyy-mm-dd): ");
            String dateStr = reader.readString();
            LocalDate date = LocalDate.parse(dateStr);

            System.out.println("Enter the time of the shift (M for MORNING/ E for EVENING): ");
            String timeStr = reader.readString();
            ShiftTime time;
            if(timeStr.equals("M") || timeStr.equals("m"))
                time = ShiftTime.MORNING;
            else if(timeStr.equals("E") || timeStr.equals("e"))
                time = ShiftTime.EVENING;
            else 
                throw new IllegalArgumentException("Failed to add shift to schduele, Invalid time input.");
            
            EnumMap<Role , Integer> requiredEmployees = new EnumMap<Role , Integer>(Role.class);
            for(Role role : Role.values())
            {
                System.out.println("Enter the number of "+role.toString()+"s required for this shift: ");
                int num = reader.readInt();
                requiredEmployees.put(role, num);
            }

            branches.get(loggedInBranchId).addShift(date, time, requiredEmployees, loggedInUserId);
            System.out.println("Shift "+dateStr+" "+timeStr+" added successfully!");
            System.out.println("");
        }
        catch(Exception e)
        {
            System.out.println("Failed to add shift to schduele. "+e.getMessage());
        }
    }

    private static void removeShiftFromSchduele() {
        try
        {
            SmartReader reader = new SmartReader();
            System.out.println("Enter the date of the shift (yyyy-mm-dd): ");
            String dateStr = reader.readString();
            LocalDate date = LocalDate.parse(dateStr);

            System.out.println("Enter the time of the shift (M for MORNING/ E for EVENING): ");
            String timeStr = reader.readString();
            ShiftTime time;
            if(timeStr.equals("M") || timeStr.equals("m"))
                time = ShiftTime.MORNING;
            else if(timeStr.equals("E") || timeStr.equals("e"))
                time = ShiftTime.EVENING;
            else 
                throw new IllegalArgumentException("Failed to remove shift from schedule, Invalid time input.");
            
            branches.get(loggedInBranchId).removeShift(date, time, loggedInUserId);
            System.out.println("Shift "+dateStr+" "+timeStr+" removed successfully!");
            System.out.println("");
        }
        catch(Exception e)
        {
            System.out.println("Failed to remove shift from schduele. "+e.getMessage());
        }
    }

    private static void addEmployeeToShift() {
        try
        {
            SmartReader reader = new SmartReader();
            // select date
            System.out.println("Enter the date of the shift (yyyy-mm-dd): ");
            String dateStr = reader.readString();
            LocalDate date = LocalDate.parse(dateStr);
            // select shift time
            System.out.println("Enter the time of the shift (M for MORNING/ E for EVENING): ");
            String timeStr = reader.readString();
            ShiftTime time;
            if(timeStr.equals("M") || timeStr.equals("m"))
                time = ShiftTime.MORNING;
            else if(timeStr.equals("E") || timeStr.equals("e"))
                time = ShiftTime.EVENING;
            else 
                throw new IllegalArgumentException("Failed to add employee to shift, Invalid time input.");
            // select employee
            System.out.println("Enter the id of the employee you want to add to the shift: ");
            String employeeId = reader.readString();
            //select role
            Role role = chooseRole();

            branches.get(loggedInBranchId).addEmployeeToShift(employeeId, date, time, role, loggedInUserId);
            System.out.println("Employee "+employeeId+" added to shift "+dateStr+" " +timeStr+ " successfully!");
            System.out.println("");
        }
        catch(Exception e)
        {
            System.out.println("Failed to add employee to shift. "+e.getMessage());
        }
    }

    private static void removeEmployeeFromShift() {
        try
        {
            SmartReader reader = new SmartReader();
            // select date
            System.out.println("Enter the date of the shift (yyyy-mm-dd): ");
            String dateStr = reader.readString();
            LocalDate date = LocalDate.parse(dateStr);
            // select shift time
            System.out.println("Enter the time of the shift (M for MORNING/ E for EVENING): ");
            String timeStr = reader.readString();
            ShiftTime time;
            if(timeStr.equals("M") || timeStr.equals("m"))
                time = ShiftTime.MORNING;
            else if(timeStr.equals("E") || timeStr.equals("e"))
                time = ShiftTime.EVENING;
            else 
                throw new IllegalArgumentException("Failed to remove employee from shift, Invalid time input.");
            // select employee
            System.out.println("Enter the id of the employee you want to add to the shift: ");
            String employeeId = reader.readString();
            //select role
            Role role = chooseRole();

            branches.get(loggedInBranchId).removeEmployeeFromShift(employeeId, date, time, role, loggedInUserId);
            System.out.println("Employee "+employeeId+" removed from shift "+dateStr+" " +timeStr+ " successfully!");
            System.out.println("");
        }
        catch(Exception e)
        {
            System.out.println("Failed to remove employee from shift. "+e.getMessage());
        }
    }

    private static void printEmployeesInShift() {
        try
        {
            SmartReader reader = new SmartReader();
            // select date
            System.out.println("Enter the date of the shift (yyyy-mm-dd): ");
            String dateStr = reader.readString();
            LocalDate date = LocalDate.parse(dateStr);
            // select shift time
            System.out.println("Enter the time of the shift (M for MORNING/ E for EVENING): ");
            String timeStr = reader.readString();
            ShiftTime time;
            if(timeStr.equals("M") || timeStr.equals("m"))
                time = ShiftTime.MORNING;
            else if(timeStr.equals("E") || timeStr.equals("e"))
                time = ShiftTime.EVENING;
            else 
                throw new IllegalArgumentException("Failed to print Employees in shift, Invalid time input.");
            
            System.out.println(branches.get(loggedInBranchId).getEmployeesForShiftStr(date, time, loggedInUserId));
        }
        catch(Exception e)
        {
            System.out.println("Failed to print employees in shift. "+e.getMessage());
        }
    }

    private static void printRequiredEmployeesForShift() {
        try
        {
            SmartReader reader = new SmartReader();
            // select date
            System.out.println("Enter the date of the shift (yyyy-mm-dd): ");
            String dateStr = reader.readString();
            LocalDate date = LocalDate.parse(dateStr);
            // select shift time
            System.out.println("Enter the time of the shift (M for MORNING/ E for EVENING): ");
            String timeStr = reader.readString();
            ShiftTime time;
            if(timeStr.equals("M") || timeStr.equals("m"))
                time = ShiftTime.MORNING;
            else if(timeStr.equals("E") || timeStr.equals("e"))
                time = ShiftTime.EVENING;
            else 
                throw new IllegalArgumentException("Failed to print required employees for shift, Invalid time input.");
            
            System.out.println(branches.get(loggedInBranchId).getRequiredEmployeesForShiftStr(date, time, loggedInUserId));
            
        }
        catch(Exception e)
        {
            System.out.println("Failed to print required employees for shift. "+e.getMessage());
        }
    }

    private static void printAvailableEmployeesForShift() {
        try
        {
            SmartReader reader = new SmartReader();
            // select date
            System.out.println("Enter the date of the shift (yyyy-mm-dd): ");
            String dateStr = reader.readString();
            LocalDate date = LocalDate.parse(dateStr);
            // select shift time
            System.out.println("Enter the time of the shift (M for MORNING/ E for EVENING): ");
            String timeStr = reader.readString();
            ShiftTime time;
            if(timeStr.equals("M") || timeStr.equals("m"))
                time = ShiftTime.MORNING;
            else if(timeStr.equals("E") || timeStr.equals("e"))
                time = ShiftTime.EVENING;
            else 
                throw new IllegalArgumentException("Failed to print available employees for shift, Invalid time input.");
            
            System.out.println(branches.get(loggedInBranchId).getAvailableEmployeesStr(date, time, loggedInUserId));
            
        }
        catch(Exception e)
        {
            System.out.println("Failed to print required employees for shift. "+e.getMessage());
        }
    }

    private static void addNewEmployeeToBranch() {
        try
        {
            SmartReader reader = new SmartReader();
            // enter name
            System.out.println("Enter the name of the new employee: ");
            String name = reader.readString();
            // enter id
            System.out.println("Enter the id of the new employee: ");
            String id = reader.readString();
            // choose Employment type
            EmploymentType employmentType = chooseEmploymentType();
            // enter salary
            System.out.println("Enter the salary of the new employee: ");
            int salary = reader.readInt();
            // enter bank account id
            System.out.println("Enter the bank account id of the new employee:");
            String bankAccountId = reader.readString();

            Employee employee = branches.get(loggedInBranchId).addNewEmployee(name, id, loggedInBranchId, employmentType, salary, bankAccountId, loggedInUserId);
            employees.add(employee);
            System.out.println("Employee "+employee.getName()+" added successfully!");
            System.out.println("");
        }
        catch(Exception e)
        {
            System.out.println("Failed to add new employee to branch. "+e.getMessage());
        }
    }

    private static void removeEmployeeFromBranch() {
        try{
            SmartReader reader = new SmartReader();
            System.out.println("Enter the id of the employee you want to remove from the branch: ");
            String employeeId = reader.readString();
            branches.get(loggedInBranchId).removeEmployee(employeeId, loggedInUserId);
            System.out.println("Employee "+employeeId+" removed successfully!");    
            System.out.println("");
        }
        catch(Exception e)
        {
            System.out.println("Failed to remove employee from branch. "+e.getMessage());
        }
    }

    private static void assignNewRoleToEmployee() {
        try
        {
            SmartReader reader = new SmartReader();
            // select employee
            System.out.println("Enter the id of the employee you want to assign a new role to: ");
            String employeeId = reader.readString();
            // select role
            Role role = chooseRole();
            branches.get(loggedInBranchId).assignRoleToEmployee(employeeId, role, loggedInUserId);
            System.out.println("Role "+role.toString()+" assigned to employee "+employeeId+" successfully!");
            System.out.println("");
        }
        catch(Exception e)
        {
            System.out.println("Failed to assign new role to employee. "+e.getMessage());
        }
    }

    private static void setLastDateForSubmitting() {
        try
        {
            SmartReader reader = new SmartReader();
            System.out.println("Enter the last date for submitting shifts (yyyy-mm-dd): ");
            String dateStr = reader.readString();
            LocalDate date = LocalDate.parse(dateStr);
            branches.get(loggedInBranchId).setLastDateForSubmitting(date, loggedInUserId);
            System.out.println("Last date for submitting shifts set to "+dateStr+" successfully!");
            System.out.println("");
        }
        catch(Exception e)
        {
            System.out.println("Failed to set last date for submitting shifts. "+e.getMessage());
        }
    }

    private static void logOut() {
        branches.get(loggedInBranchId).logOut(loggedInUserId);
        loggedInBranchId = -1;
        loggedInUserId = "";
        isAdmin = false;
        System.out.println("Logged out successfully!");
    }

    // -------------- Utility methods --------------

    public static Role chooseRole()
    {
        SmartReader reader = new SmartReader();
        Role role = null;
        while(role == null){
            System.out.println("Choose a role:");
            for(Role r : Role.values()){
                System.out.println(r.ordinal() + ". " + r.toString());
            }
            System.out.print("your choice: ");
            int roleId = reader.readInt();
            if(roleId >= 0 && roleId < Role.values().length){
                role = Role.values()[roleId];
            }
            else{
                System.out.println("Invalid role choice");
            }
        }
        return role;
    }

    public static EmploymentType chooseEmploymentType()
    {
        SmartReader reader = new SmartReader();
        EmploymentType employmentType = null;
        while(employmentType == null){
            System.out.println("Choose an employment type:");
            for(EmploymentType e : EmploymentType.values()){
                System.out.println(e.ordinal() + ". " + e.toString());
            }
            System.out.print("your choice: ");
            int employmentTypeId = reader.readInt();
            if(employmentTypeId >= 0 && employmentTypeId < EmploymentType.values().length){
                employmentType = EmploymentType.values()[employmentTypeId];
            }
            else{
                System.out.println("Invalid employment type choice");
            }
        }
        return employmentType;
    }



}