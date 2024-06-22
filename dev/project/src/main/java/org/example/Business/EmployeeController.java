package org.example.Business;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import org.example.Business.Enums.EmploymentType;
import org.example.Business.Enums.Role;
import org.example.Business.Enums.ShiftTime;
import org.example.Utilities.Trio;
//TODO: change to EmployeeController - move to Business
// the EmpManager class manages the employees in the company
public class EmployeeController {

    private HashMap<String, Employee> employees;

    //------------------- construction -------------------

    public EmployeeController() {
        employees = new HashMap<String, Employee>();
    }

    //------------------- methods -------------------
    
    public Employee addEmployee(String name, String id, int branchId, EmploymentType employmentType, int salary, String bankAccountId){
        if(this.employees.containsKey(id)){
            throw new IllegalArgumentException("Employee with id " + id + " already exists");
        }
        else{
            Employee newEmployee = Employee.createEmployee(name, id, branchId, employmentType, salary, bankAccountId);
            this.employees.put(id, newEmployee);
            return newEmployee;
        }
    }

    public void addEmployee(Employee employee){
        if(this.employees.containsKey(employee.getId())){
            throw new IllegalArgumentException("Employee with id " + employee.getId() + " already exists");
        }
        else{
            this.employees.put(employee.getId(), employee);
        }
    }

    public Employee removeEmployee(String id)
    {
        if(!this.employees.containsKey(id)){
            throw new IllegalArgumentException("Employee with id " + id + " doesn't exists");
        }
        else{
            return this.employees.remove(id);
        }
    }

    public boolean hasEmlpoyee(String id){
        return this.employees.containsKey(id);
    }

    public Employee getEmployee(String id){
        if(!this.employees.containsKey(id)){
            throw new IllegalArgumentException("Employee with id " + id + " does not exist");
        }
        return this.employees.get(id);
    }

    public void assignRoleToEmployee(String employeeId, Role role) {
        if(!this.employees.containsKey(employeeId)){
            throw new IllegalArgumentException("Employee with id " + employeeId + " does not exist");
        }
        this.employees.get(employeeId).addRole(role);
    }

    public void setPassword(String employeeId, String newPassword) {
        if(!this.employees.containsKey(employeeId)){
            throw new IllegalArgumentException("Employee with id " + employeeId + " does not exist");
        }
        this.employees.get(employeeId).setPassword(newPassword);
    }

    public boolean checkPassword(String employeeId, String password) {
        if(!this.employees.containsKey(employeeId)){
            throw new IllegalArgumentException("Employee with id " + employeeId + " does not exist");
        }
        return this.employees.get(employeeId).checkPassword(password);
    }

    public void setSalary(String employeeId, int newSalary) {
        if(!this.employees.containsKey(employeeId)){
            throw new IllegalArgumentException("Employee with id " + employeeId + " does not exist");
        }
        this.employees.get(employeeId).setSalary(newSalary);
    }

    public void setBankAccountId(String employeeId, String newBankAccountId) {
        if(!this.employees.containsKey(employeeId)){
            throw new IllegalArgumentException("Employee with id " + employeeId + " does not exist");
        }
        this.employees.get(employeeId).setBankAccountId(newBankAccountId);
    }

    public boolean isSuitableForRole(String employeeId, Role role) {
        if(!this.employees.containsKey(employeeId)){
            throw new IllegalArgumentException("Employee with id " + employeeId + " does not exist");
        }
        return this.employees.get(employeeId).isSuitableForRole(role);
    }

    public void addShift(String employeeId, LocalDate date, ShiftTime time, Role role) {
        if(!this.employees.containsKey(employeeId)){
            throw new IllegalArgumentException("Employee with id " + employeeId + " does not exist");
        }
        this.employees.get(employeeId).addShift(date, time, role);
    }

    public void removeShift(String employeeId, LocalDate date, ShiftTime time) {
        if(!this.employees.containsKey(employeeId)){
            throw new IllegalArgumentException("Employee with id " + employeeId + " does not exist");
        }
        this.employees.get(employeeId).removeShift(date, time);
    }

    public boolean isInShift(String employeeId, LocalDate date, ShiftTime time) {
        if(!this.employees.containsKey(employeeId)){
            throw new IllegalArgumentException("Employee with id " + employeeId + " does not exist");
        }
        return this.employees.get(employeeId).isInShift(date, time);
    }

    public ArrayList<Trio<LocalDate,ShiftTime,Role>> getShifts(String employeeId) {
        if(!this.employees.containsKey(employeeId)){
            throw new IllegalArgumentException("Employee with id " + employeeId + " does not exist");
        }
        return this.employees.get(employeeId).getShifts();
    }




}
