package DomainLayer.Buisness;

import java.util.List;

public class BuisnessManager {
    private final Inventory inventory;

    public BuisnessManager(String config){
        inventory = new Inventory();
        System.out.println(loadData(config));
    }
    public BuisnessManager(){
        inventory = new Inventory();
    }

    public String loadData(String data) {
        try {
            StringBuilder output = new StringBuilder();
            List<String> problematicLines = inventory.loadData(data);
            for (String s : problematicLines){
                output.append(s).append("\n");
            }
            if (output.isEmpty())
                return "Load Data - success";
            else return output.toString();
        } catch (Exception e) {
            return "Load Data - failed " + e.getMessage();
        }
    }

    public String addProduct(String name, double buyingCost, double sellingCost,
                             String[] categories, String manufacturer, int minimumAmountAllowed,
                             String warehouseLocation, String storeLocation) {
        try {
            inventory.addProduct(name, buyingCost, sellingCost, categories, manufacturer,
                    minimumAmountAllowed, warehouseLocation,  storeLocation);
            return "Add Product - success";
        } catch (Exception e) {
            return "Add Product - failed " + e.getMessage();
        }
    }

    public String addItem(String productName,int itemID ,String expiredDate ,boolean isInWareHouse) {
        try {
            inventory.addItem(productName,itemID, expiredDate,isInWareHouse);
            return "Add Item - success";
        } catch (Exception e) {
            return "Add Item - failed " + e.getMessage();
        }
    }
    public String addDiscount(String startDate,String endDate ,List<String[]> categories ,double discountParameter,
                              boolean inStore) {
        try {
            inventory.addDiscount(startDate,endDate ,categories ,discountParameter,inStore);
            return "Add Discount - success";
        } catch (Exception e) {
            return "Add Discount - failed " + e.getMessage();
        }
    }

    public String getReportByCategory(List<String[]> categories) {
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

    public String getItemCost(int itemId){
        try {
            return Double.toString(inventory.getItemPrice(itemId));
        } catch (Exception e) {
            return "Get Item Cost - failed " + e.getMessage();
        }
    }

    public String getProductCost(String productName){
        try {
            return Double.toString(inventory.getProductPrice(productName));

        } catch (Exception e) {
            return "Get Product Cost - failed " + e.getMessage();
        }
    }

    public String buyItem(int itemId){
        try {
            inventory.buyItem(itemId);
            return "Buy Item - success";
        } catch (Exception e) {
            return "Buy Item- failed " + e.getMessage();
        }
    }

    public String getItemSupplierCost(int itemId){
        try {
            return Double.toString(inventory.getItemSupplierCost(itemId));
        } catch (Exception e) {
            return "get Item Supplier Cost- failed " + e.getMessage();
        }
    }

    public String getProductSupplierCost(String productName){
        try {
            return Double.toString(inventory.getProductSupplierCost(productName));
        } catch (Exception e) {
            return "get Product Supplier Cost- failed " + e.getMessage();
        }
    }

    public String removeDiscount(boolean isStore,int discountNumber){
        try {
            inventory.removeDiscount(isStore, discountNumber);
            return "Remove Discount - success ";
        } catch (Exception e) {
            return "Remove Discount- failed " + e.getMessage();
        }
    }

    public String showDiscounts(boolean isStore){
        try {
           return inventory.showDiscounts(isStore);

        } catch (Exception e) {
            return "Show Discounts - failed " + e.getMessage();
        }
    }

    public String removeProduct(String productName){
        try {
            inventory.removeProductFromEverywhere(productName);
            return "Remove Product - success";
        } catch (Exception e) {
            return "Show Discounts - failed " + e.getMessage();
        }
    }

}
