package org.example.DomainLayerInventory;

import org.example.DataAccessInventory.ControllerClasses.DiscountController;
import org.example.DataAccessInventory.ControllerClasses.ItemController;
import org.example.DataAccessInventory.ControllerClasses.OrderController;
import org.example.DataAccessInventory.ControllerClasses.ProductController;

import java.time.LocalDate;

import java.util.*;

public class Inventory {
    //TODO:: where do we use the method "checkForExpiredProduct"?
    private final Map<String, Product> products;
    private final Map<Integer,Item> allItems;
    private  final  List<Order> orders;
    private final CallBack callBack;
    private final List<Discount> discounts;
    private int currDiscountId;
    private int currOrderId;

    private final ItemController itemController;
    private final OrderController orderController;
    private final ProductController productController;
    private final DiscountController discountController;





    public Inventory(CallBack callBack, ItemController itemController, OrderController orderController,
     ProductController productController, DiscountController discountController) {
        this.allItems = new HashMap<>();
        this.products = new HashMap<>();
        this.discounts = new ArrayList<>();
        this.orders = new LinkedList<>();
        this.callBack = callBack;
        this.currOrderId = 0;
        this.currDiscountId = 0;
        this.itemController = itemController;
        this.orderController = orderController;
        this.productController = productController;
        this.discountController = discountController;


    }

    private void checkTomorrowOrders() {
        for (Order o: orders){
            String productName = o.getProductName();
            if(o.isTomorrow() && o.getAmount()+ getAmountInInventory(productName)<products.get(productName).getMinimalAmount()){
                callBack.call("order of " + productName+ " is for tomorrow and there weren't enough for the minimal amount," +
                        " order was updated, please notify the supplier");
                o.setAmount(products.get(productName).getMinimalAmount());
            }
        }
    }


    public void checkForExpiredItems()
    {

    }




    public void notifyDamagedItem(int itemId){
        if (!allItems.containsKey(itemId)){
            throw new IllegalArgumentException("no such item stupid");
        }
        allItems.get(itemId).setDamaged(true);

   }
    private void removeItemFromInv(int itemId){
        if (allItems.containsKey(itemId)) {
            String productName = allItems.get(itemId).getProductName();
            checkIfProductIsMissing(productName);
        }
    }

    public void changeProductBuyingCost(String productName, double newBuyingCost)
    {
        if(!products.containsKey(productName))
        {
            throw new IllegalArgumentException("this product does not exists in the system");
        }
        if(newBuyingCost <=0)
        {
            throw new IllegalArgumentException("cost must be a positive number");
        }
        products.get(productName).changeProductBuyingCost(newBuyingCost);
    }

    public void changeProductSellingCost(String productName, double newSellingCost)
    {
        if(!products.containsKey(productName))
        {
            throw new IllegalArgumentException("this product does not exists in the system");
        }
        if(newSellingCost <=0)
        {
            throw new IllegalArgumentException("cost must be a positive number");
        }
        products.get(productName).changeProductSellingCost(newSellingCost);
    }

