package PresentationLayer;

import DomainLayer.Buisness.BuisnessManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommandLineUI {
    private final Scanner scanner = new Scanner(System.in);
    private final BuisnessManager manager;

    public CommandLineUI(String data) {
        manager = new BuisnessManager(data);
    }

    public CommandLineUI() {
        manager = new BuisnessManager();
    }

    public static void main(String[] args) {
        CommandLineUI ui;
        if (args.length > 0) {
            ui = new CommandLineUI(args[0]);
        } else {
            ui = new CommandLineUI(
                    "Product1, 10.5, 15.0, Category1|Category2|Category3, Manufacturer1, 5, D1, S1\n" +
                            "Product2, 8.0, 12.0, Category4|Category5|Category6, Manufacturer2, 3, D2, S2\n" +
                            "Product3, 7.5, 10.0, Category7|Category8|Category9, Manufacturer3, 2, D3, S3\n" +
                            "Product1, 101, 01/05/2023, true\n" +
                            "Product2, 102, 31/05/2024, false\n" +
                            "Product3, 103, 01/06/2022, true\n" +
                            "01/06/2024, 31/12/2024, Category1|Category2|Category3, 0.2, true\n" +
                            "01/06/2024, 31/07/2024, Category4|Category5|Category6, 0.15, false\n" +
                            "01/06/2024, 31/08/2024, Category7|Category8|Category9, 0.1, true\n" +
                            "Invalid, data, line\n");
        }

        while (true) {
            ui.showMenu();
            int choice = ui.getChoice();
            ui.handleChoice(choice);
        }
    }

    private void showMenu() {
        System.out.println("\n=== Inventory Management System ===");
        System.out.println("1. Load Data");
        System.out.println("2. Add Product");
        System.out.println("3. Add Item");
        System.out.println("4. Add Discount");
        System.out.println("5. Get Report By Category");
        System.out.println("6. Get Report Almost Missing");
        System.out.println("7. Get Report Bad Products");
        System.out.println("8. Change Product Buying Cost");
        System.out.println("9. Change Product Selling Cost");
        System.out.println("10. Change Product Categories");
        System.out.println("11. Get Item Cost");
        System.out.println("12. Get Product Cost");
        System.out.println("13. Buy Item");
        System.out.println("14. Get Item Supplier Cost");
        System.out.println("15. Get Product Supplier Cost");
        System.out.println("16. Remove Discount");
        System.out.println("17. Show Discounts");
        System.out.println("18. Remove Product");
        System.out.println("19. Exit");
        System.out.print("Enter your choice: ");
    }

    private int getChoice() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 19.");
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
            case 13 -> buyItem();
            case 14 -> getItemSupplierCost();
            case 15 -> getProductSupplierCost();
            case 16 -> removeDiscount();
            case 17 -> showDiscounts();
            case 18 -> removeProduct();
            case 19 -> exit();
            default -> System.out.println("Invalid choice. Please try again.");
        }
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
            categories.add(new String[]{input});
        }

        double discountParameter = getDoubleInput("Enter discount parameter: ");
        boolean inStore = getBooleanInput("Is the discount in-store? (true/false): ");

        System.out.println(manager.addDiscount(startDate, endDate, categories, discountParameter, inStore));
    }

    private void getReportByCategory() {
        List<String[]> categories = new ArrayList<>();
        while (true) {
            System.out.print("Enter category (type 'e' to end): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("e")) {
                break;
            }
            categories.add(new String[]{input});
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

    private void buyItem() {
        int itemId = getIntInput("Enter item ID: ");
        System.out.println(manager.buyItem(itemId));
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
