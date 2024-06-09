package PresentationLayer;

import DomainLayer.BuisnessManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class CommandLineUI {
    private final Scanner scanner = new Scanner(System.in);
    private final BuisnessManager manager;



    public CommandLineUI(String data) {
        manager = new BuisnessManager();
        System.out.println(manager.init(data));
    }

    public CommandLineUI() {
        manager = new BuisnessManager();
        System.out.println(manager.init());
    }

    public static void main(String[] args) {
        CommandLineUI ui = null;
        if (args.length > 0) {
            List<String> lines = null;
            try {
                // Read all lines from the file
                lines = Files.readAllLines(Paths.get(args[0]));
            } catch (IOException e) {
                System.out.println("Error reading data from path: " + args[0]);
                e.printStackTrace();
            }
            if (lines != null) {
                // Join the lines into a single string if CommandLineUI expects a single string
                String data = String.join(System.lineSeparator(), lines);
                ui = new CommandLineUI(data);
            }
        }
        if (ui == null){
            ui = new CommandLineUI("Apple, 10.5, 15.0, Fruit|Fresh|Small, FreshFarms, 5, D1, S1\n" +
                    "Banana, 8.0, 12.0, Fruit|Fresh|Medium, TropicFruits, 3, D2, S2\n" +
                    "Carrot, 7.5, 10.0, Vegetable|Root|Small, HealthyVeg, 2, D3, S3\n" +
                    "Detergent, 5.5, 9.0, Household|Cleaning|Large, CleanCo, 6, D4, S4\n" +
                    "Shampoo, 12.0, 18.0, PersonalCare|Hair|Medium, HairPlus, 4, D5, S5\n" +
                    "Milk, 2.5, 4.0, Dairy|Fresh|Large, DailyDairy, 8, D6, S6\n" +
                    "Apple, 101, 01/05/2023, true\n" +
                    "Banana, 102, 31/05/2024, false\n" +
                    "Carrot, 103, 01/06/2022, true\n" +
                    "Detergent, 104, 15/08/2023, true\n" +
                    "Shampoo, 105, 20/11/2024, false\n" +
                    "Milk, 106, 01/01/2024, true\n" +
                    "01/06/2024, 31/12/2024, Fruit|Fresh|Small'Vegetable|Root|Small, 0.2, true, Apple|Carrot\n" +
                    "01/06/2024, 31/07/2024, Household|Cleaning|Large'PersonalCare|Hair|Medium, 0.15, false, Detergent|Shampoo\n" +
                    "01/06/2024, 31/08/2024, Dairy|Fresh|Large'Fruit|Fresh|Medium, 0.1, true, Milk|Banana\n" +
                    "Invalid, data, line\n" +
                    "Orange, 9.0, 14.0, Fruit|Citrus|Medium, CitrusWorld, 5, D7, S7\n" +
                    "Tomato, 6.0, 9.0, Vegetable|Fresh|Small, GreenThumb, 4, D8, S8\n" +
                    "Cereal, 3.5, 6.0, Grocery|Breakfast|Packaged, GrainHouse, 7, D9, S9\n" +
                    "Soap, 4.0, 6.5, PersonalCare|Skin|Medium, PureEssence, 6, D1, S1\n" +
                    "Cheese, 8.5, 12.0, Dairy|Fresh|Small, DairyDelight, 5, D1, S1\n" +
                    "Juice, 5.0, 8.0, Beverage|Fresh|Large, JuiceVille, 7, D1, S1\n" +
                    "Orange, 107, 15/02/2024, true\n" +
                    "Tomato, 108, 22/03/2024, false\n" +
                    "Cereal, 109, 30/04/2023, true\n" +
                    "Soap, 110, 12/05/2024, false\n" +
                    "Cheese, 111, 25/06/2024, true\n" +
                    "Juice, 112, 03/07/2023, false\n" +
                    "15/07/2024, 31/12/2024, Fruit|Citrus|Medium'Beverage|Fresh|Large, 0.25, true, Orange|Juice\n" +
                    "01/08/2024, 31/10/2024, Vegetable|Fresh|Small'PersonalCare|Skin|Medium, 0.2, false, Tomato|Soap\n" +
                    "15/09/2024, 31/12/2024, Grocery|Breakfast|Packaged'Dairy|Fresh|Small, 0.18, true, Cereal|Cheese|NonExistentProduct\n");
        }

        while (true) {
            ui.showMenu();
            int choice = ui.getChoice();
            ui.handleChoice(choice);
        }
    }

    private void showMenu() {
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

        String[] menuOptions = {
                "1. Load Data",
                "2. Add Product",
                "3. Add Item",
                "4. Add Discount",
                "5. Get Report By Category",
                "6. Get Report Almost Missing",
                "7. Get Report Bad Products",
                "8. Change Product Buying Cost",
                "9. Change Product Selling Cost",
                "10. Change Product Categories",
                "11. Get Item Cost",
                "12. Get Product Cost",
                "13. Notify Item Bought",
                "14. Get Item Supplier Cost",
                "15. Get Product Supplier Cost",
                "16. Remove Discount",
                "17. Show Discounts",
                "18. Remove Product",
                "19. Notify Damaged Item",
                "20. Move Item From Warehouse",
                "21. Move Item To Warehouse",
                "22. Get Item Location",
                "23. Get product Location",
                "24. Exit"
        };

        // Print colored menu options
        for (int i = 0; i < menuOptions.length; i++) {
            String color = i == 0 || i == menuOptions.length - 1 ? RAINBOW_COLORS[0] : RAINBOW_COLORS[i % RAINBOW_COLORS.length];
            System.out.println(color + menuOptions[i].substring(0, 3) + RESET + menuOptions[i].substring(3));
        }
        System.out.print( "Enter your choice: ");
    }

    private int getChoice() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 24.");
                System.out.print("Enter your choice: ");
            }
        }
    }

    private void handleChoice(int choice) {
        switch (choice) {
            case 1 -> loadData();
            case 2 -> addProduct();
            case 3 -> addItem();
            case 4 -> addDiscount();
            case 5 -> getReportByCategory();
            case 6 -> getReportAlmostMissing();
            case 7 -> getReportBadProducts();
            case 8 -> changeProductBuyingCost();
            case 9 -> changeProductSellingCost();
            case 10 -> changeProductCategories();
            case 11 -> getItemCost();
            case 12 -> getProductCost();
            case 13 -> notifyItemBought();
            case 14 -> getItemSupplierCost();
            case 15 -> getProductSupplierCost();
            case 16 -> removeDiscount();
            case 17 -> showDiscounts();
            case 18 -> removeProduct();
            case 19 -> notifyDamagedItem();
            case 20 -> moveItemFromWarehouse();
            case 21 -> moveItemToWarehouse();
            case 22 -> getItemLocation();
            case 23 -> getProductLocation();
            case 24 -> exit();
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    private void notifyDamagedItem() {
        int itemId = getIntInput("Enter ItemId: ");
        System.out.println(manager.notifyDamagedItem(itemId));
    }

    private void loadData() {
        System.out.print("Enter data to load: ");
        String data = scanner.nextLine();
        System.out.println(manager.loadData(data));
    }

    private void addProduct() {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        double buyingCost = getDoubleInput("Enter buying cost: ");
        double sellingCost = getDoubleInput("Enter selling cost: ");
        System.out.print("Enter categories (comma separated): ");
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
        System.out.print("Enter expiration date (YYYY-MM-DD): ");
        String expiredDate = scanner.nextLine();
        boolean isInWarehouse = getBooleanInput("Is the item in the warehouse? (true/false): ");

        System.out.println(manager.addItem(productName, itemID, expiredDate, isInWarehouse));
    }

    private void addDiscount() {
        System.out.print("Enter start date (YYYY-MM-DD): ");
        String startDate = scanner.nextLine();
        System.out.print("Enter end date (YYYY-MM-DD): ");
        String endDate = scanner.nextLine();

        List<String[]> categories = new ArrayList<>();
        while (true) {
            System.out.print("Enter category (type 'e' to end): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("e")) {
                break;
            }
            categories.add(input.split("\\|"));
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
        List<String[]> categories = new ArrayList<>();
        while (true) {
            System.out.print("Enter category (type 'e' to end): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("e")) {
                break;
            }
            categories.add(input.split("\\|"));
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
        System.out.print("Enter new categories (comma separated): ");
        String[] newCategories = scanner.nextLine().split(",");

        System.out.println(manager.changeProductCategories(productName, newCategories));
    }

    private void getItemCost() {
        int itemId = getIntInput("Enter item ID: ");
        System.out.println(manager.getItemCost(itemId));
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

    private void getItemSupplierCost() {
        int itemId = getIntInput("Enter item ID: ");
        System.out.println(manager.getItemSupplierCost(itemId));
    }

    private void getProductSupplierCost() {
        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();
        System.out.println(manager.getProductSupplierCost(productName));
    }

    private void removeDiscount() {
        boolean isStore = getBooleanInput("Is the discount in-store? (true/false): ");
        String discounts = manager.showDiscounts(isStore);
        if (discounts.length() == 0) {
            System.out.println("There are no discounts");
            return;
        }
        System.out.println(discounts);
        int discountNumber = getIntInput("Enter discount number: ");
        System.out.println(manager.removeDiscount(isStore, discountNumber));
    }

    private void showDiscounts() {
        boolean isStore = getBooleanInput("Show in-store discounts? (true/false): ");
        System.out.println(manager.showDiscounts(isStore));
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
    private void getItemLocation(){
        int itemId = getIntInput("Enter item ID: ");
        System.out.println(manager.getItemLocation(itemId));
    }
    private void getProductLocation(){
        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();
        boolean inWareHouse = getBooleanInput("do you want the warehouse location? (true/false): ");
        System.out.println(manager.getProductLocation(productName,inWareHouse));
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
