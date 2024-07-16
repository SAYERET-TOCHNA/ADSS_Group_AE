package org.example.DomainLayerInventory;

import org.example.DataAccessInventory.ControllerClasses.ProductController;
import org.example.DataAccessInventory.DtoClasses.Dto;
import org.example.DataAccessInventory.DtoClasses.ProductDto;

public class Product {

    private String name;
    private double buyingCost;
    private double sellingCost;
    private Category category;
    private String manufacturer;
    private int minimumAmountAllowed;

    private String wareHouseLocation;
    private String storeLocation;
    private ProductDto myDto;
    public String getWareHouseLocation() {
        return wareHouseLocation;
    }

    public void setWareHouseLocation(String wareHouseLocation) {
        this.wareHouseLocation = wareHouseLocation;
    }

    public String getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(String storeLocation) {
        this.storeLocation = storeLocation;
    }




    public Product(String name, double buyingCost, double sellingCost,
                   Category category, String manufacturer, int minimumAmountAllowed, String wareHouseLocation,
                   String storeLocation, ProductController productController, boolean fromDB)
    {
        this.name = name;
        this.buyingCost = buyingCost;
        this.sellingCost = sellingCost;
        this.category = category;
        this.manufacturer = manufacturer;
        this.minimumAmountAllowed = minimumAmountAllowed;
         this.wareHouseLocation = wareHouseLocation;
        this.storeLocation = storeLocation;
        this.myDto = new ProductDto(name, buyingCost, sellingCost, category.toString(), manufacturer,
                minimumAmountAllowed, wareHouseLocation, storeLocation, productController, fromDB);
        if (!fromDB)
            this.myDto.persist();
    }

    public void changeProductName(String newName)
    {
        this.name = newName;
    }
    public void changeProductBuyingCost(double newBuyingCost)
    {
        this.buyingCost = newBuyingCost;
        myDto.updateBuyingCost(newBuyingCost);
    }
    public void changeProductSellingCost(double newSellingCost)
    {
        this.sellingCost = newSellingCost;
        myDto.updateSellingCost(newSellingCost);
    }
    public void changeProductCategories(Category category)
    {
        this.category = category;
        this.myDto.updateCategory(category.toString());

    }
    public void changeMinimumAmountAllowed(int newMinimumAmountAllowed){
        this.minimumAmountAllowed = newMinimumAmountAllowed;
    }

    public String getName()
    {
        return this.name;
    }
    public double getBuyingCost()
    {
        return this.buyingCost;
    }
    public double getSellingCost()
    {
        return this.sellingCost;
    }

    public Category getCategory() {
        return this.category;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public int getMinimalAmount(){
        return this.minimumAmountAllowed;
    }
    public boolean isInCategory(Category category){

        return this.category.equals(category);
    }
    public boolean isInCategory(String category){
        return this.category.getFirstCat().equals(category) ||
                this.category.getSecondCat().equals(category) ||
                this.category.getThirdCat().equals(category);
    }
    public void deleteYourself(){
        myDto.deleteProduct();
    }


}
