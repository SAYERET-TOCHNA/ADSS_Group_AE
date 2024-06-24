package org.example.DataAccess;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.concurrent.ConcurrentHashMap;

import org.example.Business.Branch;
import org.example.Business.Employee;
import org.example.Business.Enums.EmploymentType;
import org.example.Business.Enums.Role;
import org.example.Business.Enums.ShiftTime;
import org.example.Business.Shift;
import org.example.Utilities.Trio;

/// Singleton class that handles all database operations
/// Concurrency safe by Lock Synchronization
public class DBObj {


    private static final String DB_NAME = "superli.db";

    private static DBObj instance;

    private Object employeeTableLock;
    private Object employeeToRoleTableLock;
    private Object shiftsTableLock;
    private Object shiftAvailabilityTableLock;
    private Object shiftToEmployeeTableLock;
    private Object shiftRoleToRequiredTableLock;
    private Object branchesTableLock;


            
    private DBObj(){
        this.employeeTableLock = new Object();
        this.employeeToRoleTableLock = new Object();
        this.shiftsTableLock = new Object();
        this.shiftAvailabilityTableLock = new Object();
        this.shiftToEmployeeTableLock = new Object();
        this.shiftRoleToRequiredTableLock = new Object();
        this.branchesTableLock = new Object();
    }

    public static DBObj getInstance(){
        if(instance == null){
            instance = new DBObj();
        }
        return instance;

    }
// ---------------------------- Connection / Creation ----------------------------
    
    

// ---------------------------- CRUD Methods ----------------------------



// --------- Create

