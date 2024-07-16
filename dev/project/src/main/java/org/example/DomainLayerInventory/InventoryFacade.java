package org.example.DomainLayerInventory;

import org.example.DataAccessInventory.ControllerClasses.DiscountController;
import org.example.DataAccessInventory.ControllerClasses.ItemController;
import org.example.DataAccessInventory.ControllerClasses.OrderController;
import org.example.DataAccessInventory.ControllerClasses.ProductController;
import org.example.DataAccessInventory.DtoClasses.DiscountDto;
import org.example.DataAccessInventory.DtoClasses.ItemDto;
import org.example.DataAccessInventory.DtoClasses.OrderDto;
import org.example.DataAccessInventory.DtoClasses.ProductDto;

import java.util.Arrays;
import java.util.List;

public class InventoryFacade {
    private final Inventory inventory;
    private ItemController itemController;
    private OrderController orderController;
    private ProductController productController;
    private DiscountController discountController;
    public InventoryFacade(CallBack callBack){
        this.itemController = new ItemController();
        this.orderController = new OrderController();
        this.productController = new ProductController();
        this.discountController = new DiscountController();
        inventory = new Inventory(callBack,itemController,orderController,productController,discountController);



    }

    public String init(){
        List<ItemDto> items = itemController.selectAllItems();
        List<OrderDto> orders = orderController.selectAllOrders();
        List<ProductDto> products = productController.selectAllProducts();
        List<DiscountDto> discounts = discountController.selectAllDiscounts();

        for (ProductDto product : products) {
            String name = product.getName();
            double buyingCost = product.getBuyingCost();
            double sellingCost = product.getSellingCost();
            String[] category = product.getCategory().split("\\|");
            String manufacturer = product.getManufacturer();
            int minimumAmountAllowed = product.getMinimumAmountAllowed();
            String wareHouseLocation = product.getWareHouseLocation();
            String storeLocation = product.getStoreLocation();

            // Add the product

            inventory.addProduct(name, buyingCost, sellingCost, category, manufacturer, minimumAmountAllowed, wareHouseLocation, storeLocation, true);
        }
        // Iterate through the items
        for (ItemDto item : items) {
            String productName = item.getProductName();
            int itemID = item.getItemID();
            String expiredDate = Utils.DateToString(item.getExpiredDate());
            boolean isInWareHouse = item.isInWareHouse();
            boolean isDamaged = item.isDamaged();

            // Add the item
            inventory.addItem(productName, itemID, expiredDate, isInWareHouse, isDamaged, true);
        }

// Iterate through the orders
        for (OrderDto order : orders) {
            String productName = order.getProductName();
            int amount = order.getAmount();
            int dayOfMonth = order.getDayOfMonth();
            int existingId = order.getOrderId();

            // Add the order
            inventory.addOrder(productName, amount, dayOfMonth, existingId, true);
        }

// Iterate through the products


// Iterate through the discounts
        for (DiscountDto discount : discounts) {
            String startDateString = Utils.DateToString(discount.getStartDate());
            String endDateString = Utils.DateToString(discount.getEndDate());
            List<String[]> stringCategoryList = Utils.ListOfStringArraysFromString(discount.getCategories());
            double discountParameter = discount.getDiscountParameter();
            boolean inStore = discount.isToClients();
            List<String> productNames = Arrays.stream(discount.getDiscountedProducts().split("#")).toList();
            int existingId = discount.getDiscountId();

            // Add the discount
            inventory.addDiscount(startDateString, endDateString, stringCategoryList, discountParameter, inStore, productNames, existingId, true);
        }
// Adding Discounts
        String output = "\n----------------------------------\n";
        if (Utils.isTodaySunday()){
            output += "Today is Sunday so you can get your weekly sunday report\n" +
                    "by pressing 5, 1 and choosing the categories for this week";
        }

        //loadSampleData();
        return output;
    }

    public String deleteAllFromDB(){
        try{
            itemController.deleteAll();
            return "Delete Everything - success";
        }catch (Exception e){
            return "Delete Everything - failed " + e.getMessage();
        }
    }
    public String addProduct(String name, double buyingCost, double sellingCost,
                             String[] categories, String manufacturer, int minimumAmountAllowed,
                             String warehouseLocation, String storeLocation) {
        try {
            inventory.addProduct(name, buyingCost, sellingCost, categories, manufacturer,
                    minimumAmountAllowed, warehouseLocation,  storeLocation,false);
            return "Add Product - success";
        } catch (Exception e) {
            return "Add Product - failed " + e.getMessage();
        }
    }

    public String addItem(String productName,int itemID ,String expiredDate ,boolean isInWareHouse,boolean isDamaged) {
        try {
            inventory.addItem(productName,itemID, expiredDate,isInWareHouse,isDamaged,false);
            return "Add Item - success";
        } catch (Exception e) {
            return "Add Item - failed " + e.getMessage();
        }
    }
    public String addDiscount(String startDate,String endDate ,List<String[]> categories ,double discountParameter,
                              boolean inStore,List<String> productNames) {
        try {
            inventory.addDiscount(startDate,endDate ,categories ,discountParameter,inStore,productNames,-1,false);

            return "Add Discount - success";
        } catch (Exception e) {
            return "Add Discount - failed " + e.getMessage();
        }
    }

