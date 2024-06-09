package UnitTests;
import DomainLayer.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;
public class InventoryTests {
    private Inventory inventory;

    @BeforeEach
    public void setUp() {
        inventory = new Inventory();
    }





    @Test
    public void testGetItemsPriceSuccess() {
        inventory.addProduct("Product1", 10.0, 15.0, new String[]{"Cat1", "Cat2", "Cat3"},
                "Manufacturer1", 5, "A1", "B2");
        inventory.addItem("Product1", 1, "12/12/2024", true);
        double cost = inventory.getItemPrice(1);
        assertEquals(15.0, cost);
    }



    @Test
    public void testGetItemsSupplierCostSuccess() {
        inventory.addProduct("Product1", 10.0, 15.0, new String[]{"Cat1", "Cat2", "Cat3"},
                "Manufacturer1", 5, "A1", "B2");
        inventory.addItem("Product1", 1, "12/12/2024", true);
        double cost = inventory.getItemSupplierCost(1);
        assertEquals(10.0, cost);
    }

    @Test
    public void testGetProductsSupplierCostSuccess() {
        inventory.addProduct("Product1", 10.0, 15.0, new String[]{"Cat1", "Cat2", "Cat3"},
                "Manufacturer1", 5, "A1", "B2");
        double cost = inventory.getProductSupplierCost("Product1");
        assertEquals(10.0, cost);
    }

    @Test
    public void testAddDiscountSuccess() {
        inventory.addProduct("Product1", 10.0, 15.0, new String[]{"Cat1", "Cat2", "Cat3"},
                "Manufacturer1", 5, "A1", "B2");
        List<String[]> categoryList = new LinkedList<>();
        categoryList.add("Cat1|Cat2|Cat3".split("\\|"));
        inventory.addDiscount("01/06/2024", "30/06/2024",
                categoryList,
                0.2, true, Arrays.asList("Product1"));

        double costWithDiscount = inventory.getProductPrice("Product1");
        assertEquals(12.0, costWithDiscount);

        // Add assertions to check if discount was removed successfully
    }

    @Test
    public void testRemoveDiscountSuccess() {
        inventory.addProduct("Product1", 10.0, 15.0, new String[]{"Cat1", "Cat2", "Cat3"},
                "Manufacturer1", 5, "A1", "B2");
        List<String[]> categoryList = new LinkedList<>();
        categoryList.add("Cat1|Cat2|Cat3".split("\\|"));
        inventory.addDiscount("01/06/2024", "30/06/2024",
                categoryList,
                0.2, true, Arrays.asList("Product1"));

        inventory.removeDiscount(true, 1);
        double costWithoutDiscount = inventory.getProductPrice("Product1");
        assertEquals(15.0, costWithoutDiscount);
    }

    @Test
    public void testGetReportAlmostMissingSuccess() {
        String report = inventory.reportByAlmostMissing();
        assertTrue(report.contains("Report Of Almost Missing And Missing Products"));
    }

    @Test
    public void testGetReportBadProductsSuccess() {
        String report = inventory.reportBadProducts();
        assertTrue(report.contains("Report Of Wasted Products"));
    }

    @Test
    public void testChangeProductBuyingCostSuccess() {
        inventory.addProduct("Product1", 10.0, 15.0, new String[]{"Cat1", "Cat2", "Cat3"},
                "Manufacturer1", 5, "A1", "B2");
        inventory.changeProductBuyingCost("Product1", 12.0);
        double buyingCost = inventory.getProductSupplierCost("Product1");
        assertEquals(12.0, buyingCost);
    }

    @Test
    public void testChangeProductSellingCostSuccess() {
        inventory.addProduct("Product1", 10.0, 15.0, new String[]{"Cat1", "Cat2", "Cat3"},
                "Manufacturer1", 5, "A1", "B2");
        inventory.changeProductSellingCost("Product1", 18.0);
        double buyingCost = inventory.getProductPrice("Product1");
        assertEquals(18.0, buyingCost);    }


    @Test
    public void testRemoveProductSuccess() {
        inventory.addProduct("Product1", 10.0, 15.0, new String[]{"Cat1", "Cat2", "Cat3"},
                "Manufacturer1", 5, "A1", "B2");
        inventory.removeProductFromEverywhere("Product1");
        assertFalse(inventory.isProductExist("Product1"));
    }

    @Test
    public void testDoubleDiscountSuccess() {
        List<String[]> categoryList = new LinkedList<>();
        categoryList.add("Cat1|Cat2|Cat3".split("\\|"));
        inventory.addProduct("Product1", 10.0, 16.0, new String[]{"Cat1", "Cat2", "Cat3"},
                "Manufacturer1", 5, "A1", "B2");
        inventory.addDiscount("01/06/2024", "30/06/2024",
                categoryList,
                0.2, true, Arrays.asList("Product1"));
        inventory.addDiscount("01/06/2024", "30/06/2024",
                categoryList,
                0.5, true, Arrays.asList("Product1"));
        double cost = inventory.getProductPrice("Product1");
        assertEquals(8.0,cost);
    }
}
