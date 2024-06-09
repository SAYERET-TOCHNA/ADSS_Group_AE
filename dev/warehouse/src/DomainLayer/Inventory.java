package DomainLayer;

import java.time.LocalDate;

import java.util.*;

public class Inventory {
    //TODO:: where do we use the method "checkForExpiredProduct"?
    private final Map<Integer,Item> allItems;
    private final Map<String,List<Integer>> storeProducts;       //product-name : list-of-item-ids
    private final Map<String,List<Integer>> warehouseProducts;   //product-name : list-of-item-ids
    private final Map<String, Product> products;                 //product-name : product
    private final Map<String,String>productsStoreLocation;      //product-name : string-that-represent-the-location-with-letter-and-number
    private final Map<String,String>productsWarehouseLocation;  //product-name : string-that-represent-the-location-with-letter-and-number
    private final Map<String, List<Integer>> damagedItems;    //product-name : list-of-item-ids
    private final Map<String, List<Integer>> expiredItems;    //product-name : list-of-item-ids
    private final List<Discount> discountsToClients;
    private final List<Discount> discountsFromSuppliers;


    public Inventory() {
        this.allItems = new HashMap<>();
        this.storeProducts = new HashMap<>();
        this.warehouseProducts = new HashMap<>();
        this.products = new HashMap<>();
        this.productsStoreLocation = new HashMap<>();
        this.productsWarehouseLocation = new HashMap<>();
        //this.badItems = new HashMap<>();
        this.damagedItems = new HashMap<>();
        this.expiredItems = new HashMap<>();
        this.discountsToClients = new ArrayList<>();
        this.discountsFromSuppliers = new ArrayList<>();
    }

    public List<String> loadData(String configFile){
        List<String> output = processFileContent(configFile);
        checkForExpiredItems();
        return output;
    }


    public void checkForExpiredItems()
    {
        Set<Map.Entry<String,List<Integer>>> pairs = storeProducts.entrySet();
        for (Map.Entry<String,List<Integer>> productEntry : pairs) {
            for (Integer id : productEntry.getValue())
            {
                Item item = allItems.get(id);
                if(item.isExpired())
                {
                    if(!expiredItems.containsKey(item.getProductName()))
                    {
                        expiredItems.put(item.getProductName(), new LinkedList<>());
                    }
                    expiredItems.get(item.getProductName()).add(id);
                    removeItemFromInv(id);
                }
            }
        }
    }




   public void notifyDamagedItem(int itemId){
        if (!allItems.containsKey(itemId)){
            throw new IllegalArgumentException("no such item stupid");
        }
        damagedItems.get(allItems.get(itemId).getProductName()).add(itemId);
        //badItems.get(AllItems.get(itemId).getProductName()).add(itemId);

   }

    private void removeItemFromInv(int itemId){
        if (allItems.containsKey(itemId)) {
            String productName = allItems.get(itemId).getProductName();
            storeProducts.get(productName).remove((Integer)itemId);
            warehouseProducts.get(productName).remove((Integer) itemId);
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
            //throw
        }
        return storeProducts.get(productName).size()+ warehouseProducts.get(productName).size();
    }

