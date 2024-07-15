package org.example.Business;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

import org.example.Business.Enums.EmploymentType;
import org.example.Business.Enums.Role;
import org.example.Business.Enums.ShiftTime;
import org.example.DataAccess.BranchControllerDao;
import org.example.Utilities.Trio;

public class BranchController {

    private HashMap<Integer, Branch> branches;
    BranchControllerDao dao;

    private static BranchController instance;
//--------------------------- Construction (Singleton) ---------------------------

    private BranchController() {
        branches = new HashMap<>();
        dao = new BranchControllerDao();
    }

    public static BranchController getInstance(){
        if(instance==null)
            instance = new BranchController();
        return instance;
    }
//--------------------------- loading from DB -------------------------
// load only the branches that are needed since a Branch object contains a lot of data




    private void loadBranchFromDB(int branchId){
        Branch branch = dao.getBranch(branchId);
        branches.put(branchId, branch);
    }

    // removes only pointer to the branch
    // Branch object will be removed from memory by java's garbage collector
    private void offLoadBranchFromMemory(int branchId){
        branches.remove(branchId);
        System.gc();
    }

    public ArrayList<Integer> loadBranchIds(){
        return dao.loadBranchIds();
    }
//--------------------------- Methods ---------------------------------

    public void logInBranch(int branchId){
        this.loadBranchFromDB(branchId);
    }

    public void logOutBranch(int branchId){
        this.offLoadBranchFromMemory(branchId);
    }

    public Employee addNewEmployee(String name, String eId, int branchId, EmploymentType employmentType, int salary, String bankAccountId, String requestingUserId){
        Branch branch = branches.get(branchId);
        if(branch == null){
            branch = dao.getBranch(branchId);
            branches.put(branchId, branch);
        }
        return branch.addNewEmployee(name, eId, branchId, employmentType, salary, bankAccountId, requestingUserId);
    }

    public Employee removeEmployee(String eId, String requestingUserId, int branchId){
        Branch branch = branches.get(branchId);
        if(branch == null){
            branch = dao.getBranch(branchId);
            branches.put(branchId, branch);
        }
        return branch.removeEmployee(eId, requestingUserId);
    }

    public void setEmployeePassword(String eId, String oldPassword, String newPassword, int branchId){
        Branch branch = branches.get(branchId);
        if(branch == null){
            branch = dao.getBranch(branchId);
            branches.put(branchId, branch);
        }
        branch.setEmployeePassword(eId, oldPassword, newPassword);
    }

    public Employee logInEmployee(String name, String eId, String password, int branchId){
        Branch branch = branches.get(branchId);
        if(branch == null){
            branch = dao.getBranch(branchId);
            branches.put(branchId, branch);
        }
        return branch.logIn(name, eId, password);
    }

    public void logOut(String eId, int branchId){
        Branch branch = branches.get(branchId);
        if(branch == null){
            branch = dao.getBranch(branchId);
            branches.put(branchId, branch);
        }
        branch.logOut(eId);
        // memory management
        if(branch.getLoggedInEmployees().size() == 0){
            this.offLoadBranchFromMemory(branchId);
        }
    }

    public boolean isEmployeeLoggedIn(String eId, int branchId){
        Branch branch = branches.get(branchId);
        if(branch == null){
            branch = dao.getBranch(branchId);
            branches.put(branchId, branch);
        }
        return branch.isEmployeeLoggedIn(eId);
    }

    public void addShift(LocalDate date, ShiftTime time, boolean hasDelivery, EnumMap<Role,Integer> requiredEmployees, String requestingUserId, int branchId){
        Branch branch = branches.get(branchId);
        if(branch == null){
            branch = dao.getBranch(branchId);
            branches.put(branchId, branch);
        }
        branch.addShift(date, time, hasDelivery, requiredEmployees, requestingUserId);
    }

