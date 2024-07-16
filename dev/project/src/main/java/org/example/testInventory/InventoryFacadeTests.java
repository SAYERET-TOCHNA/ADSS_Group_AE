package org.example.testInventory;

import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;

import org.example.DomainLayerInventory.CallBack;
import org.example. DomainLayerInventory.InventoryFacade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryFacadeTests {
    private InventoryFacade inventoryFacade;


    @BeforeEach
    public void setUp() {
        inventoryFacade = new InventoryFacade(new CallBack() {
            @Override
            public void call(String msg) {
            }
        });
        inventoryFacade.deleteAllFromDB();

    }

    @Test
    public void testAddProductSuccess() {
        int size1 = inventoryFacade.productsAmount();
        String result = inventoryFacade.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");

        int size2 = inventoryFacade.productsAmount();
        assertEquals(size1 +1, size2);

    }

    @Test
    public void testAddItemSuccess() {
        int size1 = inventoryFacade.itemsAmount();
        inventoryFacade.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        String result = inventoryFacade.addItem("Product1", 1, "12/12/2024",
                true,false);

        int size2 = inventoryFacade.itemsAmount();

        assertEquals(size1 +1, size2);

    }

    @Test
    public void testAddDiscountSuccess() {
        int size1 = inventoryFacade.discountsAmount();
        List<String[]> categoryList = new LinkedList<>();
        categoryList.add("Cat1|Cat2|Cat3".split("\\|"));
        inventoryFacade.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        String result = inventoryFacade.addDiscount("01/06/2024", "30/06/2024",
                categoryList,
                0.2, true, Arrays.asList("Product1"));

        int size2 = inventoryFacade.discountsAmount();

        assertEquals(size1 +1, size2);


    }

    @Test
    public void testGetReportAlmostMissing() {
        String result = inventoryFacade.getReportAlmostMissing();
        assertTrue(result.contains("Report Of Almost Missing And Missing Products"));
    }

    @Test
    public void testGetReportBadProducts() {
        String result = inventoryFacade.getReportBadProducts();
        assertTrue(result.contains("Report Of Wasted Products"));
    }

    @Test
    public void testChangeProductBuyingCostSuccess() {
        inventoryFacade.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        String result = inventoryFacade.changeProductBuyingCost("Product1", 12.0);
        assertEquals("Change Product Buying Price - success", result);

    }

    @Test

    public void testChangeProductSellingCostSuccess() {
        inventoryFacade.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        String result = inventoryFacade.changeProductSellingCost("Product1", 18.0);
        assertEquals("Change Product Selling Price - success", result);


    }

    @Test
    public void testChangeProductCategoriesSuccess() {
        inventoryFacade.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        String result = inventoryFacade.changeProductCategories("Product1", new String[]{"NewCat1", "NewCat2", "NewCat3"});
        assertEquals("Change Product Categories - success", result);

    }




    @Test
    public void testGetProductCostSuccess() {
        inventoryFacade.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        String result = inventoryFacade.getProductCost("Product1");
        assertEquals("15.0", result);
    }

    @Test
    public void testNotifyItemBoughtSuccess() {
        inventoryFacade.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        inventoryFacade.addItem("Product1", 1, "12/12/2024", true,false);
        int size1 = inventoryFacade.itemsAmount();
        String result = inventoryFacade.notifyItemBought(1);

        int size2 = inventoryFacade.itemsAmount();
        assertEquals(size1 -1, size2);

    }


    @Test
    public void testGetProductSupplierCostSuccess() {
        inventoryFacade.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        String result = inventoryFacade.getProductSupplierCost("Product1");
        assertEquals("10.0", result);
    }


    @Test
    public void testRemoveProductSuccess() {
        inventoryFacade.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        int size1 = inventoryFacade.productsAmount();
        String result = inventoryFacade.removeProduct("Product1");
        int size2 = inventoryFacade.productsAmount();
        assertEquals(size1 -1, size2);
    }

    @Test
    public void testNotifyDamagedItemSuccess() {
        inventoryFacade.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        inventoryFacade.addItem("Product1", 1, "12/12/2024", true,false);
        String result = inventoryFacade.notifyDamagedItem(1);
        assertEquals("Notify Damaged Item - success", result);
    }


    @Test
    public void testAddOrderSuccess() {
        int size1 = inventoryFacade.ordersAmount();
        inventoryFacade.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        String result = inventoryFacade.addOrder("Product1", 5, 10);
        assertEquals("Add Order - success", result);

        int size2 = inventoryFacade.productsAmount();
        assertEquals(size1 +1, size2);
    }

    @Test
    public void testAddOrderFailure() {
        String result = inventoryFacade.addOrder("NonExistentProduct", 5, 10);
        assertEquals("Add Order - failed no such product in the inventory", result);
    }

    @Test
    public void testShowOrdersSuccess() {
        inventoryFacade.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        inventoryFacade.addOrder("Product1", 5, 10);
        String result = inventoryFacade.showOrders();
        assertTrue(result.contains("Orders:"));
    }

    @Test
    public void testRemoveOrderSuccess() {

        inventoryFacade.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        inventoryFacade.addOrder("Product1", 5, 10);
        int size1 = inventoryFacade.ordersAmount();
        String result = inventoryFacade.removeOrder(1);
        int size2 = inventoryFacade.ordersAmount();
        assertEquals(size1 -1, size2);
    }

    @Test
    public void testRemoveOrderFailure() {
        String result = inventoryFacade.removeOrder(10);
        assertTrue(result.contains("failed"));     }

    @Test
    public void testUpdateOrderSuccess() {
        inventoryFacade.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        inventoryFacade.addOrder("Product1", 5, 10);
        String result = inventoryFacade.updateOrder(1, 10);
        assertTrue(!result.contains("failed"));
    }

    @Test
    public void testUpdateOrderFailure() {
        String result = inventoryFacade.updateOrder(10, 10);
        assertTrue(result.contains("failed"));
    }

    @Test
    public void testUpdateOrderEffect() {
        inventoryFacade.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        inventoryFacade.addOrder("Product1", 5, 10);
        String result1 = inventoryFacade.showOrders();
        inventoryFacade.updateOrder(1, 10);
        String result2 = inventoryFacade.showOrders();
        assertNotEquals(result1,result2);
    }

    @Test
    public void testAutoOrderAddition(){
        int size1 = inventoryFacade.ordersAmount();
        inventoryFacade.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        inventoryFacade.addItem("Product1", 1, "12/12/2024", true,false);
        inventoryFacade.notifyItemBought(1);
        String result = inventoryFacade.showOrders();
        int size2 = inventoryFacade.productsAmount();
        assertEquals(size1 +1, size2);
    }

    @AfterEach
    public void afterEach() {
        inventoryFacade = new InventoryFacade(new CallBack() {
            @Override
            public void call(String msg) {
            }
        });
        inventoryFacade.deleteAllFromDB();
    }
}