    public String reportByAlmostMissing()
    {
        String ret="Report Of Almost Missing And Missing Products\n";
        for (String productName : almostMissingProducts()){
            ret += "Product name: "+productName + ", Total Amount: "+getAmountInInventory(productName)
                    +", In Store: " + storeProducts.get(productName).size()+
                    ", In WareHouse: "+warehouseProducts.get(productName).size()+", Minimal Amount Allowed: "
                    +products.get(productName).getMinimalAmount()+".\n";
        }
        return ret;
    }
    public String reportByCategories(List<String[]> StringCategories){
        try{
            List<Category> categories = new LinkedList<>();
            for (String[] stringCat : StringCategories ){
                categories.add(new Category(stringCat));
            }

        String output = "Report By Categories\n";
        for (Category category : categories){
            output += category+ "\n";
            Set<Map.Entry<String,Product>> pairs = products.entrySet();
            for (Map.Entry<String,Product> productEntry : pairs){
                Product currentProduct = productEntry.getValue();
                String productName = productEntry.getKey();
                if (currentProduct.isInCategory(category)) {
                    String row = "Product name: "+productName + ", Total Amount: "+getAmountInInventory(productName)
                            +", In Store: " + storeProducts.get(productName).size()+
                            ", In WareHouse: "+warehouseProducts.get(productName).size()+", Minimal Amount Allowed: "
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
        Set<Map.Entry<String,List<Integer>>> pairs1 = damagedItems.entrySet();
        for (Map.Entry<String,List<Integer>> productEntry : pairs1){
            if (productEntry.getValue().size() > 0) {
                ret += "Product name: " + productEntry.getKey() + ", amount of damaged products: " +
                        productEntry.getValue().size() + ".\n";
            }
        }
        ret += "Expired Products:\n";
        Set<Map.Entry<String,List<Integer>>> pairs2 = expiredItems.entrySet();
        for (Map.Entry<String,List<Integer>> productEntry : pairs2){
            if (productEntry.getValue().size() > 0) {
                ret += "Product name: " + productEntry.getKey() + ", amount of expired products: " +
                        productEntry.getValue().size() + ".\n";
            }
        }
        return ret;
    }

    public void addProduct(String name, double buyingCost, double sellingCost,
                            String[] stringCategory, String manufacturer, int minimumAmountAllowed,
                           String warehouseLocation, String storeLocation) {
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
        products.put(name,new Product(name,buyingCost,sellingCost,category,manufacturer,minimumAmountAllowed));
        warehouseProducts.put(name, new LinkedList<>());
        storeProducts.put(name, new LinkedList<>());
        damagedItems.put(name, new LinkedList<>());
        expiredItems.put(name, new LinkedList<>());

        this.productsStoreLocation.put(name, storeLocation);
        this.productsWarehouseLocation.put(name, warehouseLocation);
    }

    public void addItem(String productName,int itemID, String expiredDate,boolean isInWareHouse){
        if(!products.containsKey(productName))
        {
            throw new IllegalArgumentException("this product does not exists in the system");
        }
        LocalDate date = Utils.parseDate(expiredDate);
        if(date == null)
        {
            throw new IllegalArgumentException("expired date Must be of type dd/mm/yyyy");
        }
        if(date.isBefore(LocalDate.now()))
        {
            expiredItems.get(productName).add(itemID);
        }
        Item myItem = new Item(productName,itemID,date);
        if (isInWareHouse){
            warehouseProducts.get(productName).add(itemID);
        }
        else
        {
            storeProducts.get(productName).add(itemID);
        }
        allItems.put(itemID,myItem);

    }
    public double getItemPrice(int itemId){
        if (!isItemExist(itemId)){
            throw new IllegalArgumentException("no such item");
        }
        return getProductPrice(allItems.get(itemId).getProductName());
    }
    // no double discount
    public double getProductPrice(String productName){
        if (!isProductExist(productName)){
            throw new IllegalArgumentException("no such product - "+ productName);
        }
        Product product = products.get(productName);

        return Utils.getBestDiscountedPrice(product.getSellingCost(),product,discountsToClients);
    }


    public void removeProductFromEverywhere(String productName)
    {
        if(!products.containsKey(productName))
        {
            throw new IllegalArgumentException("product doesn't exist");
        }
        storeProducts.remove(productName);
        warehouseProducts.remove(productName);
        products.remove(productName);
        productsStoreLocation.remove(productName);
        productsWarehouseLocation.remove(productName);
        expiredItems.remove(productName);
        damagedItems.remove(productName);

        Iterator<Map.Entry<Integer, Item>> iterator = allItems.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Item> entry = iterator.next();
            if (entry.getValue().getProductName().equals(productName)) {
                iterator.remove();
            }
        }
    }




    public void buyItem(int itemId){
        if (!allItems.containsKey(itemId)){
            throw new IllegalArgumentException("no such item in the system");
        }
        String productName =allItems.get(itemId).getProductName();
        if (!storeProducts.get(productName).contains(itemId) &
                !warehouseProducts.get(productName).contains(itemId)){
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
        String productName = item.getProductName();

        if (storeProducts.get(productName) != null && storeProducts.get(productName).contains(itemId)) {
            storeProducts.get(productName).remove((Integer) itemId);
        }
        if (warehouseProducts.get(productName) != null && warehouseProducts.get(productName).contains(itemId)) {
            warehouseProducts.get(productName).remove((Integer) itemId);
        }
        //if (badItems.get(productName) != null) {
        //    badItems.get(productName).remove((Integer) itemId);
        //}
        if (expiredItems.get(productName) != null && expiredItems.get(productName).contains(itemId)) {
            expiredItems.get(productName).remove((Integer) itemId);
        }
        if (damagedItems.get(productName) != null && damagedItems.get(productName).contains(itemId)) {
            damagedItems.get(productName).remove((Integer) itemId);
        }

        if (getAmountInInventory(productName)< products.get(productName).getMinimalAmount()){
            System.out.println("Warning! the product "+ productName+" has only "+
                    getAmountInInventory(productName)+" items in store, when the minimal amount allowed is "+
                    products.get(productName).getMinimalAmount());
        }
        return true;
    }


    public List<String> processFileContent(String fileContent){
        try {
            List<String> badLines = new LinkedList<>();
            String[] lines = fileContent.split("\n");
            for (String line : lines) {
                String[] params = line.split(",");

                if (params.length == 8) {
                    try {
                        // It's a product
                        String name = params[0].trim();
                        double buyingCost = Double.parseDouble(params[1].trim());
                        double sellingCost = Double.parseDouble(params[2].trim());
                        String[] stringCategory = params[3].trim().split("\\|");
                        String manufacturer = params[4].trim();
                        int minimumAmountAllowed = Integer.parseInt(params[5].trim());
                        String warehouseLocation = params[6].trim();
                        String storeLocation = params[7].trim();

                        addProduct(name, buyingCost, sellingCost, stringCategory,
                                manufacturer, minimumAmountAllowed, warehouseLocation, storeLocation);
                    }catch (Exception e){
                        badLines.add("Line: "+line +" Error Message: " + e.getMessage());
                    }
                } else if (params.length == 4) {
                    // It's an item
                    try{
                    String productName = params[0].trim();
                    int itemID = Integer.parseInt(params[1].trim());
                    String expiredDate = params[2].trim();
                    boolean isInWarehouse = Boolean.parseBoolean(params[3].trim());
                    addItem(productName, itemID, expiredDate, isInWarehouse);
                    }catch (Exception e){
                        badLines.add("Line: "+line +" Error Message: " + e.getMessage());
                    }
                }
                else if (params.length == 6) {
                    // It's a discount
                    try{
                        String startDate = params[0].trim();
                        String endDate = params[1].trim();
                        String[] categoriesConnected  = params[2].trim().split("'");
                        List<String[]> categoryList = new LinkedList<>();
                        for (String stringCat : categoriesConnected){
                            categoryList.add(stringCat.split("\\|"));
                        }
                        double discount = Double.parseDouble(params[3].trim());
                        boolean isStore = Boolean.parseBoolean(params[4].trim());
                        List<String> productNames = Arrays.asList(params[5].trim().split("\\|"));
                        addDiscount(startDate,endDate,categoryList,discount,isStore,productNames);
                    }catch (Exception e){
                        badLines.add("Line: "+line +" Error Message: " + e.getMessage());
                    }
                }
                else badLines.add(line);
            }
            return badLines;
        }catch (Exception e){
            throw new IllegalArgumentException("Data could not be processed");
        }
    }

    public void addDiscount(String startDateString, String endDateString,
                            List<String[]> stringCategoryList, double discountParameter
    ,boolean inStore,List<String> productNames) {
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

        if (inStore) {
            discountsToClients.add(new Discount(startDate, endDate, categoryArr, discountParameter,productNames));
        }
        else discountsToClients.add(new Discount(startDate, endDate, categoryArr, discountParameter,productNames));
    }

    public double getItemSupplierCost(int itemId) {
        if (!isItemExist(itemId)){
            throw new IllegalArgumentException("no such item");
        }
        return getProductSupplierCost(allItems.get(itemId).getProductName());
    }

    public double getProductSupplierCost(String productName) {
        if (!isProductExist(productName)){
            throw new IllegalArgumentException("no such product - "+ productName);
        }
        Product product = products.get(productName);

        return Utils.getBestDiscountedPrice(product.getBuyingCost(),product,discountsFromSuppliers);
    }
    public boolean isItemExist(int itemId){
        return allItems.containsKey(itemId);
    }
    public boolean isProductExist(String productName){
        return products.containsKey(productName);
    }

    public void removeDiscount(boolean isStore, int discountNumber) {
        int index = discountNumber - 1;
        if(isStore){
            if (index >= discountsToClients.size() || index < 0)
                throw new IllegalArgumentException("no such discount");
            discountsToClients.remove(index);
        }
        else{
            if (index >= discountsFromSuppliers.size() || index < 0)
                throw new IllegalArgumentException("no such discount");
            discountsFromSuppliers.remove(index);
        }
    }

    public String showDiscounts(boolean isStore) {
        if (isStore){
            return Utils.showDiscounts(discountsToClients);
        }
        return Utils.showDiscounts(discountsFromSuppliers);
    }

    public void moveItemFromWarehouse(int itemID) {
        if (!isItemExist(itemID)){
            throw new IllegalArgumentException("no such item in the inventory");
        }
        String productName = allItems.get(itemID).getProductName();
        if(!warehouseProducts.get(productName).contains(itemID)){
            throw new IllegalArgumentException("no such item in the warehouse");
        }
        warehouseProducts.get(productName).remove((Integer) itemID);
        storeProducts.get(productName).add(itemID);
    }

    public void moveItemToWarehouse(int itemID) {
        if (!isItemExist(itemID)){
            throw new IllegalArgumentException("no such item in the inventory");
        }
        String productName = allItems.get(itemID).getProductName();
        if(!storeProducts.get(productName).contains(itemID)){
            throw new IllegalArgumentException("no such item in the store's shelves");
        }
        storeProducts.get(productName).remove((Integer) itemID);
        warehouseProducts.get(productName).add(itemID);
    }

    public String getItemLocation(int itemID) {
        if (!isItemExist(itemID)){
            throw new IllegalArgumentException("no such item in the inventory");
        }
        String productName = allItems.get(itemID).getProductName();
        if (storeProducts.get(productName).contains(itemID)){
            return getProductLocation(productName,false);
        }
        else{
            return getProductLocation(productName,true);
        }
    }
    public String getProductLocation(String productName,boolean inWarehouse) {
        if (!isProductExist(productName)){
            throw new IllegalArgumentException("no such product in the inventory");
        }
        if (inWarehouse){
            if (!productsWarehouseLocation.containsKey(productName)){
                throw new IllegalArgumentException("no such product in the warehouse");
            }
            return productsWarehouseLocation.get(productName);
        }
        else {
            if (!productsStoreLocation.containsKey(productName)){
                throw new IllegalArgumentException("no such product in the store's shelves");
            }
            return productsStoreLocation.get(productName);
        }
    }
}

