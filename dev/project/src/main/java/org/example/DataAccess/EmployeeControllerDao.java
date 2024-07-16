package org.example.DataAccess;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import org.example.Business.Employee;
import org.example.Business.Enums.Role;

public class EmployeeControllerDao {

    private int branchId;
    private DBObj dbObj;

    public EmployeeControllerDao(int branchId) {
        this.branchId = branchId;
        this.dbObj = DBObj.getInstance();
    }

    // CRUD operations

    // create

    public void addEmployee(String id, String name, String password, int employmentType, int salary, String bankAccountId, LocalDate startDate){
        this.dbObj.addEmployee(id, name, password, employmentType, salary, bankAccountId, this.branchId, startDate);
    }

    public void assignRoleToEmployee(String id, Role role){
        this.dbObj.assignNewRoleToEmployee(id, role);
    }

    // read

    public HashMap<String, Employee>  loadEmployees(){
        HashMap<String, Employee> employees = new HashMap<String, Employee>();
        ArrayList<Employee> employeesList = this.dbObj.loadEmployees(this.branchId);
        for(Employee e : employeesList){
            employees.put(e.getId(), e);
        }
        return employees;
    }

    public String loadHRMId(){
        return this.dbObj.loadHRMId(this.branchId);
    }

    // update

    public void setEmployeePassword(String eId, String password){
        this.dbObj.setEmployeePassword(eId, password);
    }

    // delete

    public void removeEmployee(String eId){
        this.dbObj.removeEmployee(eId);
    }


}
