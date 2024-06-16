package org.example.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EnumMap;

import org.example.Business.Enums.Role;
import org.example.Business.Enums.ShiftTime;

public class Database {


    private static final String DB_NAME = "superli.db";

    // ---------------------------- Table Creation Queries ----------------------------

    private static final String createEmployeeTableQuery = "CREATE TABLE IF NOT EXISTS EMPLOYEES (\n"
            + "	id text PRIMARY KEY,\n"
            + "	name text NOT NULL,\n"
            + "	password text NOT NULL,\n"
            + "	employment_type integer NOT NULL,\n"
            + "	salary integer NOT NULL,\n"
            + "	bank_account_id text NOT NULL\n"
            + " branch_id integer NOT NULL,\n"
            + " FOREIGN KEY(branch_id) REFERENCES BRANCHES(id) ON DELETE CASCADE\n"
            + ");";
    
    private static final String createEmployeeToRoleTableQuery = "CREATE TABLE IF NOT EXISTS EMPLOYEE_TO_ROLE (\n"
            + "	employee_id text NOT NULL,\n"
            + "	role integer NOT NULL,\n"
            + "	FOREIGN KEY(employee_id) REFERENCES EMPLOYEES(id) ON DELETE CASCADE\n"
            + " PRIMARY KEY (employee_id, role)\n"
            + ");";
    
    private static final String createShiftsTableQuery = "CREATE TABLE IF NOT EXISTS SHIFTS (\n"
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
            
    
    // ---------------------------- Connection / Creation ----------------------------
    
