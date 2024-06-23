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
import java.util.concurrent.ConcurrentHashMap;

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
        connectToDB();
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
    
    private void connectToDB() {

        String url = "jdbc:sqlite:" + DB_NAME;
        // DriverManager will try to connect to the database file. 
        // If the file does not exist, it will be created.
        // The database file will be created in the current working directory of your Java application. 
        // You can specify an absolute or relative path if you want to create the database file in a specific location
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("A new database has been created.");
                // TODO: create tables
                Statement stmt = conn.createStatement();
                stmt.execute(createEmployeeTableQuery);
                stmt.execute(createEmployeeToRoleTableQuery);
                stmt.execute(createShiftsTableQuery);
                stmt.execute(createShiftAvailabilityTableQuery);
                stmt.execute(createShiftToEmployeeTableQuery);
                stmt.execute(createShiftRoleToRequiredTableQuery);
                stmt.execute(createBranchesTableQuery);
                // TODO: load with demo data.
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("failed to create database. "+e.getMessage());
        }
        
    }

// ---------------------------- CRUD Methods ----------------------------


//////TODO: run files for role import


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
                System.out.println("LOG: Employee "+employeeId+" availability to "+shiftDate+" submitted successfully.");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to submit availability to DB: " + e.getMessage());
        }
    }

    /// add shift to db
    public void addShift(LocalDate shiftDate, ShiftTime shiftTime, EnumMap<Role,Integer> requiredEmployees, int branchId){
        String url = "jdbc:sqlite:" + DB_NAME;
        String queryShift = "INSERT INTO SHIFTS (shift_date, shift_time, branch_id) VALUES (?, ?, ?)";
        String queryShiftRoleToRequired = "INSERT INTO SHIFT_ROLE_TO_REQUIRED (shift_date, shift_time, branch_id, role, required) VALUES (?, ?, ?, ?, ?)";
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmtShift = conn.prepareStatement(queryShift);
            PreparedStatement pstmtShiftRoleToRequired = conn.prepareStatement(queryShiftRoleToRequired);) {
            if (conn != null) {
                // add shift to shifts table
                pstmtShift.setString(1, shiftDate.toString());
                pstmtShift.setInt(2, shiftTime.ordinal());
                pstmtShift.setInt(3, branchId);
                pstmtShift.executeUpdate();
                System.out.println("LOG: Shift "+shiftDate+" "+shiftTime+" added successfully.");
                // add required employees for each role
                for(Role role : requiredEmployees.keySet())
                {
                    pstmtShiftRoleToRequired.setString(1, shiftDate.toString());
                    pstmtShiftRoleToRequired.setInt(2, shiftTime.ordinal());
                    pstmtShiftRoleToRequired.setInt(3, branchId);
                    pstmtShiftRoleToRequired.setInt(4, role.ordinal());
                    pstmtShiftRoleToRequired.setInt(5, requiredEmployees.get(role));
                    pstmtShiftRoleToRequired.executeUpdate();
                    System.out.println("DBLOG: Shift "+shiftDate+" "+shiftTime+" requires "+requiredEmployees.get(role)+" employees of role "+role.toString());
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
                System.out.println("LOG: Employee "+employeeId+" assigned to shift "+shiftDate+" "+shiftTime+" as "+role.toString());
                pstmt2.setString(1, shiftDate.toString());
                pstmt2.setInt(2, shiftTime.ordinal());
                pstmt2.setInt(3, branchId);
                pstmt2.setInt(4, role.ordinal());
                pstmt2.executeUpdate();
                System.out.println("LOG: Required employees for shift "+shiftDate+" "+shiftTime+" as "+role.toString()+" updated.");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to assign employee to shift: " + e.getMessage());
        }
    }

    /// register new employee to db
    public void addEmployee(String id, String name, String password, int employmentType, int salary, String bankAccountId, int branchId , LocalDate startDate) {

        String url = "jdbc:sqlite:" + DB_NAME;
        String query = "INSERT INTO EMPLOYEES (id, name, password, employment_type, salary, bank_account_id, branch_id, startDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
        try (Connection conn = DriverManager.getConnection(url);
            //using Prepared Statement to prevent SQL Injection
            //(pre-compiles the sql statement)
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn != null) {
                pstmt.setString(1, id);
                pstmt.setString(2, name);
                pstmt.setString(3, password);
                pstmt.setString(4, bankAccountId);
                pstmt.setInt(5, employmentType);
                pstmt.setInt(6, salary);
                pstmt.setInt(7, branchId);
                pstmt.setString(8, startDate.toString());
                pstmt.executeUpdate();
                System.out.println("LOG: Employee "+id+" added successfully.");
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
                System.out.println("LOG: Employee "+id+" assigned new role "+role.toString());
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
                                                this.loadShiftsForEmployee(id));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to load employee data: " + e.getMessage());
        }
        return employee;
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
                shift = Shift.loadShift(date, time, branchId, this.loadRequiredEmployeesForShift(date, time, branchId), this.loadEmployeesForShift(date, time, branchId));
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
                System.out.println("LOG: Employee "+id+" password changed successfully.");
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
                System.out.println("LOG: Last date for submitting shifts for branch "+branchId+" set to "+date);
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
                System.out.println("LOG: Employee "+employeeId+" availability to "+shiftDate+" removed successfully.");
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
                System.out.println("LOG: Shift "+shiftDate+" "+shiftTime+" removed successfully from db.");
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
                System.out.println("LOG: Employee "+employeeId+" removed from shift "+shiftDate+" "+shiftTime);
                pstmt2.setString(1, shiftDate.toString());
                pstmt2.setInt(2, shiftTime.ordinal());
                pstmt2.setInt(3, branchId);
                pstmt2.setInt(4, role.ordinal());
                pstmt2.executeUpdate();
                System.out.println("LOG: Required employees for shift "+shiftDate+" "+shiftTime+" as "+role.toString()+" updated.");
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
                System.out.println("LOG: Employee "+id+" removed successfully from db.");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to remove employee from DB: " + e.getMessage());
        }
    }









    
    
    



    // ---------------------------- Table Creation Queries ----------------------------

    private static final String createEmployeeTableQuery = "CREATE TABLE IF NOT EXISTS EMPLOYEES (\n"
    + "	id text PRIMARY KEY,\n"
    + "	name text NOT NULL,\n"
    + "	password text NOT NULL,\n"
    + "	employment_type integer NOT NULL,\n"
    + "	salary integer NOT NULL,\n"
    + "	bank_account_id text NOT NULL\n"
    + " branch_id integer NOT NULL,\n"
    + " startDate text NOT NULL,\n"
    + " FOREIGN KEY(branch_id) REFERENCES BRANCHES(id) ON DELETE CASCADE\n"
    + ");";

    private static final String createEmployeeToRoleTableQuery = "CREATE TABLE IF NOT EXISTS EMPLOYEE_TO_ROLE (\n"
    + "	employee_id text NOT NULL,\n"
    + "	role integer NOT NULL,\n"
    + "	FOREIGN KEY(employee_id) REFERENCES EMPLOYEES(id) ON DELETE CASCADE\n"
    + " PRIMARY KEY (employee_id, role)\n"
    + ");";

    static final String createShiftsTableQuery = "CREATE TABLE IF NOT EXISTS SHIFTS (\n"
    + " shift_date text NOT NULL,\n"
    + " shift_time integer NOT NULL,\n"
    + " branch_id integer NOT NULL,\n"
    + " FOREIGN KEY(branch_id) REFERENCES BRANCHES(id) ON DELETE CASCADE,\n"
    + " primary key (shift_date, shift_time, branch_id)\n"
    + ");";

    private static final String createShiftAvailabilityTableQuery = "CREATE TABLE IF NOT EXISTS SHIFT_AVAILABILITY (\n"
    + "	employee_id text NOT NULL,\n"
    + "	shift_date text NOT NULL,\n"
    + "	shift_time integer NOT NULL,\n"
    + " branch_id integer NOT NULL,\n"
    + " FOREIGN KEY(branch_id) REFERENCES BRANCHES(id) ON DELETE CASCADE,\n"
    + "	FOREIGN KEY(employee_id) REFERENCES EMPLOYEES(id) ON DELETE CASCADE,\n"
    + "	FOREIGN KEY(shift_date, shift_time, branch_id) REFERENCES SHIFTS(shift_date, shift_time, branch_id) ON DELETE CASCADE,\n"
    + " PRIMARY KEY (employee_id, shift_date, shift_time)\n"
    + ");";

    private static final String createShiftToEmployeeTableQuery = "CREATE TABLE IF NOT EXISTS SHIFT_TO_EMPLOYEE (\n"
    + "	employee_id text NOT NULL,\n"
    + "	shift_date text NOT NULL,\n"
    + "	shift_time integer NOT NULL,\n"
    + " branch_id integer NOT NULL,\n"
    + "	role integer NOT NULL,\n"
    + "	FOREIGN KEY(branch_id) REFERENCES BRANCHES(id) ON DELETE CASCADE,\n"
    + "	FOREIGN KEY(employee_id) REFERENCES EMPLOYEES(id) ON DELETE CASCADE,\n"
    + "	FOREIGN KEY(shift_date, shift_time, branch_id) REFERENCES SHIFTS(shift_date, shift_time, branch_id) ON DELETE CASCADE,\n"
    + " PRIMARY KEY (employee_id, shift_date, shift_time)\n"
    + ");";

    private static final String createShiftRoleToRequiredTableQuery = "CREATE TABLE IF NOT EXISTS SHIFT_ROLE_TO_REQUIRED (\n"
    + "	shift_date text NOT NULL,\n"
    + "	shift_time integer NOT NULL,\n"
    + " branch_id integer NOT NULL,\n"
    + "	role integer NOT NULL,\n"
    + "	required integer NOT NULL,\n"
    + "	FOREIGN KEY(branch_id) REFERENCES BRANCHES(id) ON DELETE CASCADE,\n"
    + "	FOREIGN KEY(shift_date, shift_time, branch_id) REFERENCES SHIFTS(shift_date, shift_time, branch_id) ON DELETE CASCADE,\n"
    + " PRIMARY KEY (shift_date, shift_time, branch_id, role)\n"
    + ");";


    private static final String createBranchesTableQuery = "CREATE TABLE IF NOT EXISTS BRANCHES (\n"
    + "	id integer PRIMARY KEY,\n"
    + " hr_manager_id text NOT NULL,\n"
    + " last_date_for_submitting_availability text NOT NULL,\n"
    + " FOREIGN KEY(hr_manager_id) REFERENCES EMPLOYEES(id)\n"
    + ");";

}