import java.time.LocalDate;
import java.util.EnumMap;

import org.example.Business.Enums.Role;
import org.example.Business.Enums.ShiftTime;
import org.example.Business.ShiftController;

public class ShiftServiceTest {

    private static ShiftController service = new ShiftController(1);

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
            service.addShift(tomorrow, ShiftTime.MORNING, false, requiredEmployees);
            assertCondition(service.getRequiredEmployeesForShift(tomorrow, ShiftTime.MORNING) != null, "Valid Shift Added");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testAddShiftAlreadyExists() {
        LocalDate date = LocalDate.now().plusDays(2);
        EnumMap<Role, Integer> roles = new EnumMap<>(Role.class);
        roles.put(Role.SHIFT_MANAGER, 1);
        service.addShift(date, ShiftTime.MORNING,false, roles);
        try {
            service.addShift(date, ShiftTime.MORNING,false, roles);
            assertCondition(false, "Shift Already Exists - should have thrown an exception");
        } catch (IllegalArgumentException e) {
            assertCondition(true, "Correctly caught duplicate shift addition");
        }
    }

    public static void testAddShiftWithPastDate() {
        try {
            LocalDate yesterday = LocalDate.now().minusDays(1);
            EnumMap<Role, Integer> roles = new EnumMap<>(Role.class);
            roles.put(Role.SHIFT_MANAGER, 1);
            service.addShift(yesterday, ShiftTime.MORNING,false, roles);
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

    public static void testRemoveNonExistentShift() {
        LocalDate date = LocalDate.now().plusDays(4);
        try {
            service.removeShift(date, ShiftTime.MORNING);
            assertCondition(false, "Non-existent Shift Removed - should have thrown an exception");
        } catch (IllegalArgumentException e) {
            assertCondition(true, "Correctly caught attempt to remove a non-existent shift");
        }
    }

    public static void testAddAvailabilityBeforeDeadline() {
        String employeeId = "123";
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        service.setLastDateForSubmitting(tomorrow.plusDays(1));
        try {
            service.addAvailability(employeeId, tomorrow, ShiftTime.MORNING);
            assertCondition(service.getAvailableEmployeesForShift(tomorrow, ShiftTime.MORNING).contains(employeeId), "Availability Added Before Deadline");
        } catch (IllegalArgumentException e) {
            assertCondition(false, "Failed to add availability before deadline");
        }
    }

    public static void testAddAvailabilityAfterDeadline() {
        String employeeId = "123";
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        service.setLastDateForSubmitting(LocalDate.now());
        try {
            service.addAvailability(employeeId, tomorrow, ShiftTime.MORNING);
            assertCondition(false, "Availability Added After Deadline - should have thrown an exception");
        } catch (IllegalArgumentException e) {
            assertCondition(true, "Correctly caught addition of availability after deadline");
        }
    }

    public static void testRemoveAvailability() {
        String employeeId = "456";
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        service.setLastDateForSubmitting(tomorrow.plusDays(4));
        service.addAvailability(employeeId, tomorrow, ShiftTime.MORNING);
        try {
            service.removeAvailability(employeeId, tomorrow, ShiftTime.MORNING);
            assertCondition(!service.getAvailableEmployeesForShift(tomorrow, ShiftTime.MORNING).contains(employeeId), "Availability Successfully Removed");
        } catch (IllegalArgumentException e) {
            assertCondition(false, "Failed to remove availability");
        }
    }


    public static void testAddEmployeeToShift() {
        String employeeId = "789";
        LocalDate tomorrow = LocalDate.now().plusDays(19);
        EnumMap<Role, Integer> roles = new EnumMap<>(Role.class);
        roles.put(Role.SHIFT_MANAGER, 1);
        service.addShift(tomorrow, ShiftTime.MORNING,false, roles);
        service.setLastDateForSubmitting(tomorrow.plusDays(20) );
        service.addAvailability(employeeId, tomorrow, ShiftTime.MORNING);
        try {
            service.addEmployeeToShift(employeeId, tomorrow, ShiftTime.MORNING, Role.SHIFT_MANAGER);
            assertCondition(service.getEmployeesForShift(tomorrow, ShiftTime.MORNING).get(Role.SHIFT_MANAGER).contains(employeeId), "Employee Added to Shift");
        } catch (IllegalArgumentException e) {
            assertCondition(false, "Failed to add employee to shift");
        }
    }

    public static void testAddEmployeeToNonexistentShift() {
        String employeeId = "789";
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        try {
            service.addEmployeeToShift(employeeId, tomorrow, ShiftTime.MORNING, Role.SHIFT_MANAGER);
            assertCondition(false, "Employee Added to Nonexistent Shift - should have thrown an exception");
        } catch (IllegalArgumentException e) {
            assertCondition(true, "Correctly caught addition of employee to non-existent shift");
        }
    }

    public static void testRemoveEmployeeFromShift() {
        String employeeId = "1011";
        LocalDate tomorrow = LocalDate.now().plusDays(69);
        EnumMap<Role, Integer> roles = new EnumMap<>(Role.class);
        roles.put(Role.SHIFT_MANAGER, 1);
        service.addShift(tomorrow, ShiftTime.MORNING,false, roles);
        service.setLastDateForSubmitting(tomorrow.plusDays(34));
        service.addAvailability(employeeId, tomorrow, ShiftTime.MORNING);
        service.addEmployeeToShift(employeeId, tomorrow, ShiftTime.MORNING, Role.SHIFT_MANAGER);
        try {
            service.removeEmployeeFromShift(employeeId, tomorrow, ShiftTime.MORNING, Role.SHIFT_MANAGER);
            assertCondition(!service.getEmployeesForShift(tomorrow, ShiftTime.MORNING).get(Role.SHIFT_MANAGER).contains(employeeId), "Employee Removed from Shift");
        } catch (IllegalArgumentException e) {
            assertCondition(false, "Failed to remove employee from shift");
        }
    }

    

    
}