    public void removeShift(LocalDate date, ShiftTime time, String requestingUserId, int branchId){
        Branch branch = branches.get(branchId);
        if(branch == null){
            branch = dao.getBranch(branchId);
            branches.put(branchId, branch);
        }
        branch.removeShift(date, time, requestingUserId);
    }

    public void submitAvailability(String eId, LocalDate date, ShiftTime time, int branchId){
        Branch branch = branches.get(branchId);
        if(branch == null){
            branch = dao.getBranch(branchId);
            branches.put(branchId, branch);
        }
        branch.submitAvailability(eId, date, time);
    }

    public void removeAvailability(String eId, LocalDate date, ShiftTime time, int branchId){
        Branch branch = branches.get(branchId);
        if(branch == null){
            branch = dao.getBranch(branchId);
            branches.put(branchId, branch);
        }
        branch.removeAvailability(eId, date, time);
    }

    public void addEmployeeToShift(String eId, LocalDate date, ShiftTime time, Role role, String requestingUserid, int branchId){
        Branch branch = branches.get(branchId);
        if(branch == null){
            branch = dao.getBranch(branchId);
            branches.put(branchId, branch);
        }
        branch.addEmployeeToShift(eId, date, time, role, requestingUserid);
    }

    public void removeEmployeeFromShift(String eId, LocalDate date, ShiftTime time, Role role, String requestingUserid, int branchId){
        Branch branch = branches.get(branchId);
        if(branch == null){
            branch = dao.getBranch(branchId);
            branches.put(branchId, branch);
        }
        branch.removeEmployeeFromShift(eId, date, time, role, requestingUserid);
    }

    public void assignRoleToEmployee(String eId, Role role, String requestingUserId, int branchId){
        Branch branch = branches.get(branchId);
        if(branch == null){
            branch = dao.getBranch(branchId);
            branches.put(branchId, branch);
        }
        branch.assignRoleToEmployee(eId, role, requestingUserId);
    }

    public void setLastDateForSubmitting(LocalDate date, String requestingUserId, int branchId){
        Branch branch = branches.get(branchId);
        if(branch == null){
            branch = dao.getBranch(branchId);
            branches.put(branchId, branch);
        }
        branch.setLastDateForSubmitting(date, requestingUserId);
    }

    public String getEmployeesForShiftStr(LocalDate date, ShiftTime time, String requestingUserId, int branchId){
        Branch branch = branches.get(branchId);
        if(branch == null){
            branch = dao.getBranch(branchId);
            branches.put(branchId, branch);
        }
        return branch.getEmployeesForShiftStr(date, time, requestingUserId);
    }

    public String getRequiredEmployeesForShiftStr(LocalDate date, ShiftTime time, String requestingUserId, int branchId){
        Branch branch = branches.get(branchId);
        if(branch == null){
            branch = dao.getBranch(branchId);
            branches.put(branchId, branch);
        }
        return branch.getRequiredEmployeesForShiftStr(date, time, requestingUserId);
    }

    public String getAvailableEmployeesForShiftStr(LocalDate date, ShiftTime time, String requestingUserId, int branchId){
        Branch branch = branches.get(branchId);
        if(branch == null){
            branch = dao.getBranch(branchId);
            branches.put(branchId, branch);
        }
        return branch.getAvailableEmployeesForShiftStr(date, time, requestingUserId);
    }

    public ArrayList<Trio<LocalDate,ShiftTime,Role>> getShiftsForEmployee(String eId, int branchId){
        Branch branch = branches.get(branchId);
        if(branch == null){
            branch = dao.getBranch(branchId);
            branches.put(branchId, branch);
        }
        return branch.getShiftsForEmployee(eId);
    }

    public int wareHouseQualification(String eid, int branchId) {
        Branch branch = branches.get(branchId);
        if (branch == null) {
            branch = dao.getBranch(branchId);
            branches.put(branchId, branch);
        }
        return branch.wareHouseQualification(eid);
    }


    
}