    /// submit availability to shift
    public void submitAvailability(String employeeId, LocalDate shiftDate, ShiftTime shiftTime, int branchId){
        String url = "jdbc:sqlite:" + DB_NAME;
        String query = "INSERT INTO SHIFT_AVAILABILITY (employee_id, shift_date, shift_time, branch_id) VALUES (?, ?, ?, ?)";
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn != null) {
                pstmt.setString(1, employeeId);
                pstmt.setString(2, shiftDate.toString());
                pstmt.setInt(3, shiftTime.ordinal());
                pstmt.setInt(4, branchId);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to submit availability to DB: " + e.getMessage());
        }
    }

    /// add shift to db
    public void addShift(LocalDate shiftDate, ShiftTime shiftTime, boolean hasDelivery, EnumMap<Role,Integer> requiredEmployees, int branchId){
        String url = "jdbc:sqlite:" + DB_NAME;
        String queryShift = "INSERT INTO SHIFTS (shift_date, shift_time, branch_id, has_delivery) VALUES (?, ?, ?, ?)";
        String queryShiftRoleToRequired = "INSERT INTO SHIFT_ROLE_TO_REQUIRED (shift_date, shift_time, branch_id, role, required) VALUES (?, ?, ?, ?, ?)";
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmtShift = conn.prepareStatement(queryShift);
            PreparedStatement pstmtShiftRoleToRequired = conn.prepareStatement(queryShiftRoleToRequired);) {
            if (conn != null) {
                // add shift to shifts table
                pstmtShift.setString(1, shiftDate.toString());
                pstmtShift.setInt(2, shiftTime.ordinal());
                pstmtShift.setInt(3, branchId);
                pstmtShift.setInt(4, hasDelivery ? 1 : 0);
                pstmtShift.executeUpdate();
                
                // add required employees for each role
                for(Role role : requiredEmployees.keySet())
                {
                    pstmtShiftRoleToRequired.setString(1, shiftDate.toString());
                    pstmtShiftRoleToRequired.setInt(2, shiftTime.ordinal());
                    pstmtShiftRoleToRequired.setInt(3, branchId);
                    pstmtShiftRoleToRequired.setInt(4, role.ordinal());
                    pstmtShiftRoleToRequired.setInt(5, requiredEmployees.get(role));
                    pstmtShiftRoleToRequired.executeUpdate();
                    
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to add shift to DB: " + e.getMessage());
        }
    }

    /// assign employee to shift
    public void assignEmployeeToShift(String employeeId, LocalDate shiftDate, ShiftTime shiftTime, Role role, int branchId){ //v
        String url = "jdbc:sqlite:" + DB_NAME;
        String query1 = "INSERT INTO SHIFT_TO_EMPLOYEE (employee_id, shift_date, shift_time, branch_id, role) VALUES (?, ?, ?, ?, ?)";
        String query2 = "UPDATE SHIFT_ROLE_TO_REQUIRED SET required = required - 1 WHERE shift_date = ? AND shift_time = ? AND branch_id = ? AND role = ?";
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt1 = conn.prepareStatement(query1);
            PreparedStatement pstmt2 = conn.prepareStatement(query2);) {
            if (conn != null) {
                pstmt1.setString(1, employeeId);
                pstmt1.setString(2, shiftDate.toString());
                pstmt1.setInt(3, shiftTime.ordinal());
                pstmt1.setInt(4, branchId);
                pstmt1.setInt(5, role.ordinal());
                pstmt1.executeUpdate();
                //System.out.println("LOG: Employee "+employeeId+" assigned to shift "+shiftDate+" "+shiftTime+" as "+role.toString());
                pstmt2.setString(1, shiftDate.toString());
                pstmt2.setInt(2, shiftTime.ordinal());
                pstmt2.setInt(3, branchId);
                pstmt2.setInt(4, role.ordinal());
                pstmt2.executeUpdate();
                //System.out.println("LOG: Required employees for shift "+shiftDate+" "+shiftTime+" as "+role.toString()+" updated.");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to assign employee to shift: " + e.getMessage());
        }
    }

    /// register new employee to db
    public void addEmployee(String id, String name, String password, int employmentType, int salary, String bankAccountId, int branchId , LocalDate startDate) {

        String url = "jdbc:sqlite:superli.db";
        String query = "INSERT INTO EMPLOYEES (id, name, password, employment_type, salary, bank_account_id, branch_id, startDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
        try (Connection conn = DriverManager.getConnection(url);
            //using Prepared Statement to prevent SQL Injection
            //(pre-compiles the sql statement)
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn != null) {
                pstmt.setString(1, id);
                pstmt.setString(2, name);
                pstmt.setString(3, password);
                pstmt.setInt(4, employmentType);
                pstmt.setInt(5, salary);
                pstmt.setString(6, bankAccountId);
                pstmt.setInt(7, branchId);
                pstmt.setString(8, startDate.toString());
                pstmt.executeUpdate();
                //System.out.println("LOG: Employee "+id+" added successfully.");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to add employee to DB: " + e.getMessage());
        }

    }

    /// assign new role to employee
    public void assignNewRoleToEmployee(String id, Role role){ //v
        String url = "jdbc:sqlite:" + DB_NAME;
        String query = "INSERT INTO EMPLOYEE_TO_ROLE (employee_id, role) VALUES (?, ?)";
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn != null) {
                pstmt.setString(1, id);
                pstmt.setInt(2, role.ordinal());
                pstmt.executeUpdate();
                //System.out.println("LOG: Employee "+id+" assigned new role "+role.toString());
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to assign new role to employee: " + e.getMessage());
        }
    }




