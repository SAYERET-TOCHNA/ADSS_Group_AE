package org.example.DataAccessInventory.DtoClasses;

import org.example.DataAccessInventory.ControllerClasses.Controller;
import java.util.Date;

public class ProductDto extends Dto {
    private String name;
    private double buyingCost;
    private double sellingCost;
    private String category;
    private String manufacturer;
    private int minimumAmountAllowed;
    private String wareHouseLocation;
    private String storeLocation;

    public ProductDto(String name, double buyingCost, double sellingCost, String category, String manufacturer,
                      int minimumAmountAllowed, String wareHouseLocation, String storeLocation, Controller controller, boolean fromDB) {
        super(controller, fromDB);
        this.name = name;
        this.buyingCost = buyingCost;
        this.sellingCost = sellingCost;
        this.category = category;
        this.manufacturer = manufacturer;
        this.minimumAmountAllowed = minimumAmountAllowed;
        this.wareHouseLocation = wareHouseLocation;
        this.storeLocation = storeLocation;
    }

    @Override
    public void persist() {
        insert(new Object[]{name, category,buyingCost, sellingCost,  manufacturer,
                minimumAmountAllowed, wareHouseLocation, storeLocation});
        isPersisted = true;
    }
    public void updateCategory(String category) {
        this.category =category;
        if (isPersisted) {
            update(new Object[]{name}, "category", category);
        }
    }
    public void updateBuyingCost(double buyingCost) {
        this.buyingCost = buyingCost;
        if (isPersisted) {
            update(new Object[]{name}, "buyingCost", buyingCost);
        }
    }

    public void updateSellingCost(double sellingCost) {
        this.sellingCost =sellingCost;
        if (isPersisted) {
            update(new Object[]{name}, "sellingCost", sellingCost);
        }
    }

    public void updateWarehouseLocation(String wareHouseLocation) {
        this.wareHouseLocation =wareHouseLocation;
        if (isPersisted) {
            update(new Object[]{name}, "wareHouseLocation", wareHouseLocation);
        }
    }

    public void updateStoreLocation(String storeLocation) {
        this.storeLocation = storeLocation;
        if (isPersisted) {
            update(new Object[]{name}, "storeLocation", storeLocation);
        }
    }

    public void deleteProduct() {
        if (isPersisted) {
            delete(new Object[]{name});
            isPersisted = false;
        }
    }

    public String getName() {
        return name;
    }


    public double getBuyingCost() {
        return buyingCost;
    }


    public double getSellingCost() {
        return sellingCost;
    }

    public String getCategory() {
        return category;
    }

    public String getManufacturer() {
        return manufacturer;
    }


    public int getMinimumAmountAllowed() {
        return minimumAmountAllowed;
    }


    public String getWareHouseLocation() {
        return wareHouseLocation;
    }


    public String getStoreLocation() {
        return storeLocation;
    }

}
