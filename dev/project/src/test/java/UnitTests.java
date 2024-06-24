import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;

import org.example.Business.Employee;
import org.example.Business.EmployeeController;
import org.example.Business.Enums.EmploymentType;
import org.example.Business.Enums.Role;
import org.example.Business.Enums.ShiftTime;
import org.example.Business.ShiftController;

public class UnitTests {

    private static ShiftController shiftC = new ShiftController(1);
    private static EmployeeController employeeC = new EmployeeController(1);

    public static void main(String[] args) {
        testAddValidShift();
        testAddShiftAlreadyExists();
        testAddShiftWithPastDate();
        testRemoveNonExistentShift();
        testAddAvailabilityBeforeDeadline();
        testAddAvailabilityAfterDeadline();
        testRemoveAvailability();
        testAddEmployeeToShift();
        testAddEmployeeToNonexistentShift();
        testRemoveEmployeeFromShift();
        
        testGetRequiredEmployeesForShift();
        
        testGetAvailableEmployeesForShift();
        
        testGetEmployeesForShift();
        testCreateEmployee();
        testDoesShiftExist();
        testDoesShiftNotExist();

        testAddShiftWithEmptyRequiredEmployees();
        testAddShiftWithNegativeRequiredEmployees();
        testShiftWithDeliveryRequiresDriver();
        testShiftWithDeliveryRequiresWareHouse();
        
        
        
    }

    private static void assertCondition(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
        System.out.println("Test Passed: " + message);
    }