// --------- Read

    public ArrayList<Integer> loadBranchIds(){
        String url = "jdbc:sqlite:" + DB_NAME;
        String query = "SELECT id FROM BRANCHES";
        ArrayList<Integer> branchIds = new ArrayList<Integer>();
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn != null) {
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    branchIds.add(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to load branch data: " + e.getMessage());
        }
        return branchIds;
            
    }

    public String loadHRMId(int branchId){
        String url = "jdbc:sqlite:" + DB_NAME;
        String query = "SELECT hr_manager_id FROM BRANCHES WHERE id = ?";
        String hrManagerId = "";
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn != null) {
                pstmt.setInt(1, branchId);
                ResultSet rs = pstmt.executeQuery();
                hrManagerId = rs.getString("hr_manager_id");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to load HRM data: " + e.getMessage());
        }
        return hrManagerId;
    }

    
    public ArrayList<Employee> loadEmployees(int branchId){
        String url = "jdbc:sqlite:" + DB_NAME;
        String query = "SELECT id FROM EMPLOYEES WHERE branch_id = ?";
        ArrayList<Employee> employees = new ArrayList<Employee>();
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn != null) {
                pstmt.setInt(1, branchId);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    employees.add(this.loadEmployee(rs.getString("id")));
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to load employee data: " + e.getMessage());
        }
        return employees;
    }

    public Employee loadEmployee(String id){
        String url = "jdbc:sqlite:" + DB_NAME;
        String query = "SELECT * FROM EMPLOYEES WHERE id = ?";
        Employee employee = null;
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn != null) {
                pstmt.setString(1, id);
                ResultSet rs = pstmt.executeQuery();
                employee = Employee.loadEmployee(rs.getString("name"), 
                                                rs.getString("id"), 
                                                rs.getInt("branch_id"), 
                                                EmploymentType.values()[rs.getInt("employment_type")] ,
                                                rs.getString("password"), rs.getInt("salary"), 
                                                rs.getString("bank_account_id"), 
                                                LocalDate.parse(rs.getString("startDate")), 
                                                this.loadShiftsForEmployee(id),
                                                this.loadRolesForEmployee(id));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to load employee data: " + e.getMessage());
        }
        return employee;
    }

    public EnumSet<Role> loadRolesForEmployee(String id){
        String url = "jdbc:sqlite:" + DB_NAME;
        String query = "SELECT * FROM EMPLOYEE_TO_ROLE WHERE employee_id = ?";
        EnumSet<Role> roles = EnumSet.noneOf(Role.class);
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn != null) {
                pstmt.setString(1, id);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    roles.add(Role.values()[rs.getInt("role")]);
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to load employee data: " + e.getMessage());
        }
        return roles;
    }

    public ArrayList<Trio<LocalDate, ShiftTime, Role>> loadShiftsForEmployee(String id){
        String url = "jdbc:sqlite:" + DB_NAME;
        String query = "SELECT * FROM SHIFT_TO_EMPLOYEE WHERE employee_id = ?";
        ArrayList<Trio<LocalDate, ShiftTime, Role>> shifts = new ArrayList<Trio<LocalDate, ShiftTime, Role>>();
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn != null) {
                pstmt.setString(1, id);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    shifts.add(new Trio<LocalDate, ShiftTime, Role>(LocalDate.parse(rs.getString("shift_date")), ShiftTime.values()[rs.getInt("shift_time")], Role.values()[rs.getInt("role")]));
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to load employee data: " + e.getMessage());
        }
        return shifts;
    }

    public ArrayList<Shift> loadShiftsForBranch(int branchid){
        String url = "jdbc:sqlite:" + DB_NAME;
        String query = "SELECT * FROM SHIFTS WHERE branch_id = ?";
        ArrayList<Shift> shifts = new ArrayList<Shift>();
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn != null) {
                pstmt.setInt(1, branchid);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    shifts.add(this.loadShift(LocalDate.parse(rs.getString("shift_date")), ShiftTime.values()[rs.getInt("shift_time")], branchid));
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to load shift data: " + e.getMessage());
        }
        return shifts;
    }



    public Shift loadShift(LocalDate date, ShiftTime time, int branchId){
        String url = "jdbc:sqlite:" + DB_NAME;
        String query = "SELECT * FROM SHIFTS WHERE shift_date = ? AND shift_time = ? AND branch_id = ?";
        Shift shift = null;
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn != null) {
                pstmt.setString(1, date.toString());
                pstmt.setInt(2, time.ordinal());
                pstmt.setInt(3, branchId);
                ResultSet rs = pstmt.executeQuery();
                shift = Shift.loadShift(date, time, branchId, rs.getInt("has_delivery") == 1, this.loadRequiredEmployeesForShift(date, time, branchId), this.loadEmployeesForShift(date, time, branchId));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to load shift data: " + e.getMessage());
        }
        return shift;
    }

    public EnumMap<Role, Integer> loadRequiredEmployeesForShift(LocalDate date, ShiftTime time, int branchId){
        String url = "jdbc:sqlite:" + DB_NAME;
        String query = "SELECT * FROM SHIFT_ROLE_TO_REQUIRED WHERE shift_date = ? AND shift_time = ? AND branch_id = ?";
        EnumMap<Role, Integer> requiredEmployees = new EnumMap<Role, Integer>(Role.class);
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn != null) {
                pstmt.setString(1, date.toString());
                pstmt.setInt(2, time.ordinal());
                pstmt.setInt(3, branchId);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    //System.out.println(rs.getInt("role") + " " + rs.getInt("required"));
                    requiredEmployees.put(Role.values()[rs.getInt("role")], rs.getInt("required"));
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to load required employees data: " + e.getMessage());
        }
        return requiredEmployees;
    }

    public EnumMap<Role, ArrayList<String>> loadEmployeesForShift(LocalDate date, ShiftTime time, int branchId){
        String url = "jdbc:sqlite:" + DB_NAME;
        String query = "SELECT * FROM SHIFT_TO_EMPLOYEE WHERE shift_date = ? AND shift_time = ? AND branch_id = ?";
        EnumMap<Role, ArrayList<String>> employees = new EnumMap<Role, ArrayList<String>>(Role.class);
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn != null) {
                pstmt.setString(1, date.toString());
                pstmt.setInt(2, time.ordinal());
                pstmt.setInt(3, branchId);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    if(!employees.containsKey(Role.values()[rs.getInt("role")]))
                        employees.put(Role.values()[rs.getInt("role")], new ArrayList<String>());
                    employees.get(Role.values()[rs.getInt("role")]).add(rs.getString("employee_id"));
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to load employees data: " + e.getMessage());
        }
        return employees;
    }
    
    public ConcurrentHashMap<LocalDate, EnumMap<ShiftTime, ArrayList<String>>> loadAvailableEmployeesFromDB(int branchId){
        String url = "jdbc:sqlite:" + DB_NAME;
        String query = "SELECT * FROM SHIFT_AVAILABILITY WHERE branch_id = ?";
        ConcurrentHashMap<LocalDate, EnumMap<ShiftTime, ArrayList<String>>> availableEmployees = new ConcurrentHashMap<LocalDate, EnumMap<ShiftTime, ArrayList<String>>>();
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn != null) {
                pstmt.setInt(1, branchId);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    if(!availableEmployees.containsKey(LocalDate.parse(rs.getString("shift_date"))))
                        availableEmployees.put(LocalDate.parse(rs.getString("shift_date")), new EnumMap<ShiftTime, ArrayList<String>>(ShiftTime.class));
                    if(!availableEmployees.get(LocalDate.parse(rs.getString("shift_date"))).containsKey(ShiftTime.values()[rs.getInt("shift_time")]))
                        availableEmployees.get(LocalDate.parse(rs.getString("shift_date"))).put(ShiftTime.values()[rs.getInt("shift_time")], new ArrayList<String>());
                    availableEmployees.get(LocalDate.parse(rs.getString("shift_date"))).get(ShiftTime.values()[rs.getInt("shift_time")]).add(rs.getString("employee_id"));
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to load available employees data: " + e.getMessage());
        }
        return availableEmployees;
    }

    public LocalDate loadLastDateForSubmittingShiftsFromDB(int branchId){
        String url = "jdbc:sqlite:" + DB_NAME;
        String query = "SELECT last_date_for_submitting_availability FROM BRANCHES WHERE id = ?";
        LocalDate lastDate = null;
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn != null) {
                pstmt.setInt(1, branchId);
                ResultSet rs = pstmt.executeQuery();
                lastDate = LocalDate.parse(rs.getString("last_date_for_submitting_availability"));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to load last date for submitting shifts data: " + e.getMessage());
        }
        return lastDate;
    }

    

