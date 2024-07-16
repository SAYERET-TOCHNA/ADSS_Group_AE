package org.example.Business;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;

import org.example.Business.Enums.Role;
import org.example.Business.Enums.ShiftTime;


public class Shift {

    //------------------- fields -------------------

    private EnumMap<Role, ArrayList<String>> employees;
    private EnumMap<Role, Integer> requiredEmployees;
    private ShiftTime shiftTime;
    private LocalDate date;
    private int branchId;
    private boolean hasDelivery;

    //------------------- construction (factory) -------------------

    private Shift(LocalDate date, ShiftTime shiftTime, int branchId, boolean hasDelivery, EnumMap<Role, Integer> requiredEmployees) {
        this.date = date;
        this.shiftTime = shiftTime;
        this.requiredEmployees = requiredEmployees;
        this.branchId = branchId;
        this.hasDelivery = hasDelivery;
        this.employees = new EnumMap<Role, ArrayList<String>>(Role.class);
        for( Role role : requiredEmployees.keySet() ){
            this.employees.put(role, new ArrayList<String>());
        }
    }
    private Shift(){}
    
    
    public static Shift createShift(LocalDate date , ShiftTime shiftTime , int branchId, boolean hasDelivery, EnumMap<Role, Integer> requiredEmployees){
        // checks if asked to require negative number of employees
        for (Role role : requiredEmployees.keySet()){
            if ( requiredEmployees.get(role) < 0 )
                throw new IllegalArgumentException("Number of required employees must be positive. thrown for role: " + role.toString() + " in shift " + date.toString() + " " + shiftTime.toString());
        }
        // checks if asked for shift managers
        if( !requiredEmployees.containsKey(Role.SHIFT_MANAGER) || requiredEmployees.get(Role.SHIFT_MANAGER) <= 0 )
            throw new IllegalArgumentException("Shift must have a shift manager.");
        // checks if the date is legal (not in the past)
        if( date.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Shift date must not be in the past. given date: " + date.toString()+ " current date: " + LocalDate.now().toString());
        // checks condition hasDelivey --> hasWareHouseWorker
        if(hasDelivery && (!requiredEmployees.containsKey(Role.WAREHOUSE) || requiredEmployees.get(Role.WAREHOUSE) <= 0))
            throw new IllegalArgumentException("Shift must have a warehouse worker if it has a delivery.");
        if(hasDelivery && (!requiredEmployees.containsKey(Role.DRIVER) || requiredEmployees.get(Role.DRIVER) <= 0))
            throw new IllegalArgumentException("Shift must have a driver if it has a delivery.");

        return new Shift(date, shiftTime, branchId, hasDelivery, requiredEmployees);
    }

    //------------------- loading from db -------------------

    public static Shift loadShift(LocalDate date, ShiftTime shiftTime, int branchId, boolean hasDelivery, EnumMap<Role, Integer> requiredEmployees, EnumMap<Role, ArrayList<String>> employees){
        return new Shift(date, shiftTime, branchId, hasDelivery, requiredEmployees, employees);
    }

    private Shift(LocalDate date, ShiftTime shiftTime, int branchId, boolean hasDelivery, EnumMap<Role, Integer> requiredEmployees, EnumMap<Role, ArrayList<String>> employees){
        this.date = date;
        this.shiftTime = shiftTime;
        this.branchId = branchId;
        this.hasDelivery = hasDelivery;
        this.requiredEmployees = requiredEmployees;
        this.employees = employees;
    }

    //------------------- getter & setters -------------------

    public ShiftTime getTime() {
        return shiftTime;
    }

    public LocalDate getDate() {
        return date;
    }

    //------------------- methods -------------------

    public ArrayList<String> getEmployees(Role role){
        return employees.get(role);
    }

    public void addEmployee(String id , Role role){//v

        if(!requiredEmployees.containsKey(role)) 
            throw new IllegalArgumentException("shift" + date.toString() + " " + shiftTime.toString() + " does not require employees of role: " + role.toString());

        if(requiredEmployees.get(role) == 0 )
            throw new IllegalArgumentException("shift" + date.toString() + " " + shiftTime.toString() + " does not require anymore employees of role: " + role.toString());
        
        for(Role r : employees.keySet())
            for(String eId : employees.get(r))
                if(eId.equals(id))
                    throw new IllegalArgumentException("Employee " + id + " is already assigned to shift" + date.toString() + " " + shiftTime.toString() + " as " + r.toString());

        if(!employees.containsKey(role))
            employees.put(role, new ArrayList<String>());
        employees.get(role).add(id);
        requiredEmployees.put(role, requiredEmployees.get(role) - 1);
    }

    public void removeEmployee(String id, Role role){
        if(!employees.containsKey(role)) 
            throw new IllegalArgumentException("shift" + date.toString() + " " + shiftTime.toString() + " does not require employees of role: " + role.toString());
        if(employees.get(role).contains(id)){
            ArrayList<String> employeesList = employees.get(role);
            for (int i=0 ; i < employeesList.size() ; i++){
                if(employeesList.get(i).equals(id)){
                    employeesList.remove(i);
                    break;
                }
            }
            requiredEmployees.put(role, requiredEmployees.get(role) + 1);
        }
        else
            throw new IllegalArgumentException("Employee " + id + " is not assigned to shift" + date.toString() + " " + shiftTime.toString() + " as " + role.toString());
    }

    public EnumMap<Role, ArrayList<String>> getEmployeeIds(){
        return new EnumMap<Role, ArrayList<String>>(employees);
    }

    public EnumMap<Role, Integer> getRequiredEmployees(){
        return new EnumMap<Role, Integer>(requiredEmployees);
    }

    


}
