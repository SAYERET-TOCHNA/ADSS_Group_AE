package UnitTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;

import DomainLayer.BuisnessManager;

public class BusinessManagerTests {
    private BuisnessManager buisnessManager;


    @BeforeEach
    public void setUp() {
        buisnessManager = new BuisnessManager();
    }

    @Test
    public void testAddProductSuccess() {
        String result = buisnessManager.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        assertEquals("Add Product - success", result);
    }

    @Test
    public void testAddItemSuccess() {
        buisnessManager.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        String result = buisnessManager.addItem("Product1", 1, "12/12/2024", true);
        assertEquals("Add Item - success", result);
    }

    @Test
    public void testAddDiscountSuccess() {
        List<String[]> categoryList = new LinkedList<>();
        categoryList.add("Cat1|Cat2|Cat3".split("\\|"));
        buisnessManager.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        String result = buisnessManager.addDiscount("01/06/2024", "30/06/2024",
                categoryList,
                0.2, true, Arrays.asList("Product1"));
        assertEquals("Add Discount - success", result);
    }

    @Test
    public void testGetReportByCategorySuccess() {
        List<String[]> categoryList = new LinkedList<>();
        categoryList.add("Cat1|Cat2|Cat3".split("\\|"));
        buisnessManager.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        String result = buisnessManager.getReportByCategory(categoryList);
        assertTrue(result.contains("Report By Categories"));
    }

    @Test
    public void testGetReportAlmostMissing() {
        String result = buisnessManager.getReportAlmostMissing();
        assertTrue(result.contains("Report Of Almost Missing And Missing Products"));
    }

    @Test
    public void testGetReportBadProducts() {
        String result = buisnessManager.getReportBadProducts();
        assertTrue(result.contains("Report Of Wasted Products"));
    }

    @Test
    public void testChangeProductBuyingCostSuccess() {
        buisnessManager.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        String result = buisnessManager.changeProductBuyingCost("Product1", 12.0);
        assertEquals("Change Product Buying Price - success", result);
    }

    @Test
    public void testChangeProductSellingCostSuccess() {
        buisnessManager.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        String result = buisnessManager.changeProductSellingCost("Product1", 18.0);
        assertEquals("Change Product Selling Price - success", result);
    }

    @Test
    public void testChangeProductCategoriesSuccess() {
        buisnessManager.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        String result = buisnessManager.changeProductCategories("Product1", new String[]{"NewCat1", "NewCat2", "NewCat3"});
        assertEquals("Change Product Categories - success", result);
    }

    @Test
    public void testGetItemCostSuccess() {
        buisnessManager.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        buisnessManager.addItem("Product1", 1, "12/12/2024", true);
        String result = buisnessManager.getItemCost(1);
        assertEquals("15.0", result);
    }

    @Test
    public void testGetProductCostSuccess() {
        buisnessManager.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        String result = buisnessManager.getProductCost("Product1");
        assertEquals("15.0", result);
    }

    @Test
    public void testNotifyItemBoughtSuccess() {
        buisnessManager.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        buisnessManager.addItem("Product1", 1, "12/12/2024", true);
        String result = buisnessManager.notifyItemBought(1);
        assertEquals("Notify Item Bought - success", result);
    }

    @Test
    public void testGetItemSupplierCostSuccess() {
        buisnessManager.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        buisnessManager.addItem("Product1", 1, "12/12/2024", true);
        String result = buisnessManager.getItemSupplierCost(1);
        assertEquals("10.0", result);
    }

    @Test
    public void testGetProductSupplierCostSuccess() {
        buisnessManager.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        String result = buisnessManager.getProductSupplierCost("Product1");
        assertEquals("10.0", result);
    }

    @Test
    public void testRemoveDiscountSuccess() {
        List<String[]> categoryList = new LinkedList<>();
        categoryList.add("Cat1|Cat2|Cat3".split("\\|"));
        buisnessManager.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");

        buisnessManager.addDiscount("01/06/2024", "30/06/2024",
               categoryList,
                0.2, true, Arrays.asList("Product1"));
        String result = buisnessManager.removeDiscount(true, 1);
        assertEquals("Remove Discount - success ", result);
    }

    @Test
    public void testRemoveProductSuccess() {
        buisnessManager.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        String result = buisnessManager.removeProduct("Product1");
        assertEquals("Remove Product - success", result);
    }

    @Test
    public void testNotifyDamagedItemSuccess() {
        buisnessManager.addProduct("Product1", 10.0, 15.0,
                new String[]{"Cat1", "Cat2", "Cat3"}, "Manufacturer1",
                5, "A1", "B2");
        buisnessManager.addItem("Product1", 1, "12/12/2024", true);
        String result = buisnessManager.notifyDamagedItem(1);
        assertEquals("Notify Damaged Item - success", result);
    }
}