// --------- Update

    /// change password of employee
    public void setEmployeePassword(String id, String newPassword){ //v
        String url = "jdbc:sqlite:" + DB_NAME;
        String query = "UPDATE EMPLOYEES SET password = ? WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn != null) {
                pstmt.setString(1, newPassword);
                pstmt.setString(2, id);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to remove employee from DB: " + e.getMessage());
        }

    }

    /// set last date for submitting shifts
    public void setLastDateForSubmittingShifts(LocalDate date, int branchId){
        String url = "jdbc:sqlite:" + DB_NAME;
        String query = "UPDATE BRANCHES SET last_date_for_submitting_availability = ? WHERE id = ?";
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn != null) {
                pstmt.setString(1, date.toString());
                pstmt.setInt(2, branchId);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to set last date for submitting shifts: " + e.getMessage());
        }
    }




// --------- Delete

    /// remove availability for shift
    public void removeAvailability(String employeeId, LocalDate shiftDate, ShiftTime shiftTime, int branchId){
        String url = "jdbc:sqlite:" + DB_NAME;
        String query = "DELETE FROM SHIFT_AVAILABILITY WHERE employee_id = ? AND shift_date = ? AND shift_time = ? AND branch_id = ?";
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn != null) {
                pstmt.setString(1, employeeId);
                pstmt.setString(2, shiftDate.toString());
                pstmt.setInt(3, shiftTime.ordinal());
                pstmt.setInt(4, branchId);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to remove availability from DB: " + e.getMessage());
        }
    }

    /// remove shift from db
    public void removeShift(LocalDate shiftDate, ShiftTime shiftTime, int branchId){
        String url = "jdbc:sqlite:" + DB_NAME;
        String query = "DELETE FROM SHIFTS WHERE shift_date = ? AND shift_time = ? AND branch_id = ?";
        
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(query);) {
            if (conn != null) {
                pstmt.setString(1, shiftDate.toString());
                pstmt.setInt(2, shiftTime.ordinal());
                pstmt.setInt(3, branchId);
                pstmt.executeUpdate();
                // SHIFT_AVAILABILITY has ON DELETE CASCADE
                // SHIFT_ROLE_TO_REQUIRED has ON DELETE CASCADE
                // SHIFT_TO_EMPLOYEE has ON DELETE CASCADE
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to remove shift from DB: " + e.getMessage());
        }
    }

    /// remove employee from shift
    public void removeEmployeeFromShift(String employeeId, LocalDate shiftDate, ShiftTime shiftTime, Role role, int branchId){
        String url = "jdbc:sqlite:" + DB_NAME;
        String query1 = "DELETE FROM SHIFT_TO_EMPLOYEE WHERE employee_id = ? AND shift_date = ? AND shift_time = ? AND branch_id = ?";
        String query2 = "UPDATE SHIFT_ROLE_TO_REQUIRED SET required = required + 1 WHERE shift_date = ? AND shift_time = ? AND branch_id = ? AND role = ?";
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt1 = conn.prepareStatement(query1);
            PreparedStatement pstmt2 = conn.prepareStatement(query2);) {
            if (conn != null) {
                pstmt1.setString(1, employeeId);
                pstmt1.setString(2, shiftDate.toString());
                pstmt1.setInt(3, shiftTime.ordinal());
                pstmt1.setInt(4, branchId);
                pstmt1.executeUpdate();
                pstmt2.setString(1, shiftDate.toString());
                pstmt2.setInt(2, shiftTime.ordinal());
                pstmt2.setInt(3, branchId);
                pstmt2.setInt(4, role.ordinal());
                pstmt2.executeUpdate();
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to remove employee from shift: " + e.getMessage());
        }
    }

    /// remove employee from db
    public void removeEmployee(String id) {
        String url = "jdbc:sqlite:" + DB_NAME;
        String query = "DELETE FROM EMPLOYEES WHERE id = ?";
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn != null) {
                pstmt.setString(1, id);
                pstmt.executeUpdate();
                // EMPLOYEE_TO_ROLE has ON DELETE CASCADE
                // SHIFT_AVAILABILITY has ON DELETE CASCADE
                // SHIFT_TO_EMPLOYEE has ON DELETE CASCADE
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to remove employee from DB: " + e.getMessage());
        }
    }
    
}