    public void changeProductCategories(String productName, String[] newCategory)
    {
        if(!products.containsKey(productName))
        {
            throw new IllegalArgumentException("this product does not exists in the system");
        }
        try {
        Category category = new Category(newCategory);
        products.get(productName).changeProductCategories(category);
        }catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public List<String> almostMissingProducts(){
        List<String> output = new LinkedList<>();
        Set<Map.Entry<String,Product>> pairs = products.entrySet();
        for (Map.Entry<String,Product> productEntry : pairs){
            if (this.getAmountInInventory(productEntry.getKey()) <
                    productEntry.getValue().getMinimalAmount()){
                output.add(productEntry.getKey());
            }
        }
        return output;
    }


    public int getAmountInInventory(String productName){
        if (products.get(productName) == null){
            return -1;
        }
        return getStoreProducts().get(productName).size()+ getWarehouseProducts().get(productName).size();
    }



    public String reportByAlmostMissing()
    {
        String ret="Report Of Almost Missing And Missing Products\n";
        for (String productName : almostMissingProducts()){
            ret += "Product name: "+productName + ", Total Amount: "+getAmountInInventory(productName)
                    +", In Store: " + getStoreProducts().get(productName).size()+
                    ", In WareHouse: "+getWarehouseProducts().get(productName).size()+", Minimal Amount Allowed: "
                    +products.get(productName).getMinimalAmount()+".\n";
        }
        return ret;
    }
    public String reportByCategories(List<String> categories){
        try{
        String output = "Report By Categories\n";
        for (String category : categories){
            output += category+ "\n";
            Set<Map.Entry<String,Product>> pairs = products.entrySet();
            for (Map.Entry<String,Product> productEntry : pairs){
                Product currentProduct = productEntry.getValue();
                String productName = productEntry.getKey();
                if (currentProduct.isInCategory(category)) {
                    String row = "Product name: "+productName + ", Total Amount: "+getAmountInInventory(productName)
                            +", In Store: " + getStoreProducts().get(productName).size()+
                            ", In WareHouse: "+getWarehouseProducts().get(productName).size()+", Minimal Amount Allowed: "
                            +products.get(productName).getMinimalAmount();
                    output += row+".\n";
                }
            }
        }
       return output;
        }catch (Exception e){
            throw new IllegalArgumentException("not valid categories");
        }
    }

    public String reportBadProducts()
    {
        String ret = "Report Of Wasted Products\nDamaged Products:\n";
        Set<Map.Entry<String,List<Integer>>> pairs1 = getDamagedItems().entrySet();
        for (Map.Entry<String,List<Integer>> productEntry : pairs1){
            if (productEntry.getValue().size() > 0) {
                ret += "Product name: " + productEntry.getKey() + ", amount of damaged items: " +
                        productEntry.getValue().size() + ".\n";
            }
        }
        ret += "Expired Products:\n";
        Set<Map.Entry<String,List<Integer>>> pairs2 = getExpiredItems().entrySet();
        for (Map.Entry<String,List<Integer>> productEntry : pairs2){
            if (productEntry.getValue().size() > 0) {
                ret += "Product name: " + productEntry.getKey() + ", amount of expired items: " +
                        productEntry.getValue().size() + ".\n";
            }
        }
        return ret;
    }



    public void addProduct(String name, double buyingCost, double sellingCost,
                            String[] stringCategory, String manufacturer, int minimumAmountAllowed,
                           String warehouseLocation, String storeLocation, boolean fromDB) {
        if(products.containsKey(name))
        {
            throw new IllegalArgumentException("this product already exists in the system");
        }
        if(buyingCost <=0)
        {
            throw new IllegalArgumentException("buying cost must be a positive number");
        }
        if(sellingCost <=0)
        {
            throw new IllegalArgumentException("selling cost must be a positive number");
        }
        if(minimumAmountAllowed <=0)
        {
            throw new IllegalArgumentException("minimum amount allowed must be a non-negative number");
        }
        if (!Utils.isValidLocation(warehouseLocation)){
            throw new IllegalArgumentException("warehouse location must be a letter followed by a number like 'D5'");
        }
        if (!Utils.isValidLocation(storeLocation)){
            throw new IllegalArgumentException("store location must be a letter followed by a number like 'D5'");
        }
        if (stringCategory.length != 3){
            throw new IllegalArgumentException("category must contain 3 sub categories ");
        }
        Category category = new Category(stringCategory);
        products.put(name,new Product(name,buyingCost,sellingCost,category,manufacturer,minimumAmountAllowed,
                warehouseLocation,storeLocation,productController, fromDB));
    }



    public void addItem(String productName,int itemID, String expiredDate,boolean isInWareHouse,boolean isDamaged, boolean fromDB){
        if(!products.containsKey(productName))
        {
            throw new IllegalArgumentException("this product does not exists in the system");
        }
        LocalDate date = Utils.parseDate(expiredDate);
        if(date == null)
        {
            throw new IllegalArgumentException("expired date Must be of type dd/mm/yyyy");
        }

        Item myItem = new Item(productName,itemID,date,isInWareHouse,isDamaged,itemController,  fromDB);
        allItems.put(itemID,myItem);

    }

    public double getProductPrice(String productName){
        if (!isProductExist(productName)){
            throw new IllegalArgumentException("no such product - "+ productName);
        }
        Product product = products.get(productName);
        List<Discount> discountsToClients = new LinkedList<>();
        for (Discount d: discounts){
            if (d.isToClients()){
                discountsToClients.add(d);
            }
        }
        return Utils.getBestDiscountedPrice(product.getSellingCost(),product,discountsToClients);
    }


    public void removeProductFromEverywhere(String productName)
    {
        if(!products.containsKey(productName))
        {
            throw new IllegalArgumentException("product doesn't exist");
        }
        Product producToRemove = products.remove(productName);
        producToRemove.deleteYourself();


        Iterator<Map.Entry<Integer, Item>> iterator = allItems.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Item> entry = iterator.next();
            if (entry.getValue().getProductName().equals(productName)) {
                Item itemToRemove = entry.getValue();
                itemToRemove.deleteYourself();
                iterator.remove();
            }
        }
    }




