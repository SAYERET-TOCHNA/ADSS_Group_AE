package PresentationLayer;

import DomainLayer.InventoryFacade;
import DomainLayer.CallBack;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommandLineUI {
    private final Scanner scanner = new Scanner(System.in);
    private final InventoryFacade manager;

    private CallBack callBack = new CallBack() {
        @Override
        public void call(String msg) {
            System.out.println(msg);
        }
    };



    public CommandLineUI() {
        manager = new InventoryFacade(callBack);
        System.out.println(manager.init());
    }

    public static void main(String[] args) {
        CommandLineUI ui = new CommandLineUI();


        while (true) {
            ui.showMainMenu();
            int mainChoice = ui.getChoice();
            ui.handleMainChoice(mainChoice);
        }
    }

    private void showMainMenu() {
        // ANSI escape codes for different rainbow colors
        final String RESET = "\u001B[0m";
        final String[] RAINBOW_COLORS = {
                "\u001B[38;2;255;0;0m",     // Red
                "\u001B[38;2;255;127;0m",   // Orange
                "\u001B[38;2;255;255;0m",   // Yellow
                "\u001B[38;2;0;255;0m",     // Green
                "\u001B[38;2;0;0;255m",     // Blue
                "\u001B[38;2;75;0;130m",    // Indigo
                "\u001B[38;2;148;0;211m"    // Violet
        };

        System.out.println(RAINBOW_COLORS[3] + "\n=== Inventory Management System ===" + RESET);

        String[] mainMenuOptions = {
                "1. Products",
                "2. Items",
                "3. Discounts",
                "4. Orders",
                "5. Reports",
                "6. Delete Everything",
                "7. Exit"
        };

        // Print colored main menu options
        for (int i = 0; i < mainMenuOptions.length; i++) {
            String color = i == 0 || i == mainMenuOptions.length - 1 ? RAINBOW_COLORS[0] : RAINBOW_COLORS[i % RAINBOW_COLORS.length];
            System.out.println(color + mainMenuOptions[i].substring(0, 3) + RESET + mainMenuOptions[i].substring(3));
        }
        System.out.print("Enter your choice: ");
    }

    private void showSubMenu(String[] subMenuOptions) {
        // ANSI escape codes for different rainbow colors
        final String RESET = "\u001B[0m";
        final String[] RAINBOW_COLORS = {
                "\u001B[38;2;255;0;0m",     // Red
                "\u001B[38;2;255;127;0m",   // Orange
                "\u001B[38;2;255;255;0m",   // Yellow
                "\u001B[38;2;0;255;0m",     // Green
                "\u001B[38;2;0;0;255m",     // Blue
                "\u001B[38;2;75;0;130m",    // Indigo
                "\u001B[38;2;148;0;211m"    // Violet
        };

        // Print colored sub-menu options
        for (int i = 0; i < subMenuOptions.length; i++) {
            String color = i == 0 || i == subMenuOptions.length - 1 ? RAINBOW_COLORS[0] : RAINBOW_COLORS[i % RAINBOW_COLORS.length];
            System.out.println(color + subMenuOptions[i].substring(0, 3) + RESET + subMenuOptions[i].substring(3));
        }
        System.out.print("Enter your choice: ");
    }

    private int getChoice() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                System.out.print("Enter your choice: ");
            }
        }
    }

    private void handleMainChoice(int choice) {
        switch (choice) {
            case 1 -> handleProducts();
            case 2 -> handleItems();
            case 3 -> handleDiscounts();
            case 4 -> handleOrders();
            case 5 -> handleReports();
            case 6 -> handleDeleteEverything();
            case 7 -> exit();
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    private void handleDeleteEverything() {
        System.out.println("WARNING!!! you are about to delete all the database \ntype 'delete' to confirm");
        String confirm = scanner.nextLine();
        if(confirm.equals("delete")){
            System.out.println(manager.deleteAllFromDB());
        }
        else System.out.println("Understood, data deletion was canceled");
    }

    private void handleProducts() {
        String[] productMenuOptions = {
                "1. Add Product",
                "2. Change Product Buying Cost",
                "3. Change Product Selling Cost",
                "4. Change Product Categories",
                "5. Get Product Cost",
                "6. Get Product Supplier Cost",
                "7. Remove Product",
                "8. Get Product Location",
                "9. Back to Main Menu"
        };
        while (true) {
            showSubMenu(productMenuOptions);
            int choice = getChoice();
            switch (choice) {
                case 1 -> addProduct();
                case 2 -> changeProductBuyingCost();
                case 3 -> changeProductSellingCost();
                case 4 -> changeProductCategories();
                case 5 -> getProductCost();
                case 6 -> getProductSupplierCost();
                case 7 -> removeProduct();
                case 8 -> getProductLocation();
                case 9 -> { return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleItems() {
        String[] itemMenuOptions = {
                "1. Add Item",
                "2. Notify Item Bought",
                "3. Notify Damaged Item",
                "4. Move Item From Warehouse",
                "5. Move Item To Warehouse",
                "6. Get Item Product Name",
                "7. Back to Main Menu"
        };
        while (true) {
            showSubMenu(itemMenuOptions);
            int choice = getChoice();
            switch (choice) {
                case 1 -> addItem();
                case 2 -> notifyItemBought();
                case 3 -> notifyDamagedItem();
                case 4 -> moveItemFromWarehouse();
                case 5 -> moveItemToWarehouse();
                case 6 -> getItemProductName();
                case 7 -> { return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleDiscounts() {
        String[] discountMenuOptions = {
                "1. Add Discount",
                "2. Remove Discount",
                "3. Show Discounts",
                "4. Back to Main Menu"
        };
        while (true) {
            showSubMenu(discountMenuOptions);
            int choice = getChoice();
            switch (choice) {
                case 1 -> addDiscount();
                case 2 -> removeDiscount();
                case 3 -> showDiscounts();
                case 4 -> { return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleOrders() {
        String[] orderMenuOptions = {
                "1. Add an Occasional Order",
                "2. Show Orders",
                "3. Remove Order",
                "4. Update Order",
                "5. Back to Main Menu"
        };
        while (true) {
            showSubMenu(orderMenuOptions);
            int choice = getChoice();
            switch (choice) {
                case 1 -> addAnOccasionalOrder();
                case 2 -> showOrders();
                case 3 -> removeOrder();
                case 4 -> updateOrder();
                case 5 -> { return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleReports() {
        String[] reportMenuOptions = {
                "1. Get Report By Category",
                "2. Get Report Almost Missing",
                "3. Get Report Bad Products",
                "4. Back to Main Menu"
        };
        while (true) {
            showSubMenu(reportMenuOptions);
            int choice = getChoice();
            switch (choice) {

                case 1 -> getReportByCategory();
                case 2 -> getReportAlmostMissing();
                case 3 -> getReportBadProducts();
                case 4 -> { return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void getItemProductName() {
        int itemId = getIntInput("Enter ItemId: ");
        System.out.println(manager.getItemProductName(itemId));
    }

    private void addAnOccasionalOrder() {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        int amount = getIntInput("Enter amount in order: ");
        int dayOfMonth = getIntInput("Enter day of month: (between one and 28)");

        System.out.println(manager.addOrder(name,amount,dayOfMonth));
    }

    private void notifyDamagedItem() {
        int itemId = getIntInput("Enter ItemId: ");
        System.out.println(manager.notifyDamagedItem(itemId));
    }



    private void addProduct() {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        double buyingCost = getDoubleInput("Enter buying cost: ");
        double sellingCost = getDoubleInput("Enter selling cost: ");
        System.out.print("Enter categories separated by , : ");
        String[] categories = scanner.nextLine().split(",");
        System.out.print("Enter manufacturer: ");
        String manufacturer = scanner.nextLine();
        int minimumAmountAllowed = getIntInput("Enter minimum amount allowed: ");
        System.out.print("Enter warehouse location: ");
        String warehouseLocation = scanner.nextLine();
        System.out.print("Enter store location: ");
        String storeLocation = scanner.nextLine();

        System.out.println(manager.addProduct(name, buyingCost, sellingCost, categories, manufacturer, minimumAmountAllowed, warehouseLocation, storeLocation));
    }

    private void addItem() {
        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();
        int itemID = getIntInput("Enter item ID: ");
        System.out.print("Enter expiration date (DD/MM/YYYY): ");
        String expiredDate = scanner.nextLine();
        boolean isInWarehouse = getBooleanInput("Is the item in the warehouse? (true/false): ");

        System.out.println(manager.addItem(productName, itemID, expiredDate, isInWarehouse,false));
    }

    private void addDiscount() {
        System.out.print("Enter start date (DD/MM/YYYY): ");
        String startDate = scanner.nextLine();
        System.out.print("Enter end date (DD/MM/YYYY): ");
        String endDate = scanner.nextLine();

        List<String[]> categories = new ArrayList<>();
        while (true) {
            System.out.print("Enter category separated by , (type 'e' to end): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("e")) {
                break;
            }
            categories.add(input.split(","));
        }

        double discountParameter = getDoubleInput("Enter discount parameter: ");
        boolean inStore = getBooleanInput("Is the discount in-store? (true/false): ");

        List<String> productsNames = new ArrayList<>();
        while (true) {
            System.out.print("Enter product (type 'e' to end): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("e")) {
                break;
            }
            productsNames.add(input);
        }
        System.out.println(manager.addDiscount(startDate, endDate, categories, discountParameter, inStore,productsNames));
    }

    private void getReportByCategory() {
        List<String> categories = new ArrayList<>();
        while (true) {
            System.out.print("Enter a single category (type 'e' to end): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("e")) {
                break;
            }
            categories.add(input);
        }
        System.out.println(manager.getReportByCategory(categories));
    }

    private void getReportAlmostMissing() {
        System.out.println(manager.getReportAlmostMissing());
    }

    private void getReportBadProducts() {
        System.out.println(manager.getReportBadProducts());
    }

    private void changeProductBuyingCost() {
        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();
        double newBuyingCost = getDoubleInput("Enter new buying cost: ");

        System.out.println(manager.changeProductBuyingCost(productName, newBuyingCost));
    }

    private void changeProductSellingCost() {
        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();
        double newSellingCost = getDoubleInput("Enter new selling cost: ");

        System.out.println(manager.changeProductSellingCost(productName, newSellingCost));
    }

    private void changeProductCategories() {
        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();
        System.out.print("Enter new categories separated by , : ");
        String[] newCategories = scanner.nextLine().split(",");

        System.out.println(manager.changeProductCategories(productName, newCategories));
    }



    private void getProductCost() {
        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();
        System.out.println(manager.getProductCost(productName));
    }

    private void notifyItemBought() {
        int itemId = getIntInput("Enter item ID: ");
        System.out.println(manager.notifyItemBought(itemId));
    }



    private void getProductSupplierCost() {
        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();
        System.out.println(manager.getProductSupplierCost(productName));
    }

    private void removeDiscount() {
        String discounts = manager.showDiscounts();
        if (discounts.length() == 0) {
            System.out.println("There are no discounts");
            return;
        }
        System.out.println(discounts);
        int discountNumber = getIntInput("Enter discount number: ");
        System.out.println(manager.removeDiscount(discountNumber));
    }

    private void showDiscounts() {
        System.out.println(manager.showDiscounts());
    }

    private void removeProduct() {
        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();
        System.out.println(manager.removeProduct(productName));
    }
    private void moveItemFromWarehouse(){
        int itemId = getIntInput("Enter item ID: ");
        System.out.println(manager.moveItemFromWarehouse(itemId));
    }
    private void moveItemToWarehouse(){
        int itemId = getIntInput("Enter item ID: ");
        System.out.println(manager.moveItemToWarehouse(itemId));
    }

    private void getProductLocation(){
        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();
        boolean inWareHouse = getBooleanInput("do you want the warehouse location? (true/false): ");
        System.out.println(manager.getProductLocation(productName,inWareHouse));
    }

    private void showOrders() {
        System.out.println(manager.showOrders());
    }

    private void removeOrder() {
        showOrders();
        int index = getIntInput("Enter order index to remove: ");
        System.out.println(manager.removeOrder(index));
    }

    private void updateOrder() {
        showOrders();
        int index = getIntInput("Enter order index to update: ");
        int newAmount = getIntInput("Enter new amount: ");
        System.out.println(manager.updateOrder(index, newAmount));
    }
    private void exit() {
        System.out.println("Exiting the system. Goodbye!");
        scanner.close();
        System.exit(0);
    }

    private int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter an integer.");
            }
        }
    }

    private double getDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private boolean getBooleanInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Boolean.parseBoolean(scanner.nextLine());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input. Please enter 'true' or 'false'.");
            }
        }
    }
}