    public static void connectToDB() {

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
            System.out.println("failed to create database. "+e.getMessage());
        }
        
    }

    // ---------------------------- CRUD Methods ----------------------------

    /// submit availability to shift
    public static void submitAvailability(String employeeId, String shiftDate, ShiftTime shiftTime){
        String url = "jdbc:sqlite:" + DB_NAME;
        String query = "INSERT INTO SHIFT_AVAILABILITY (employee_id, shift_date, shift_time) VALUES (?, ?, ?)";
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn != null) {
                pstmt.setString(1, employeeId);
                pstmt.setString(2, shiftDate);
                pstmt.setInt(3, shiftTime.ordinal());
                pstmt.executeUpdate();
                System.out.println("LOG: Employee "+employeeId+" availability to "+shiftDate+" submitted successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to submit availability to DB: " + e.getMessage());
        }
    }

    /// remove availability for shift
    public static void removeAvailability(String employeeId, String shiftDate, ShiftTime shiftTime){
        String url = "jdbc:sqlite:" + DB_NAME;
        String query = "DELETE FROM SHIFT_AVAILABILITY WHERE employee_id = ? AND shift_date = ? AND shift_time = ?";
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn != null) {
                pstmt.setString(1, employeeId);
                pstmt.setString(2, shiftDate);
                pstmt.setInt(3, shiftTime.ordinal());
                pstmt.executeUpdate();
                System.out.println("LOG: Employee "+employeeId+" availability to "+shiftDate+" removed successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to remove availability from DB: " + e.getMessage());
        }
    }

    /// change password of employee
    public static void changePassword(String id, String newPassword){
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
            System.out.println("Failed to remove employee from DB: " + e.getMessage());
        }

    }

    /// add shift to db
    public static void addShift(String shiftDate, ShiftTime shiftTime, EnumMap<Role,Integer> requiredEmployees, int branchId){
        String url = "jdbc:sqlite:" + DB_NAME;
        String queryShift = "INSERT INTO SHIFTS (shift_date, shift_time, branch_id) VALUES (?, ?, ?)";
        String queryShiftRoleToRequired = "INSERT INTO SHIFT_ROLE_TO_REQUIRED (shift_date, shift_time, branch_id, role, required) VALUES (?, ?, ?, ?, ?)";
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmtShift = conn.prepareStatement(queryShift);
            PreparedStatement pstmtShiftRoleToRequired = conn.prepareStatement(queryShiftRoleToRequired);) {
            if (conn != null) {
                // add shift to shifts table
                pstmtShift.setString(1, shiftDate);
                pstmtShift.setInt(2, shiftTime.ordinal());
                pstmtShift.setInt(3, branchId);
                pstmtShift.executeUpdate();
                System.out.println("LOG: Shift "+shiftDate+" "+shiftTime+" added successfully.");
                // add required employees for each role
                for(Role role : requiredEmployees.keySet())
                {
                    pstmtShiftRoleToRequired.setString(1, shiftDate);
                    pstmtShiftRoleToRequired.setInt(2, shiftTime.ordinal());
                    pstmtShiftRoleToRequired.setInt(3, branchId);
                    pstmtShiftRoleToRequired.setInt(4, role.ordinal());
                    pstmtShiftRoleToRequired.setInt(5, requiredEmployees.get(role));
                    pstmtShiftRoleToRequired.executeUpdate();
                    System.out.println("DBLOG: Shift "+shiftDate+" "+shiftTime+" requires "+requiredEmployees.get(role)+" employees of role "+role.toString());
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to add shift to DB: " + e.getMessage());
        }
    }

    /// remove shift from db
    public static void removeShift(String shiftDate, ShiftTime shiftTime, int branchId){
        String url = "jdbc:sqlite:" + DB_NAME;
        String query = "DELETE FROM SHIFTS WHERE shift_date = ? AND shift_time = ? AND branch_id = ?";
        
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(query);) {
            if (conn != null) {
                pstmt.setString(1, shiftDate);
                pstmt.setInt(2, shiftTime.ordinal());
                pstmt.setInt(3, branchId);
                pstmt.executeUpdate();
                // SHIFT_AVAILABILITY has ON DELETE CASCADE
                // SHIFT_ROLE_TO_REQUIRED has ON DELETE CASCADE
                // SHIFT_TO_EMPLOYEE has ON DELETE CASCADE
                System.out.println("LOG: Shift "+shiftDate+" "+shiftTime+" removed successfully from db.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to remove shift from DB: " + e.getMessage());
        }
    }

    /// assign employee to shift
    public static void assignEmployeeToShift(String employeeId, String shiftDate, ShiftTime shiftTime, Role role, int branchId){
        String url = "jdbc:sqlite:" + DB_NAME;
        String query1 = "INSERT INTO SHIFT_TO_EMPLOYEE (employee_id, shift_date, shift_time, branch_id, role) VALUES (?, ?, ?, ?, ?)";
        String query2 = "UPDATE SHIFT_ROLE_TO_REQUIRED SET required = required - 1 WHERE shift_date = ? AND shift_time = ? AND branch_id = ? AND role = ?";
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt1 = conn.prepareStatement(query1);
            PreparedStatement pstmt2 = conn.prepareStatement(query2);) {
            if (conn != null) {
                pstmt1.setString(1, employeeId);
                pstmt1.setString(2, shiftDate);
                pstmt1.setInt(3, shiftTime.ordinal());
                pstmt1.setInt(4, branchId);
                pstmt1.setInt(5, role.ordinal());
                pstmt1.executeUpdate();
                System.out.println("LOG: Employee "+employeeId+" assigned to shift "+shiftDate+" "+shiftTime+" as "+role.toString());
                pstmt2.setString(1, shiftDate);
                pstmt2.setInt(2, shiftTime.ordinal());
                pstmt2.setInt(3, branchId);
                pstmt2.setInt(4, role.ordinal());
                pstmt2.executeUpdate();
                System.out.println("LOG: Required employees for shift "+shiftDate+" "+shiftTime+" as "+role.toString()+" updated.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to assign employee to shift: " + e.getMessage());
        }
    }

    /// remove employee from shift
    public static void removeEmployeeFromShift(String employeeId, String shiftDate, ShiftTime shiftTime, Role role, int branchId){
        String url = "jdbc:sqlite:" + DB_NAME;
        String query1 = "DELETE FROM SHIFT_TO_EMPLOYEE WHERE employee_id = ? AND shift_date = ? AND shift_time = ? AND branch_id = ?";
        String query2 = "UPDATE SHIFT_ROLE_TO_REQUIRED SET required = required + 1 WHERE shift_date = ? AND shift_time = ? AND branch_id = ? AND role = ?";
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt1 = conn.prepareStatement(query1);
            PreparedStatement pstmt2 = conn.prepareStatement(query2);) {
            if (conn != null) {
                pstmt1.setString(1, employeeId);
                pstmt1.setString(2, shiftDate);
                pstmt1.setInt(3, shiftTime.ordinal());
                pstmt1.setInt(4, branchId);
                pstmt1.executeUpdate();
                System.out.println("LOG: Employee "+employeeId+" removed from shift "+shiftDate+" "+shiftTime);
                pstmt2.setString(1, shiftDate);
                pstmt2.setInt(2, shiftTime.ordinal());
                pstmt2.setInt(3, branchId);
                pstmt2.setInt(4, role.ordinal());
                pstmt2.executeUpdate();
                System.out.println("LOG: Required employees for shift "+shiftDate+" "+shiftTime+" as "+role.toString()+" updated.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to remove employee from shift: " + e.getMessage());
        }
    }

    /// register new employee to db
    public static void addEmployee(String id, String name, String password, int employmentType, int salary, String bankAccountId, int branchId) {

        String url = "jdbc:sqlite:" + DB_NAME;
        String query = "INSERT INTO EMPLOYEES (id, name, password, employment_type, salary, bank_account_id, branch_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
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
                pstmt.executeUpdate();
                System.out.println("LOG: Employee "+id+" added successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to add employee to DB: " + e.getMessage());
        }

    }

    /// remove employee from db
    public static void removeEmployee(String id) {
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
            System.out.println("Failed to remove employee from DB: " + e.getMessage());
        }
    }

    /// assign new role to employee
    public static void assignNewRoleToEmployee(String id, Role role){
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
            System.out.println("Failed to assign new role to employee: " + e.getMessage());
        }
    }

    /// set last date for submitting shifts
    public static void setLastDateForSubmittingShifts(String date, int branchId){
        String url = "jdbc:sqlite:" + DB_NAME;
        String query = "UPDATE BRANCHES SET last_date_for_submitting_availability = ? WHERE id = ?";
    
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (conn != null) {
                pstmt.setString(1, date);
                pstmt.setInt(2, branchId);
                pstmt.executeUpdate();
                System.out.println("LOG: Last date for submitting shifts for branch "+branchId+" set to "+date);
            }
        } catch (SQLException e) {
            System.out.println("Failed to set last date for submitting shifts: " + e.getMessage());
        }
    }

    

    ///TODO load branch data


    //TODO static method in branch class that pulls data

    
    // ArrayList<branchId>
    public static ArrayList<int> loadData(){

    }

    public static Branch getBranchData(int branchId){

        
    }