    public void buyItem(int itemId){
        if (!allItems.containsKey(itemId)){
            throw new IllegalArgumentException("no such item in the system");
        }
        String productName =allItems.get(itemId).getProductName();
        if (!getStoreProducts().get(productName).contains(itemId) &
                !getWarehouseProducts().get(productName).contains(itemId)){
            throw new IllegalArgumentException("no such item in the store");
        }
        boolean success = removeItemFromEverywhere(itemId);
        if(!success)
        {
            throw new IllegalArgumentException("Item doesn't exist");
        }
    }

    private boolean removeItemFromEverywhere(int itemId) {
        if (!allItems.containsKey(itemId)) {
            return false;
        }

        Item item = allItems.remove(itemId);
        item.deleteYourself();
        String productName = item.getProductName();

        checkIfProductIsMissing(productName);
        return true;
    }



    public void addDiscount(String startDateString, String endDateString,
                            List<String[]> stringCategoryList, double discountParameter
    ,boolean inStore,List<String> productNames,int existingId, boolean fromDB) {
        LocalDate startDate = Utils.parseDate(startDateString);
        if(startDate == null)
        {
            throw new IllegalArgumentException("start date Must be of type dd/mm/yyyy");
        }
        LocalDate endDate = Utils.parseDate(endDateString);
        if(endDateString == null)
        {
            throw new IllegalArgumentException("end date Must be of type dd/mm/yyyy");
        }
        if(discountParameter>= 1| discountParameter<= 0)
        {
            throw new IllegalArgumentException("discount parameter must be a number between 0 and 1");
        }
        Category[] categoryArr = new Category[stringCategoryList.size()];
        for (int i = 0; i < stringCategoryList.size(); i++) {
            String[] stringCategory = stringCategoryList.get(i);
            if (stringCategory.length != 3) {
                throw new IllegalArgumentException("category must contain 3 sub categories ");
            }
            categoryArr[i] = new Category(stringCategory);
        }
        for (String s:productNames){
            if (!isProductExist(s)){
                throw new IllegalArgumentException("the product "+ s+ " does not exist in the system");
            }
        }

        if (existingId == -1){
            discounts.add(new Discount(startDate, endDate, categoryArr, discountParameter,productNames,
                    inStore,currDiscountId,discountController, fromDB));
            currDiscountId++;
        }
        else{
            discounts.add(new Discount(startDate, endDate, categoryArr, discountParameter,productNames,
                    inStore,existingId,discountController,fromDB));
            if (existingId>= currDiscountId){
                currDiscountId = existingId +1;
            }
        }


    }

    public double getProductSupplierCost(String productName) {
        if (!isProductExist(productName)){
            throw new IllegalArgumentException("no such product - "+ productName);
        }
        Product product = products.get(productName);
        List<Discount> discountsFromSuppliers = new LinkedList<>();
        for (Discount d: discounts){
            if (!d.isToClients()){
                discountsFromSuppliers.add(d);
            }
        }
        return Utils.getBestDiscountedPrice(product.getBuyingCost(),product,discountsFromSuppliers);
    }
    public boolean isItemExist(int itemId){
        return allItems.containsKey(itemId);
    }
    public boolean isProductExist(String productName){
        return products.containsKey(productName);
    }

    public void removeDiscount(int discountNumber) {
        int index = discountNumber - 1;

        if (index >= discounts.size() || index < 0)
            throw new IllegalArgumentException("no such discount");
        Discount toRemove = discounts.remove(index);
        toRemove.deleteYourself();
    }

    public String showDiscounts() {
        return Utils.showDiscounts(discounts);
    }

    public void moveItemFromWarehouse(int itemID) {
        if (!isItemExist(itemID)){
            throw new IllegalArgumentException("no such item in the inventory");
        }
        if (allItems.get(itemID).isDamaged()||allItems.get(itemID).isExpired()){
            throw new IllegalArgumentException("item is defected please remove it");
        }
        if(!allItems.get(itemID).isInWareHouse()){
            throw new IllegalArgumentException("no such item in the warehouse");
        }
        allItems.get(itemID).setInWareHouse(false);
    }

    public void moveItemToWarehouse(int itemID) {
        if (!isItemExist(itemID)){
            throw new IllegalArgumentException("no such item in the inventory");
        }
        if (allItems.get(itemID).isDamaged()||allItems.get(itemID).isExpired()){
            throw new IllegalArgumentException("item is defected please remove it");
        }
        if(allItems.get(itemID).isInWareHouse()){
            throw new IllegalArgumentException("no such item in the store's shelves");
        }
        allItems.get(itemID).setInWareHouse(true);
    }


