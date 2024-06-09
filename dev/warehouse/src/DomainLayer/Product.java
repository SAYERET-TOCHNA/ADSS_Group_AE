package DomainLayer;

public class Product {

    private String name;
    private double buyingCost;
    private double sellingCost;
    private Category category;
    private String manufacturer;
    private int minimumAmountAllowed;

    public Product(String name,double buyingCost, double sellingCost, Category category, String manufacturer, int minimumAmountAllowed)
    {
        this.name = name;
        this.buyingCost = buyingCost;
        this.sellingCost = sellingCost;
        this.category = category;
        this.manufacturer = manufacturer;
        this.minimumAmountAllowed = minimumAmountAllowed;
    }

    public void changeProductName(String newName)
    {
        this.name = newName;
    }
    public void changeProductBuyingCost(double newBuyingCost)
    {
        this.buyingCost = newBuyingCost;
    }
    public void changeProductSellingCost(double newSellingCost)
    {
        this.sellingCost = newSellingCost;
    }
    public void changeProductCategories(Category category)
    {
        this.category = category;
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


}
