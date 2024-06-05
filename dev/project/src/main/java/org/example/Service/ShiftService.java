package org.example.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


import org.example.Business.Shift;
import org.example.Business.Enums.ShiftTime;
import org.example.Business.Enums.Role;

// EnumHashMap Lit af

public class ShiftService {

    private HashMap< LocalDate , EnumMap<ShiftTime, Shift> > shifts;
    //concurrent since many employees can submit at the same time
    private ConcurrentHashMap< LocalDate , EnumMap<ShiftTime, ArrayList<String>> > availableEmployees;
    private LocalDate lastDateForSubmitting;

    //------------------- construction -------------------

    public ShiftService() {
        shifts = new HashMap< LocalDate , EnumMap<ShiftTime, Shift> >();
        availableEmployees = new ConcurrentHashMap< LocalDate , EnumMap<ShiftTime, ArrayList<String>> >();
        lastDateForSubmitting = LocalDate.now();
    }
    
    //------------------- methods -------------------

    public void addShift(LocalDate date, ShiftTime shiftTime, EnumMap<Role, Integer> requiredEmployees){

        if(this.shifts.containsKey(date) && this.shifts.get(date).containsKey(shiftTime)){
            throw new IllegalArgumentException("Shift already exists");
        }
        else{
            Shift newShift = Shift.createShift(date, shiftTime, requiredEmployees);
            if(this.shifts.containsKey(date)){
                this.shifts.get(date).put(shiftTime, newShift);
            }
            else{
                this.shifts.put(date, new EnumMap<ShiftTime, Shift>(ShiftTime.class));
                this.shifts.get(date).put(shiftTime, newShift);
            }  
        }
    }

    public void removeShift(LocalDate date , ShiftTime time){ 
        if(!this.shifts.containsKey(date) || !this.shifts.get(date).containsKey(time)){
            throw new IllegalArgumentException("Shift already exists");
        }
        this.shifts.get(date).remove(time);
    }

    public void addAvailability(String eId, LocalDate date, ShiftTime time){

        if(date.isAfter(lastDateForSubmitting))
            throw new IllegalArgumentException("the deadline for submitting shifts has passed. was "+ lastDateForSubmitting.toString());

        if(date.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("cannot submit date from the past");

        if(!this.availableEmployees.containsKey(date))
            this.availableEmployees.put(date, new EnumMap<ShiftTime, ArrayList<String>>(ShiftTime.class));
        
        if(!this.availableEmployees.get(date).containsKey(time))
            this.availableEmployees.get(date).put(time, new ArrayList<String>());
        
        if(!this.availableEmployees.get(date).get(time).contains(eId))
            this.availableEmployees.get(date).get(time).add(eId);
        else
            throw new IllegalArgumentException("Employee " + eId + " already submitted  availability for shift " + date.toString() + " " + time.toString());
    }

    public void removeAvailability(String eId, LocalDate date, ShiftTime time){

        if(date.isAfter(lastDateForSubmitting))
            throw new IllegalArgumentException("the deadline for submitting shifts has passed. was "+ lastDateForSubmitting.toString());
            
        if(date.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("cannot remove availability of date from the past");

        if(!this.availableEmployees.containsKey(date))
            throw new IllegalArgumentException("failed to remove availability for shift " + date.toString() + "no date matching found.");

        if(!(this.availableEmployees.get(date) instanceof EnumMap))
            throw new IllegalArgumentException("Error! failed to remove availability for shift " + date.toString() + " value for key is not an EnumMap!");

        if(!this.availableEmployees.get(date).containsKey(time))
            throw new IllegalArgumentException("failed to remove availability for shift " + date.toString() + " " + time.toString() + " no time matching found.");

        for(int i = 0; i < this.availableEmployees.get(date).get(time).size(); i++){
            if(this.availableEmployees.get(date).get(time).get(i).equals(eId)){
                this.availableEmployees.get(date).get(time).remove(i);
                return;
            }
        }
    }

    public void addEmployeeToShift(String eId , LocalDate date, ShiftTime time, Role role){
        if(!this.shifts.containsKey(date) || !this.shifts.get(date).containsKey(time))
            throw new IllegalArgumentException("Shift does not exist");
        if(!this.availableEmployees.containsKey(date) || !this.availableEmployees.get(date).containsKey(time))
            throw new IllegalArgumentException("Employee did not submit availability for this shift");
        if(!this.availableEmployees.get(date).get(time).contains(eId))
            throw new IllegalArgumentException("Employee did not submit availability for this shift");
            
        this.shifts.get(date).get(time).addEmployee(eId, role);
    }

    public void removeEmployeeFromShift(String eId , LocalDate date, ShiftTime time, Role role){ 
        if(!this.shifts.containsKey(date) || !this.shifts.get(date).containsKey(time))
            throw new IllegalArgumentException("Shift does not exist");
        this.shifts.get(date).get(time).removeEmployee(eId, role);
    }

    public void setLastDateForSubmitting(LocalDate date){
        if(date.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("cannot submit date from the past");
        this.lastDateForSubmitting = date;
    }




    public EnumMap<Role , ArrayList<String>> getEmployeesForShift(LocalDate date, ShiftTime time){
        if(!this.shifts.containsKey(date) || !this.shifts.get(date).containsKey(time))
            throw new IllegalArgumentException("Shift does not exist");
        return this.shifts.get(date).get(time).getEmployeeIds();
    }

    public EnumMap<Role , Integer> getRequiredEmployeesForShift(LocalDate date, ShiftTime time){
        if(!this.shifts.containsKey(date) || !this.shifts.get(date).containsKey(time))
            throw new IllegalArgumentException("Shift does not exist");
        return this.shifts.get(date).get(time).getRequiredEmployees();
    }

    public ArrayList<String> getAvailableEmployeesForShift(LocalDate date, ShiftTime time){
        if(!this.availableEmployees.containsKey(date) || !this.availableEmployees.get(date).containsKey(time))
            throw new IllegalArgumentException("Shift does not exist");
        return new ArrayList<String> (this.availableEmployees.get(date).get(time));
    }


}