package org.example;
import java.util.ArrayList;
import java.util.Scanner;

import org.example.Business.Employee;
import org.example.Service.Branch;
import org.example.Business.Enums.EmploymentType;
import org.example.Business.Enums.Role;


public class Main {

    private static int loggedInBranchId = -1;
    private static String loggedInUserId = "";

    public static void main(String[] args) {
        
        /// create 9 branches
        ArrayList<Employee> employees = new ArrayList<Employee>();
        ArrayList<Branch> branches = new ArrayList<Branch>();
        for(int i = 0; i < 9; i++){
            Employee employee = Employee.createEmployee("name"+i, ""+i, i, EmploymentType.FULL_TIME, 25000, "bankAccountId"+i);
            employee.addRole(Role.HR_MANAGER);
            employees.add(employee);
            Branch branch = new Branch(employees.get(i));
        }

        
        
    }

    public static v logIn()
    {
        Scanner k = new Scanner(System.in);
        // choose a branch
        while(true){
            System.out.println("Choose a branch to log in to (0-8): ");
            int branchId = k.nextInt();
            if(branchId < 0 || branchId > 8){
                System.out.println("Invalid branch id");
            }  
        }

        // choose an employee
        while


    }
}