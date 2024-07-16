package org.example.testInventory;

import org.example.Business.Enums.Role;
import org.example.Business.Enums.ShiftTime;
import org.example.Presentation.Main;
import org.example.PresentationLayerInventory.CommandLineUI;
import org.example.PresentationLayerInventory.InventoryCLI;
import org.example.PresentationLayerInventory.reducedCLI;
import org.example.Service.SystemService;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;

import org.example.DomainLayerInventory.CallBack;
import org.example. DomainLayerInventory.InventoryFacade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.EnumMap;

public class integrationTests {


    @Test
    public void testWareHouseManagerIntegration() {
        SystemService.loadBranchIds();
        String whMId = "326529229";
        int whq = SystemService.wareHouseQualification(whMId, 1);
        InventoryCLI cli = Main.getEmployeeInventoryCLI(whq);
        assertTrue(cli instanceof CommandLineUI);
        System.out.println("WareHouseManagerIntegration passed");
    }

    @Test
    public void testWareHouseEmployeeIntegration() {
        SystemService.loadBranchIds();
        String whEId = "1";
        int whq = SystemService.wareHouseQualification(whEId, 1);
        InventoryCLI cli = Main.getEmployeeInventoryCLI(whq);
        assertTrue(cli instanceof reducedCLI);
        System.out.println("WareHouseEmployeeIntegration passed");
    }

    @Test
    public void testNoWHPermission() {
        SystemService.loadBranchIds();
        String whEId = "2";
        int whq = SystemService.wareHouseQualification(whEId, 2);
        InventoryCLI cli = Main.getEmployeeInventoryCLI(whq);
        assertTrue(cli == null);
        System.out.println("NoWHPermission passed");
    }

}

