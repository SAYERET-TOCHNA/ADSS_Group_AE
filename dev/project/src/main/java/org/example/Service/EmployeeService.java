package org.example.Service;

import java.util.HashMap;

import org.example.Business.Employee;

import org.example.Business.Enums.EmploymentType;
import org.example.Business.Enums.Role;

// the EmpManager class manages the employees in the company
public class EmployeeService {

    private HashMap<String, Employee> employees;

    //------------------- construction -------------------

    public EmployeeService() {
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




}
