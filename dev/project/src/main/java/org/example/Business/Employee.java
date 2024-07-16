package org.example.Business;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;

import org.example.Business.Enums.EmploymentType;
import org.example.Business.Enums.Role;
import org.example.Business.Enums.ShiftTime;
import org.example.Utilities.Trio;

public class Employee {

    protected String name;
    protected String id;
    protected String password;
    protected int branchId;
    protected EmploymentType employmentType;
    protected EnumSet<Role> roles;
    protected int salary;
    protected String bankAccountId;
    protected LocalDate startDate;
    protected ArrayList<Trio<LocalDate , ShiftTime, Role>> shifts; 

    
    //------------------- construction (factory) -------------------

    private Employee(String name, String id, int branchId ,EmploymentType employmentType, int salary, String bankAccountId) {
        this.name = name;
        this.id = id;
        this.password = id;
        this.branchId = branchId;
        this.employmentType = employmentType;
        this.roles = EnumSet.noneOf(Role.class);
        this.salary = salary;
        this.bankAccountId = bankAccountId;
        this.startDate = LocalDate.now();
        this.shifts = new ArrayList<Trio<LocalDate , ShiftTime, Role>>();
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

    //------------------- loading from db -------------------

    public static Employee loadEmployee(String name, String id, int branchId, EmploymentType employmentType, String password, int salary, String bankAccountId, LocalDate startDate, ArrayList<Trio<LocalDate , ShiftTime, Role>> shifts, EnumSet<Role> roles){
        if(isLegalId(id) && isLegalId(bankAccountId) && salary >= 0){
            return new Employee(name, id, branchId, employmentType, password, salary, bankAccountId, startDate, shifts, roles);
        }
        else{
            throw new IllegalArgumentException("Illegal Employee id, bank account id or salary when loading from db!! for id: " + id);
        }
    }

    private Employee(String name, String id, int branchId, EmploymentType employmentType, String password, int salary, String bankAccountId, LocalDate startDate, ArrayList<Trio<LocalDate , ShiftTime, Role>> shifts, EnumSet<Role> roles){
        this.name = name;
        this.id = id;
        this.password = password;
        this.branchId = branchId;
        this.employmentType = employmentType;
        this.roles = EnumSet.noneOf(Role.class);
        this.salary = salary;
        this.bankAccountId = bankAccountId;
        this.startDate = startDate;
        this.shifts = shifts;
        this.roles=roles;
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

    public void setPassword(String password) {
        this.password = password;

    }

    public boolean checkPassword(String password){
        return this.password.equals(password);
    }

    public String getPassword(){
        return this.password;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public int getBranchId() {
        return branchId;
    }

    public void addToShift(LocalDate date, ShiftTime time, Role role){ 
        if(isInShift(date, time))
            throw new IllegalArgumentException("Employee is already in shift at this time");
        this.shifts.add(new Trio<LocalDate, ShiftTime, Role>(date, time, role));
        this.shifts.sort((a,b) -> a.getFirst().compareTo(b.getFirst()));
    }

    public boolean isInShift(LocalDate date, ShiftTime time){
        for(Trio<LocalDate, ShiftTime, Role> shift : this.shifts){
            if(shift.getFirst().equals(date) && shift.getSecond().equals(time)){
                return true;
            }
        }
        return false;
    }

    public void removeFromShift(LocalDate date, ShiftTime time){
        for(Trio<LocalDate, ShiftTime, Role> shift : this.shifts){
            if(shift.getFirst().equals(date) && shift.getSecond().equals(time)){
                this.shifts.remove(shift);
            }
        }
    }

    public ArrayList<Trio<LocalDate,ShiftTime,Role>> getShifts(){
        return new ArrayList<Trio<LocalDate,ShiftTime,Role>>(this.shifts);
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
