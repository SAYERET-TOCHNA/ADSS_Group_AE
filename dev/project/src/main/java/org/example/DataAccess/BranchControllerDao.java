package org.example.DataAccess;

import java.util.ArrayList;

import org.example.Business.Branch;

public class BranchControllerDao {

    private DBObj dbObj;

    public BranchControllerDao() {
        this.dbObj = DBObj.getInstance();
    }

    // CRUD Operations

    // create

    // read

    public Branch getBranch(int branchId){
        Branch b = Branch.loadBranchFromDB(branchId);
        return b;
    }
    
    public ArrayList<Integer> loadBranchIds(){
        return this.dbObj.loadBranchIds();
    }
    // update

    // delete

    

}
