package org.example.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;

import org.example.Business.BranchController;
import org.example.Business.Employee;
import org.example.Business.Enums.EmploymentType;
import org.example.Business.Enums.Role;
import org.example.Business.Enums.ShiftTime;
import org.example.Utilities.Trio;

// static service class (API)
public class SystemService {

    private static BranchController branchController = BranchController.getInstance() ;
    private static ArrayList<Integer> branchIds = branchController.loadBranchIds();

    private SystemService(){}

    public static void loadBranchIds(){
        branchIds = branchController.loadBranchIds();
    }

    public static boolean hasBranch(int branchId){
        return branchIds.contains(branchId);
    }

    public static void logInBranch(int branchId){
        branchController.logInBranch(branchId);
    }

    public static Employee logInEmployee(String username, String id, String password, int branchId){
        return branchController.logInEmployee(username, id, password, branchId);
    }

    public static void logOutEmployee(String eId, int branchId){
        branchController.logOut(eId, branchId);
    }

    public static void submitAvailabilityForShift(String uId, LocalDate date, ShiftTime shiftTime, int branchId){
        branchController.submitAvailability(uId, date, shiftTime, branchId);
    }

    public static void removeAvailabilityForShift(String uId, LocalDate date, ShiftTime shiftTime, int branchId){
        branchController.removeAvailability(uId, date, shiftTime, branchId);
    }

    public static void changePassword(String uId, String oldPassword, String newPassword, int branchId){
        branchController.setEmployeePassword(uId, oldPassword, newPassword, branchId);
    }

    public static void addShiftToSchedule(LocalDate date, ShiftTime time, boolean hasDelivery, EnumMap<Role,Integer> requiredEmployees, String requestingUserId, int branchId){
        branchController.addShift(date, time, hasDelivery, requiredEmployees, requestingUserId, branchId);
    }

    public static void removeShiftFromSchedule(LocalDate date, ShiftTime time, String requestingUserId, int branchId){
        branchController.removeShift(date, time, requestingUserId, branchId);
    }

    public static void addEmployeeToShift(String eid, LocalDate date, ShiftTime time, Role role, String requestingUserId, int branchId){
        branchController.addEmployeeToShift(eid, date, time, role, requestingUserId, branchId);
    }

    public static void removeEmployeeFromShift(String eid, LocalDate date, ShiftTime time, Role role, String requestingUserId, int branchId){
        branchController.removeEmployeeFromShift(eid, date, time, role, requestingUserId, branchId);
    }

    public static String getEmployeesForShiftStr(LocalDate date, ShiftTime time, String requestingUserId, int branchId){
        return branchController.getEmployeesForShiftStr(date, time, requestingUserId, branchId);
    }

    public static String getRequiredEmployeesForShiftStr(LocalDate date, ShiftTime time, String requestingUserId, int branchId){
        return branchController.getRequiredEmployeesForShiftStr(date, time, requestingUserId, branchId);
    }

    public static String getAvailableEmployeesForShiftStr(LocalDate date, ShiftTime time, String requestingUserId, int branchId){
        return branchController.getAvailableEmployeesForShiftStr(date, time, requestingUserId, branchId);
    }

    public static Employee addNewEmployee(String name, String id, int branchId, EmploymentType et, int salary, String bankAccountId, String requestingUserId){
        return branchController.addNewEmployee(name, id, branchId, et, salary, bankAccountId, requestingUserId);
    }

    public static void removeEmployee(String id, String requestingUserId, int branchId){
        branchController.removeEmployee(id, requestingUserId, branchId);
    }

    public static void assignRoleToEmployee(String eid, Role role, String requestingUserId, int branchId){
        branchController.assignRoleToEmployee(eid, role, requestingUserId, branchId);
    }

    public static void setLastDateForSubmittingAvailability(LocalDate date, String requestingUserId, int branchId){
        branchController.setLastDateForSubmitting(date, requestingUserId, branchId);
    }

    public static ArrayList<Trio<LocalDate, ShiftTime, Role>> getShiftsForEmployee(String eid, int branchId){
        return branchController.getShiftsForEmployee(eid, branchId);
    }

    public static int wareHouseQualification(String eid, int branchId){
        return branchController.wareHouseQualification(eid, branchId);
    }
    

    
}