    public static void testAddValidShift() {
        try {
            EnumMap<Role, Integer> requiredEmployees = new EnumMap<>(Role.class);
            requiredEmployees.put(Role.SHIFT_MANAGER, 1);
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            shiftC.addShift(tomorrow, ShiftTime.MORNING, false, requiredEmployees);
            assertCondition(shiftC.getRequiredEmployeesForShift(tomorrow, ShiftTime.MORNING) != null, "Valid Shift Added");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testAddShiftAlreadyExists() {
        LocalDate date = LocalDate.now().plusDays(20);
        EnumMap<Role, Integer> roles = new EnumMap<>(Role.class);
        roles.put(Role.SHIFT_MANAGER, 1);
        shiftC.addShift(date, ShiftTime.MORNING,false, roles);
        try {
            shiftC.addShift(date, ShiftTime.MORNING,false, roles);
            assertCondition(false, "Shift Already Exists - should have thrown an exception");
        } catch (IllegalArgumentException e) {
            assertCondition(true, "Correctly caught duplicate shift addition");
        }
    }

    public static void testAddShiftWithPastDate() {
        try {
            LocalDate yesterday = LocalDate.now().minusDays(19);
            EnumMap<Role, Integer> roles = new EnumMap<>(Role.class);
            roles.put(Role.SHIFT_MANAGER, 1);
            shiftC.addShift(yesterday, ShiftTime.MORNING,false, roles);
            assertCondition(false, "Shift with Past Date - should have thrown an exception");
        } catch (IllegalArgumentException e) {
            assertCondition(true, "Correctly caught addition of a shift with a past date");
        }
    }

    /* 
    public static void testRemoveExistingShift() {
        LocalDate date = LocalDate.now().plusDays(3);
        EnumMap<Role, Integer> roles = new EnumMap<>(Role.class);
        roles.put(Role.SHIFT_MANAGER, 1);
        service.addShift(date, ShiftTime.MORNING, roles);
        service.removeShift(date, ShiftTime.MORNING);
        try {
            assertCondition(service.doesShiftExist(date, ShiftTime.MORNING), "Shift Successfully Removed");
        } catch (Exception e) {
            assertCondition(false, "Failed to remove an existing shift"+e.getMessage());
        }
    }
        */

    public static void testCreateEmployee(){
        try{
        String employeeId = "2222222";
        String name = "John";
        Employee e = employeeC.addEmployee(name, employeeId, 1, EmploymentType.FULL_TIME, 1000, "123456");
        assertCondition(employeeC.hasEmlpoyee(employeeId), "Employee Created");
        }catch(Exception e){
            assertCondition(false, "Failed to create employee");
        }
    }
    public static void testRemoveNonExistentShift() {
        LocalDate date = LocalDate.now().plusDays(2000);
        try {
            shiftC.removeShift(date, ShiftTime.MORNING);
            assertCondition(false, "Non-existent Shift Removed - should have thrown an exception");
        } catch (IllegalArgumentException e) {
            assertCondition(true, "Correctly caught attempt to remove a non-existent shift");
        }
    }

    public static void testAddAvailabilityBeforeDeadline() {
        String employeeId = "123";
        LocalDate tomorrow = LocalDate.now().plusDays(2);
        shiftC.setLastDateForSubmitting(tomorrow.plusDays(1));
        try {
            shiftC.addAvailability(employeeId, tomorrow, ShiftTime.MORNING);
            assertCondition(shiftC.getAvailableEmployeesForShift(tomorrow, ShiftTime.MORNING).contains(employeeId), "Availability Added Before Deadline");
        } catch (IllegalArgumentException e) {
            assertCondition(false, "Failed to add availability before deadline");
        }
    }

    public static void testAddAvailabilityAfterDeadline() {
        String employeeId = "123";
        LocalDate tomorrow = LocalDate.now().plusDays(3);
        shiftC.setLastDateForSubmitting(LocalDate.now());
        try {
            shiftC.addAvailability(employeeId, tomorrow, ShiftTime.MORNING);
            assertCondition(false, "Availability Added After Deadline - should have thrown an exception");
        } catch (IllegalArgumentException e) {
            assertCondition(true, "Correctly caught addition of availability after deadline");
        }
    }

    public static void testRemoveAvailability() {
        String employeeId = "456";
        LocalDate tomorrow = LocalDate.now().plusDays(4);
        shiftC.setLastDateForSubmitting(tomorrow.plusDays(4));
        shiftC.addAvailability(employeeId, tomorrow, ShiftTime.MORNING);
        try {
            shiftC.removeAvailability(employeeId, tomorrow, ShiftTime.MORNING);
            assertCondition(!shiftC.getAvailableEmployeesForShift(tomorrow, ShiftTime.MORNING).contains(employeeId), "Availability Successfully Removed");
        } catch (IllegalArgumentException e) {
            assertCondition(false, "Failed to remove availability");
        }
    }


    public static void testAddEmployeeToShift() {
        String employeeId = "789";
        LocalDate tomorrow = LocalDate.now().plusDays(5);
        EnumMap<Role, Integer> roles = new EnumMap<>(Role.class);
        roles.put(Role.SHIFT_MANAGER, 1);
        shiftC.addShift(tomorrow, ShiftTime.MORNING,false, roles);
        shiftC.setLastDateForSubmitting(tomorrow.plusDays(20) );
        shiftC.addAvailability(employeeId, tomorrow, ShiftTime.MORNING);
        try {
            shiftC.addEmployeeToShift(employeeId, tomorrow, ShiftTime.MORNING, Role.SHIFT_MANAGER);
            assertCondition(shiftC.getEmployeesForShift(tomorrow, ShiftTime.MORNING).get(Role.SHIFT_MANAGER).contains(employeeId), "Employee Added to Shift");
        } catch (IllegalArgumentException e) {
            assertCondition(false, "Failed to add employee to shift");
        }
    }

    public static void testAddEmployeeToNonexistentShift() {
        String employeeId = "789";
        LocalDate tomorrow = LocalDate.now().plusDays(6);
        try {
            shiftC.addEmployeeToShift(employeeId, tomorrow, ShiftTime.MORNING, Role.SHIFT_MANAGER);
            assertCondition(false, "Employee Added to Nonexistent Shift - should have thrown an exception");
        } catch (IllegalArgumentException e) {
            assertCondition(true, "Correctly caught addition of employee to non-existent shift");
        }
    }

    public static void testRemoveEmployeeFromShift() {
        String employeeId = "1011";
        LocalDate tomorrow = LocalDate.now().plusDays(7);
        EnumMap<Role, Integer> roles = new EnumMap<>(Role.class);
        roles.put(Role.SHIFT_MANAGER, 1);
        shiftC.addShift(tomorrow, ShiftTime.MORNING,false, roles);
        shiftC.setLastDateForSubmitting(tomorrow.plusDays(34));
        shiftC.addAvailability(employeeId, tomorrow, ShiftTime.MORNING);
        shiftC.addEmployeeToShift(employeeId, tomorrow, ShiftTime.MORNING, Role.SHIFT_MANAGER);
        try {
            shiftC.removeEmployeeFromShift(employeeId, tomorrow, ShiftTime.MORNING, Role.SHIFT_MANAGER);
            assertCondition(!shiftC.getEmployeesForShift(tomorrow, ShiftTime.MORNING).get(Role.SHIFT_MANAGER).contains(employeeId), "Employee Removed from Shift");
        } catch (IllegalArgumentException e) {
            assertCondition(false, "Failed to remove employee from shift");
        }
    }

    public static void testRemoveEmployeeFromNonexistentShift() {
        String employeeId = "1011";
        LocalDate tomorrow = LocalDate.now().plusDays(8);
        try {
            shiftC.removeEmployeeFromShift(employeeId, tomorrow, ShiftTime.MORNING, Role.SHIFT_MANAGER);
            assertCondition(false, "Employee Removed from Nonexistent Shift - should have thrown an exception");
        } catch (IllegalArgumentException e) {
            assertCondition(true, "Correctly caught removal of employee from non-existent shift");
        }
    }

    public static void testGetRequiredEmployeesForShift() {
        LocalDate tomorrow = LocalDate.now().plusDays(9);
        EnumMap<Role, Integer> requiredEmployees = new EnumMap<>(Role.class);
        requiredEmployees.put(Role.SHIFT_MANAGER, 1);
        shiftC.addShift(tomorrow, ShiftTime.MORNING, false, requiredEmployees);
        EnumMap<Role, Integer> retrievedRequiredEmployees = shiftC.getRequiredEmployeesForShift(tomorrow, ShiftTime.MORNING);
        assertCondition(retrievedRequiredEmployees != null && retrievedRequiredEmployees.containsKey(Role.SHIFT_MANAGER), "Retrieved Required Employees for Shift");
    }

    public static void testGetAvailableEmployeesForShift() {
        String employeeId = "123";
        LocalDate tomorrow = LocalDate.now().plusDays(10);
        shiftC.setLastDateForSubmitting(tomorrow.plusDays(1));
        shiftC.addAvailability(employeeId, tomorrow, ShiftTime.MORNING);
        ArrayList<String> availableEmployees = shiftC.getAvailableEmployeesForShift(tomorrow, ShiftTime.MORNING);
        assertCondition(availableEmployees != null && availableEmployees.contains(employeeId), "Retrieved Available Employees for Shift");
    }

    public static void testGetEmployeesForShift() {
        String employeeId = "789";
        LocalDate tomorrow = LocalDate.now().plusDays(11);
        EnumMap<Role, Integer> roles = new EnumMap<>(Role.class);
        roles.put(Role.SHIFT_MANAGER, 1);
        shiftC.addShift(tomorrow, ShiftTime.MORNING, false, roles);
        shiftC.setLastDateForSubmitting(tomorrow.plusDays(20));
        shiftC.addAvailability(employeeId, tomorrow, ShiftTime.MORNING);
        shiftC.addEmployeeToShift(employeeId, tomorrow, ShiftTime.MORNING, Role.SHIFT_MANAGER);
        EnumMap<Role, ArrayList<String>> employeesForShift = shiftC.getEmployeesForShift(tomorrow, ShiftTime.MORNING);
        assertCondition(employeesForShift != null && employeesForShift.get(Role.SHIFT_MANAGER).contains(employeeId), "Retrieved Employees for Shift");
    }

    public static void testDoesShiftExist() {
        LocalDate date = LocalDate.now().plusDays(12);
        EnumMap<Role, Integer> roles = new EnumMap<>(Role.class);
        roles.put(Role.SHIFT_MANAGER, 1);
        shiftC.addShift(date, ShiftTime.MORNING, false, roles);
        boolean shiftExists = shiftC.doesShiftExist(date, ShiftTime.MORNING);
        assertCondition(shiftExists, "Shift Exists");
    }

    public static void testDoesShiftNotExist() {
        LocalDate date = LocalDate.now().plusDays(13);
        boolean shiftExists = shiftC.doesShiftExist(date, ShiftTime.MORNING);
        assertCondition(!shiftExists, "Shift Does Not Exist");
    }

    public static void testAddShiftWithEmptyRequiredEmployees() {
        LocalDate tomorrow = LocalDate.now().plusDays(15);
        EnumMap<Role, Integer> requiredEmployees = new EnumMap<>(Role.class);
        try {
            shiftC.addShift(tomorrow, ShiftTime.MORNING, false, requiredEmployees);
            assertCondition(false, "Shift Added with Empty Required Employees - should have thrown an exception");
        } catch (IllegalArgumentException e) {
            assertCondition(true, "Correctly caught addition of shift with empty required employees");
        }
    }

    public static void testAddShiftWithNegativeRequiredEmployees() {
        LocalDate tomorrow = LocalDate.now().plusDays(16);
        EnumMap<Role, Integer> requiredEmployees = new EnumMap<>(Role.class);
        requiredEmployees.put(Role.SHIFT_MANAGER, -1);
        try {
            shiftC.addShift(tomorrow, ShiftTime.MORNING, false, requiredEmployees);
            assertCondition(false, "Shift Added with Negative Required Employees - should have thrown an exception");
        } catch (IllegalArgumentException e) {
            assertCondition(true, "Correctly caught addition of shift with negative required employees");
        }
    }

    public static void testShiftWithDeliveryRequiresDriver() {
        LocalDate tomorrow = LocalDate.now().plusDays(17);
        EnumMap<Role, Integer> requiredEmployees = new EnumMap<>(Role.class);
        requiredEmployees.put(Role.SHIFT_MANAGER, 1);
        requiredEmployees.put(Role.DRIVER, 0);
        try {
            shiftC.addShift(tomorrow, ShiftTime.MORNING, true, requiredEmployees);
        } catch (Exception e) {
            assertCondition(true, "Shift with Delivery Requires driver successfully");
            return;
        }
        assertCondition(false, "Shift with Delivery Requires Driver");
    }

    public static void testShiftWithDeliveryRequiresWareHouse() {
        LocalDate tomorrow = LocalDate.now().plusDays(18);
        EnumMap<Role, Integer> requiredEmployees = new EnumMap<>(Role.class);
        requiredEmployees.put(Role.SHIFT_MANAGER, 1);
        requiredEmployees.put(Role.WAREHOUSE, 0);
        try {
            shiftC.addShift(tomorrow, ShiftTime.MORNING, true, requiredEmployees);
        } catch (Exception e) {
            assertCondition(true, "Shift with Delivery required Warehouseworker successfully");
            return;
        }
        assertCondition(false, "Shift with Delivery Requires Warehouseworker");
    }

    
}
