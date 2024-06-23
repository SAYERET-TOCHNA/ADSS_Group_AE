package org.example.DataAccess;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.example.Business.Enums.Role;
import org.example.Business.Enums.ShiftTime;
import org.example.Business.Shift;


/// responsible for tables: 
///     SHIFTS, 
///     SHIFT_AVAILABILITY, 
///     SHIFT_TO_EMPLOYEE, 
///     SHIFT_ROLE_TO_REQUIRED
///     BRANCHES (last date for submitting shifts)
public class ShiftControllerDao {

    private int branchId;
    private DBObj dbObj;

    public ShiftControllerDao(int branchId) {
        this.branchId = branchId;
        this.dbObj = DBObj.getInstance();
    }

    // CRUD operations

    // create

    public void submitAvailabilityForShift(String eId, LocalDate date, ShiftTime time){
        this.dbObj.submitAvailability(eId, date, time, this.branchId);
    }

    public void addShift(LocalDate date, ShiftTime shiftTime, EnumMap<Role, Integer> requiredEmployees){
        this.dbObj.addShift(date, shiftTime, requiredEmployees, this.branchId);
    }

    public void addEmployeeToShift(String eId, LocalDate date, ShiftTime time, Role role){
        this.dbObj.assignEmployeeToShift(eId, date, time, role, this.branchId);
    }

    // read

    public HashMap<LocalDate, EnumMap<ShiftTime,Shift>> loadShiftsFromDB(){
        HashMap<LocalDate, EnumMap<ShiftTime,Shift>> shifts = new HashMap<LocalDate, EnumMap<ShiftTime,Shift>>();
        ArrayList<Shift> shiftsList = this.dbObj.loadShiftsForBranch(branchId);
        for(Shift shift : shiftsList){
            if(!shifts.containsKey(shift.getDate()))
                shifts.put(shift.getDate(), new EnumMap<ShiftTime,Shift>(ShiftTime.class));
            if(!shifts.get(shift.getDate()).containsKey(shift.getTime()))
                shifts.get(shift.getDate()).put(shift.getTime(), shift);
        }
        return shifts;
    }

    public ConcurrentHashMap<LocalDate ,  EnumMap<ShiftTime, ArrayList<String>>> loadAvailableEmployeesFromDB(){
        return this.dbObj.loadAvailableEmployeesFromDB(this.branchId);
    }

    public LocalDate loadLastDateForSubmittingShiftsFromDB(){
        return this.dbObj.loadLastDateForSubmittingShiftsFromDB(this.branchId);
    }
    

    // update

    public void setLastDateForSubmittingShifts(LocalDate date){
        this.dbObj.setLastDateForSubmittingShifts(date, this.branchId);
    }
    // delete

    public void removeAvailability(String eId, LocalDate date, ShiftTime time){
        this.dbObj.removeAvailability(eId, date, time, this.branchId);
    }

    public void removeShift(LocalDate date, ShiftTime time){
        this.dbObj.removeShift(date, time, this.branchId);
    }

    public void removeEmployeeFromShift(String eId, LocalDate date, ShiftTime time, Role role){
        this.dbObj.removeEmployeeFromShift(eId, date, time, role, this.branchId);
    }
}
