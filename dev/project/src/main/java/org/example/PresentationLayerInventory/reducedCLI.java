package org.example.PresentationLayerInventory;


import org.example.DomainLayerInventory.CallBack;
import org.example.DomainLayerInventory.InventoryFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class reducedCLI implements InventoryCLI {
    private final Scanner scanner = new Scanner(System.in);
    private final InventoryFacade manager;

    private CallBack callBack = new CallBack() {
        @Override
        public void call(String msg) {
            System.out.println(msg);
        }
    };

    boolean isFinished = false;

    public reducedCLI(InventoryFacade inventoryFacade) {
        manager = inventoryFacade;
        System.out.println("Hello Worker Welcome to Inventory");

    }

    public void runWareHouse() {
        while (!isFinished) {
            this.showMainMenu();
            int mainChoice = this.getChoice();
            this.handleMainChoice(mainChoice);
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
                "6. Exit"
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
            case 6 -> exit();
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }
    private void handleProducts() {
        String[] productMenuOptions = {

                "1. Get Product Cost",
                "2. Get Product Location",
                "3. Back to Main Menu"
        };
        while (true) {
            showSubMenu(productMenuOptions);
            int choice = getChoice();
            switch (choice) {
                case 1 -> getProductCost();
                case 2 -> getProductLocation();
                case 3 -> { return; }
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
                "1. Show Discounts",
                "2. Back to Main Menu"
        };
        while (true) {
            showSubMenu(discountMenuOptions);
            int choice = getChoice();
            switch (choice) {
                case 1 -> showDiscounts();
                case 2 -> { return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleOrders() {
        String[] orderMenuOptions = {
                "1. Show Orders",
                "2. Back to Main Menu"
        };
        while (true) {
            showSubMenu(orderMenuOptions);
            int choice = getChoice();
            switch (choice) {
                case 1 -> showOrders();
                case 2 -> { return; }
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

    private void notifyDamagedItem() {
        int itemId = getIntInput("Enter ItemId: ");
        System.out.println(manager.notifyDamagedItem(itemId));
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

    private void exit() {
        System.out.println("Exiting the inventory system");
        this.isFinished = true;
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
