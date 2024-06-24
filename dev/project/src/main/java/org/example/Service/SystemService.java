package org.example.Service;

//manage branches and system actions and data loading

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
// for next part should do Json?
public class SystemService {

    private BranchController branchController ;
    private ArrayList<Integer> branchIds ;

    public SystemService(){ branchController = new BranchController();}

    public void loadBranchIds(){
        branchIds = branchController.loadBranchIds();
        System.out.println(branchIds.size());
    }

    public boolean hasBranch(int branchId){
        return branchIds.contains(branchId);
    }

    public void logInBranch(int branchId){
        branchController.logInBranch(branchId);
    }

    public Employee logInEmployee(String username, String id, String password, int branchId){
        return branchController.logInEmployee(username, id, password, branchId);
    }

    public void logOutEmployee(String eId, int branchId){
        branchController.logOut(eId, branchId);
    }

    public void submitAvailabilityForShift(String uId, LocalDate date, ShiftTime shiftTime, int branchId){
        branchController.submitAvailability(uId, date, shiftTime, branchId);
    }

    public void removeAvailabilityForShift(String uId, LocalDate date, ShiftTime shiftTime, int branchId){
        branchController.removeAvailability(uId, date, shiftTime, branchId);
    }

    public void changePassword(String uId, String oldPassword, String newPassword, int branchId){
        branchController.setEmployeePassword(uId, oldPassword, newPassword, branchId);
    }

    public void addShiftToSchedule(LocalDate date, ShiftTime time, EnumMap<Role,Integer> requiredEmployees, String requestingUserId, int branchId){
        branchController.addShift(date, time, requiredEmployees, requestingUserId, branchId);
    }

    public void removeShiftFromSchedule(LocalDate date, ShiftTime time, String requestingUserId, int branchId){
        branchController.removeShift(date, time, requestingUserId, branchId);
    }

    public void addEmployeeToShift(String eid, LocalDate date, ShiftTime time, Role role, String requestingUserId, int branchId){
        branchController.addEmployeeToShift(eid, date, time, role, requestingUserId, branchId);
    }

    public void removeEmployeeFromShift(String eid, LocalDate date, ShiftTime time, Role role, String requestingUserId, int branchId){
        branchController.removeEmployeeFromShift(eid, date, time, role, requestingUserId, branchId);
    }

    public String getEmployeesForShiftStr(LocalDate date, ShiftTime time, String requestingUserId, int branchId){
        return branchController.getEmployeesForShiftStr(date, time, requestingUserId, branchId);
    }

    public String getRequiredEmployeesForShiftStr(LocalDate date, ShiftTime time, String requestingUserId, int branchId){
        return branchController.getRequiredEmployeesForShiftStr(date, time, requestingUserId, branchId);
    }

    public String getAvailableEmployeesForShiftStr(LocalDate date, ShiftTime time, String requestingUserId, int branchId){
        return branchController.getAvailableEmployeesForShiftStr(date, time, requestingUserId, branchId);
    }

    public Employee addNewEmployee(String name, String id, int branchId, EmploymentType et, int salary, String bankAccountId, String requestingUserId){
        return branchController.addNewEmployee(name, id, branchId, et, salary, bankAccountId, requestingUserId);
    }

    public void removeEmployee(String id, String requestingUserId, int branchId){
        branchController.removeEmployee(id, requestingUserId, branchId);
    }

    public void assignRoleToEmployee(String eid, Role role, String requestingUserId, int branchId){
        branchController.assignRoleToEmployee(eid, role, requestingUserId, branchId);
    }

    public void setLastDateForSubmittingAvailability(LocalDate date, String requestingUserId, int branchId){
        branchController.setLastDateForSubmitting(date, requestingUserId, branchId);
    }

    public ArrayList<Trio<LocalDate, ShiftTime, Role>> getShiftsForEmployee(String eid, int branchId){
        return branchController.getShiftsForEmployee(eid, branchId);
    }




    
}
