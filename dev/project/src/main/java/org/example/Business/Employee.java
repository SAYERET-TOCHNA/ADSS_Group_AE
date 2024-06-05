package org.example.Business;

import java.time.LocalDate;
import java.util.ArrayList;
// enumset docs: https://docs.oracle.com/javase/8/docs/api/java/util/EnumSet.html
import java.util.EnumSet;


import org.example.Business.Enums.EmploymentType;
import org.example.Business.Enums.Role;

public class Employee {

    protected String name;
    protected String id;
    protected int branchId;
    protected EmploymentType employmentType;
    protected EnumSet<Role> roles;
    protected int salary;
    protected String bankAccountId;
    protected LocalDate startDate;


    
    //------------------- construction (factory) -------------------

    private Employee(String name, String id, int branchId ,EmploymentType employmentType, int salary, String bankAccountId) {
        this.name = name;
        this.id = id;
        this.branchId = branchId;
        this.employmentType = employmentType;
        this.roles = EnumSet.noneOf(Role.class);
        this.salary = salary;
        this.bankAccountId = bankAccountId;
    }
    private Employee(){}


    public static Employee createEmployee(String name, String id, int branchId, EmploymentType employmentType, int salary, String bankAccountId){
        if(isLegalId(id) && isLegalId(bankAccountId) && salary >= 0){
            return new Employee(name, id, branchId, employmentType, salary, bankAccountId);
        }
        else{
            throw new IllegalArgumentException("Illegal Employee id, bank account id or salary");
        }
    }

    //------------------- getter & setters -------------------

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public int getSalary() {
        return salary;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public void setSalary(int salary) {
        if(salary > 0)
            this.salary = salary;
        else throw new IllegalArgumentException("Salary must be positive");
    }

    public void setBankAccountId(String bankAccountId) {
        if(isLegalId(bankAccountId))
            this.bankAccountId = bankAccountId;
        else throw new IllegalArgumentException("Illegal bank account id");
    }

    public ArrayList<Role> getRoles() {
        return new ArrayList<Role>(roles);
    }

    //------------------------ Methods -------------------------

    public void addRole(Role role){
        this.roles.add(role);
    }   

    public boolean isSuitableForRole(Role role){
        return this.roles.contains(role);
    }

    // checks if a given string is a legal id
    private static boolean isLegalId(String id){
        for(int i = 0 ; i < id.length(); i++){
            if(id.charAt(i) < '0' || id.charAt(i) > '9'){
                return false;
            }
        }
        return true;
    }


}