    public String getReportByCategory(List<String> categories) {
        try {
            return inventory.reportByCategories(categories);

        } catch (Exception e) {
            return "Get Report By Category - failed " + e.getMessage();
        }
    }

    public String getReportAlmostMissing() {
        try {
            return inventory.reportByAlmostMissing();
        } catch (Exception e) {
            return "Get Report Almost Missing - failed " + e.getMessage();
        }
    }

    public String getReportBadProducts() {
        try {
            return inventory.reportBadProducts();
        } catch (Exception e) {
            return "Get Report Bad Products - failed " + e.getMessage();
        }
    }

    public String changeProductBuyingCost(String productName, double newBuyingCost) {
        try {
            inventory.changeProductBuyingCost(productName, newBuyingCost);
            return "Change Product Buying Price - success";
        } catch (Exception e) {
            return "Change Product Buying Price - failed " + e.getMessage();
        }
    }

    public String changeProductSellingCost(String productName, double newSellingCost) {
        try {
            inventory.changeProductSellingCost(productName, newSellingCost);
            return "Change Product Selling Price - success";
        } catch (Exception e) {
            return "Change Product Selling Price - failed " + e.getMessage();
        }
    }


    public String changeProductCategories(String productName, String[] newCategories) {
        try {
            inventory.changeProductCategories(productName, newCategories);
            return "Change Product Categories - success";
        } catch (Exception e) {
            return "Change Product Categories - failed " + e.getMessage();
        }
    }



    public String getProductCost(String productName){
        try {
            return Double.toString(inventory.getProductPrice(productName));

        } catch (Exception e) {
            return "Get Product Cost - failed " + e.getMessage();
        }
    }

    public String notifyItemBought(int itemId){
        try {
            inventory.buyItem(itemId);
            return "Notify Item Bought - success";
        } catch (Exception e) {
            return "Notify Item Bought - failed " + e.getMessage();
        }
    }



    public String getProductSupplierCost(String productName){
        try {
            return Double.toString(inventory.getProductSupplierCost(productName));
        } catch (Exception e) {
            return "get Product Supplier Cost- failed " + e.getMessage();
        }
    }

    public String removeDiscount(int discountNumber){
        try {
            inventory.removeDiscount(discountNumber);
            return "Remove Discount - success ";
        } catch (Exception e) {
            return "Remove Discount- failed " + e.getMessage();
        }
    }

    public String showDiscounts(){
        try {
           return inventory.showDiscounts();

        } catch (Exception e) {
            return "Show Discounts - failed " + e.getMessage();
        }
    }

    public String removeProduct(String productName){
        try {
            inventory.removeProductFromEverywhere(productName);
            return "Remove Product - success";
        } catch (Exception e) {
            return "Remove Product - failed " + e.getMessage();
        }
    }

    public String notifyDamagedItem(int itemID){
        try {
            inventory.notifyDamagedItem(itemID);
            return "Notify Damaged Item - success";
        } catch (Exception e) {
            return "Notify Damaged Item - failed " + e.getMessage();
        }
    }

    public String moveItemFromWarehouse(int itemID){
        try {
            inventory.moveItemFromWarehouse(itemID);
            return "Move Item From Warehouse - success";
        } catch (Exception e) {
            return "Move Item From Warehouse - failed " + e.getMessage();
        }
    }
    public String moveItemToWarehouse(int itemID){
        try {
            inventory.moveItemToWarehouse(itemID);
            return "Move Item To Warehouse - success";
        } catch (Exception e) {
            return "Move Item To Warehouse - failed " + e.getMessage();
        }
    }
    public String getProductLocation(String productName, boolean inWarehouse){
        try {
            return inventory.getProductLocation(productName,inWarehouse);
        } catch (Exception e) {
            return "Get Product Location- failed " + e.getMessage();
        }
    }
    public String addOrder(String productName,int amount,int dayOfMonth){
        try {
            inventory.addOrder(productName,amount,dayOfMonth,-1,false);
            return "Add Order - success";
        } catch (Exception e) {
            return "Add Order - failed " + e.getMessage();
        }
    }
    public String showOrders() {
        try {
            return inventory.showOrders();
        } catch (Exception e) {
            return "Show Orders - failed " + e.getMessage();
        }
    }

    public String removeOrder(int index) {
        try {
            inventory.removeOrder(index);
            return "Remove Order - success";
        } catch (Exception e) {
            return "Remove Order - failed " + e.getMessage();
        }
    }

    public String updateOrder(int index, int newAmount) {
        try {
            return inventory.updateOrder(index, newAmount);
        } catch (Exception e) {
            return "Update Order - failed " + e.getMessage();
        }
    }

    public String getItemProductName(int itemId) {
        try {
            return inventory.getItemProductName(itemId);

        } catch (Exception e) {
            return "Get Item Product Name - failed " + e.getMessage();
        }
    }