    public String getProductLocation(String productName,boolean inWarehouse) {
        if (!isProductExist(productName)){
            throw new IllegalArgumentException("no such product in the inventory");
        }
        if (inWarehouse){

            return products.get(productName).getWareHouseLocation();
        }
        else {
            return products.get(productName).getStoreLocation();
        }
    }
    public void addOrder(String productName, int amount, int dayOfMonth,int existingId, boolean fromDB){
        if (!isProductExist(productName)){
            throw new IllegalArgumentException("no such product in the inventory");
        }
        if (amount<= 0){
            throw new IllegalArgumentException("amount must be positive");
        }
        if (dayOfMonth >28 || (dayOfMonth <1 && dayOfMonth != -1)){
            throw new IllegalArgumentException("day of month must be between 1 and 28 for convenience");
        }
        if (existingId == -1){
            orders.add(new Order(productName,amount,dayOfMonth,currOrderId,orderController,  fromDB));
            currOrderId++;
        }
        else{
            orders.add(new Order(productName,amount,dayOfMonth,existingId,orderController,  fromDB));
            if (existingId >= currOrderId){
                currOrderId = existingId +1;
            }
        }
    }
    private void checkIfProductIsMissing(String productName){
        if (getAmountInInventory(productName)< products.get(productName).getMinimalAmount()){
            addOrder(productName,products.get(productName).getMinimalAmount(),-1,-1,false);
            callBack.call("Warning! the product "+ productName+" has only "+
                    getAmountInInventory(productName)+" items in store, when the minimal amount allowed is "+
                    products.get(productName).getMinimalAmount()+" a one time order was added");
        }
    }
    public String showOrders(){
        String output = "Orders: \n";
        int counter = 1;
        for(Order o :orders){
            output += counter+". "+ o+"\n";
            counter++;
        }
        return output;
    }
    public void removeOrder(int index){
        int actualIdx = index -1;
        if (actualIdx<0 || actualIdx >= orders.size()){
            throw new IllegalArgumentException("not one of the indexes provided");
        }
        Order orderToDelete = orders.remove(actualIdx);
        orderToDelete.deleteYourself();
    }

    public String updateOrder (int index,int newAmount){
        int actualIdx = index -1;
        if (actualIdx<0 || actualIdx >= orders.size()){
            throw new IllegalArgumentException("not one of the indexes provided");
        }
        Order order = orders.get(actualIdx);
        if (order.isToday()){
            throw new IllegalArgumentException("the order is due to today and you cannot edit it");
        }
        order.setAmount(newAmount);
        return "please notify the supplier that order of "+ order.getProductName()+" was updated.";
    }

    public String getItemProductName(int itemId) {
        if (!allItems.containsKey(itemId)){
            throw new IllegalArgumentException("no such item");
        }
        return allItems.get(itemId).getProductName();
    }

    public void refresh(){
        checkForExpiredItems();
        checkTomorrowOrders();
    }

    private Map<String, List<Integer>> getStoreProducts() {
        Map<String, List<Integer>> output = generateProductMap();
        for (int itemId: allItems.keySet()){
            Item curr = allItems.get(itemId);
            if(!curr.isExpired() &!curr.isDamaged() &!curr.isInWareHouse()& output.containsKey(curr.getProductName())){
                output.get(curr.getProductName()).add(itemId);
            }
        }
        return output;
    }

    private Map<String,List<Integer>> getExpiredItems() {
        Map<String, List<Integer>> output = generateProductMap();
        for (int itemId: allItems.keySet()){
            Item curr = allItems.get(itemId);
            if(curr.isExpired() & output.containsKey(curr.getProductName())){
                //System.out.println(curr.getExpiredDate().getMonth());
                output.get(curr.getProductName()).add(itemId);
            }
        }
        return output;
    }
    private Map<String, List<Integer>> getWarehouseProducts() {
        Map<String, List<Integer>> output = generateProductMap();
        for (int itemId: allItems.keySet()){
            Item curr = allItems.get(itemId);
            if(!curr.isExpired() &!curr.isDamaged() &curr.isInWareHouse()& output.containsKey(curr.getProductName())){
                output.get(curr.getProductName()).add(itemId);
            }
        }
        return output;
    }

    private Map<String, List<Integer>> getDamagedItems() {
        Map<String, List<Integer>> output = generateProductMap();
        for (int itemId: allItems.keySet()){
            Item curr = allItems.get(itemId);
            if(curr.isDamaged() & output.containsKey(curr.getProductName())){
                output.get(curr.getProductName()).add(itemId);
            }
        }
        return output;
    }
    private Map<String, List<Integer>> generateProductMap(){
        Map<String, List<Integer>> output = new HashMap<>();
        for (String productName : products.keySet()){
            output.put(productName,new LinkedList<>());
        }
        return output;
    }


    public int productsAmount(){
        return products.size();
    }
    public int itemsAmount(){
        return allItems.size();
    }
    public int discountsAmount(){
        return discounts.size();
    }
    public int ordersAmount(){
        return orders.size();
    }
}

