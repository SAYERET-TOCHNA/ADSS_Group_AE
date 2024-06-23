package org.example.Business;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import org.example.Business.Enums.EmploymentType;
import org.example.Business.Enums.Role;
import org.example.Business.Enums.ShiftTime;
import org.example.DataAccess.EmployeeControllerDao;
import org.example.Utilities.Trio;
//TODO: change to EmployeeController - move to Business
// the EmpManager class manages the employees in the company
public class EmployeeController {

    private int branchId;

    private HashMap<String, Employee> employees;

    private EmployeeControllerDao dao;

    //------------------- construction -------------------

    public EmployeeController(int branchId) {
        this.branchId=branchId;
        employees = new HashMap<String, Employee>();
        this.dao = new EmployeeControllerDao(branchId);
    }

    public static EmployeeController loadEmployeeControllerFromDB(int branchId){
        return new EmployeeController(branchId, true);
    }

    private EmployeeController(int branchId, boolean discrimnator){
        this.branchId = branchId;
        this.dao = new EmployeeControllerDao(branchId);
        this.employees = dao.loadEmployees();
    }

    //------------------- methods -------------------
    
    public Employee addEmployee(String name, String id, int branchId, EmploymentType employmentType, int salary, String bankAccountId){
        if(this.employees.containsKey(id)){
            throw new IllegalArgumentException("Employee with id " + id + " already exists");
        }
        else{
            Employee newEmployee = Employee.createEmployee(name, id, branchId, employmentType, salary, bankAccountId);
            this.employees.put(id, newEmployee);
            this.dao.addEmployee(id, name, newEmployee.getPassword(), employmentType.ordinal() , salary, bankAccountId, LocalDate.now());
            return newEmployee;
        }
    }

    public void addEmployee(Employee employee){
        if(this.employees.containsKey(employee.getId())){
            throw new IllegalArgumentException("Employee with id " + employee.getId() + " already exists");
        }
        else{
            this.employees.put(employee.getId(), employee);
            this.dao.addEmployee(employee.getId(), employee.getName(), employee.getPassword(), employee.getEmploymentType().ordinal(), employee.getSalary(), employee.getBankAccountId(), employee.getStartDate());
        }
    }

    public Employee removeEmployee(String id)
    {
        if(!this.employees.containsKey(id)){
            throw new IllegalArgumentException("Employee with id " + id + " doesn't exists");
        }
        else{
            this.dao.removeEmployee(id);
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
        this.dao.assignRoleToEmployee(employeeId, role);
        this.employees.get(employeeId).addRole(role);
    }

    public void setPassword(String employeeId, String newPassword) {
        if(!this.employees.containsKey(employeeId)){
            throw new IllegalArgumentException("Employee with id " + employeeId + " does not exist");
        }
        this.employees.get(employeeId).setPassword(newPassword);
        this.dao.setEmployeePassword(employeeId, newPassword);
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
        this.employees.get(employeeId).addToShift(date, time, role);
    }

    public void removeShift(String employeeId, LocalDate date, ShiftTime time) {
        if(!this.employees.containsKey(employeeId)){
            throw new IllegalArgumentException("Employee with id " + employeeId + " does not exist");
        }
        this.employees.get(employeeId).removeFromShift(date, time);
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

    // data loading
    public String loadHRMId()
    {
        return this.dao.loadHRMId();
    }




}