    private void loadSampleData() {

        inventory.addProduct("Apple", 10.5, 15.0, new String[]{"Fruit", "Fresh", "Small"}, "FreshFarms", 5, "D1", "S1", false);
        inventory.addProduct("Banana", 8.0, 12.0, new String[]{"Fruit", "Fresh", "Medium"}, "TropicFruits", 3, "D2", "S2", false);
        inventory.addProduct("Carrot", 7.5, 10.0, new String[]{"Vegetable", "Root", "Small"}, "HealthyVeg", 2, "D3", "S3", false);
        inventory.addProduct("Detergent", 5.5, 9.0, new String[]{"Household", "Cleaning", "Large"}, "CleanCo", 6, "D4", "S4", false);
        inventory.addProduct("Shampoo", 12.0, 18.0, new String[]{"PersonalCare", "Hair", "Medium"}, "HairPlus", 4, "D5", "S5", false);
        inventory.addProduct("Milk", 2.5, 4.0, new String[]{"Dairy", "Fresh", "Large"}, "DailyDairy", 8, "D6", "S6", false);
        inventory.addProduct("Orange", 9.0, 14.0, new String[]{"Fruit", "Citrus", "Medium"}, "CitrusWorld", 5, "D7", "S7", false);
        inventory.addProduct("Tomato", 6.0, 9.0, new String[]{"Vegetable", "Fresh", "Small"}, "GreenThumb", 4, "D8", "S8", false);
        inventory.addProduct("Cereal", 3.5, 6.0, new String[]{"Grocery", "Breakfast", "Packaged"}, "GrainHouse", 7, "D9", "S9", false);
        inventory.addProduct("Soap", 4.0, 6.5, new String[]{"PersonalCare", "Skin", "Medium"}, "PureEssence", 6, "D1", "S1", false);
        inventory.addProduct("Cheese", 8.5, 12.0, new String[]{"Dairy", "Fresh", "Small"}, "DairyDelight", 5, "D1", "S1", false);
        inventory.addProduct("Juice", 5.0, 8.0, new String[]{"Beverage", "Fresh", "Large"}, "JuiceVille", 7, "D1", "S1", false);

        for (int i = 0; i < 20; i++) {
                inventory.addItem("Apple", 100 + i, "05/07/2024", true, false, false);
                inventory.addItem("Banana", 200 + i, "31/07/2024", false, false, false);
                inventory.addItem("Carrot", 300 + i, "01/08/2024", true, false, false);
                inventory.addItem("Detergent", 400 + i, "15/07/2024", true, false, false);
                inventory.addItem("Shampoo", 500 + i, "20/08/2024", false, false, false);
                inventory.addItem("Milk", 600 + i, "01/08/2024", true, false, false);
                inventory.addItem("Orange", 700 + i, "15/07/2024", true, false, false);
                inventory.addItem("Tomato", 800 + i, "22/08/2024", false, false, false);
                inventory.addItem("Cereal", 900 + i, "30/07/2024", true, false, false);
                inventory.addItem("Soap", 1000 + i, "12/08/2024", false, false, false);
                inventory.addItem("Cheese", 1100 + i, "25/07/2024", true, false, false);
                inventory.addItem("Juice", 1200 + i, "03/08/2023", false, false, false);
        }


        inventory.addDiscount("01/06/2024", "31/12/2024", Arrays.asList(new String[]{"Fruit", "Fresh", "Small"}, new String[]{"Vegetable", "Root", "Small"}), 0.2, true, Arrays.asList("Apple", "Carrot"), -1, false);
        inventory.addDiscount("01/06/2024", "31/07/2024", Arrays.asList(new String[]{"Household", "Cleaning", "Large"}, new String[]{"PersonalCare", "Hair", "Medium"}), 0.15, false, Arrays.asList("Detergent", "Shampoo"), -1, false);
        inventory.addDiscount("01/06/2024", "31/08/2024", Arrays.asList(new String[]{"Dairy", "Fresh", "Large"}, new String[]{"Fruit", "Fresh", "Medium"}), 0.1, true, Arrays.asList("Milk", "Banana"), -1, false);
        inventory.addDiscount("15/07/2024", "31/12/2024", Arrays.asList(new String[]{"Fruit", "Citrus", "Medium"}, new String[]{"Beverage", "Fresh", "Large"}), 0.25, true, Arrays.asList("Orange", "Juice"), -1, false);
        inventory.addDiscount("01/08/2024", "31/10/2024", Arrays.asList(new String[]{"Vegetable", "Fresh", "Small"}, new String[]{"PersonalCare", "Skin", "Medium"}), 0.2, false, Arrays.asList("Tomato", "Soap"), -1, false);






    }

    public int productsAmount(){
        return inventory.productsAmount();
    }
    public int itemsAmount(){
        return inventory.itemsAmount();
    }
    public int discountsAmount(){
        return inventory.discountsAmount();
    }
    public int ordersAmount(){
        return inventory.ordersAmount();
    }

}
