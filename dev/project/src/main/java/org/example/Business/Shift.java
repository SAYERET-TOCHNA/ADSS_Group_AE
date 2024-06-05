package org.example.Business;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

import org.example.Business.Enums.Role;
import org.example.Business.Enums.ShiftTime;


public class Shift {

    //------------------- fields -------------------

    private EnumMap<Role, ArrayList<String>> employees;
    private EnumMap<Role, Integer> requiredEmployees;
    private ShiftTime shiftTime;
    private LocalDate date;

    //------------------- construction (factory) -------------------

    private Shift(LocalDate date, ShiftTime shiftTime, EnumMap<Role, Integer> requiredEmployees) {
        this.date = date;
        this.shiftTime = shiftTime;
        this.requiredEmployees = requiredEmployees;
        this.employees = new EnumMap<Role, ArrayList<String>>(Role.class);
        for( Role role : requiredEmployees.keySet() ){
            this.employees.put(role, new ArrayList<String>());
        }
    }
    private Shift(){}
    
    
    public static Shift createShift(LocalDate date , ShiftTime shiftTime, EnumMap<Role, Integer> requiredEmployees){
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

        return new Shift(date, shiftTime, requiredEmployees);
    }

    //------------------- getter & setters -------------------

    public ShiftTime getShiftTime() {
        return shiftTime;
    }

    public LocalDate getShiftDate() {
        return date;
    }

    //------------------- methods -------------------

    public ArrayList<String> getEmployees(Role role){
        return employees.get(role);
    }

    public void addEmployee(String id , Role role){

        if(!employees.containsKey(role)) 
            throw new IllegalArgumentException("shift" + date.toString() + " " + shiftTime.toString() + " does not require employees of role: " + role.toString());

        if(requiredEmployees.get(role) == 0 )
            throw new IllegalArgumentException("shift" + date.toString() + " " + shiftTime.toString() + " does not require anymore employees of role: " + role.toString());
        
        for(String eId : employees.get(role))
            if(eId.equals(id))
                throw new IllegalArgumentException("Employee " + id + " is already assigned to shift" + date.toString() + " " + shiftTime.toString() + " as " + role.toString());

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
