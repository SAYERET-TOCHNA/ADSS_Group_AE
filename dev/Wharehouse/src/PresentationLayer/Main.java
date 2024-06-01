package PresentationLayer;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import DomainLayer.Buisness.BuisnessManager;

public class Main {

    public static void main(String[] args) {
        /*
        Scanner scanner = new Scanner(System.in);
        System.out.println("WELCOME TO THE NEW SUPERLY WEBSITE :)\n\nLogin\n");
        System.out.println("Username:");
        String userName = scanner.nextLine();
        System.out.println("Password:");
        String password = scanner.nextLine();
        //if(!login(userName,password))
        if (false) {

        }


        BuisnessManager buisnessManager = new BuisnessManager();
        boolean run = true;
        System.out.println("Welcome " + userName);
        while (run) {
            System.out.println("Available actions:\n" +
                    "1.addProduct \n 2.addItem \n getReportByCategory, getReportAlmostMissing," +
                    " getReportBadProducts, changeProductBuyingCost, changeProductSellingCost, changeProductCategories, disconnect\n");
            System.out.println("Command: ");
            String input = scanner.nextLine();
            if (input == "addProduct")
            {
                System.out.print("Enter product name: ");
                String name = scanner.nextLine();

                double buyingCost = 0.0;
                while (true) {
                    System.out.print("Enter buying cost: ");
                    try {
                        buyingCost = scanner.nextDouble();
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid number for buying cost.");
                        scanner.next();
                    }
                }

                double sellingCost = 0.0;
                while (true) {
                    System.out.print("Enter selling cost: ");
                    try {
                        sellingCost = scanner.nextDouble();
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid number for selling cost.");
                        scanner.next();
                    }
                }
                scanner.nextLine();

                System.out.print("Enter categories (comma-separated): ");
                String categoriesInput = scanner.nextLine();
                String[] categories = categoriesInput.split(",");

                System.out.print("Enter manufacturer: ");
                String manufacturer = scanner.nextLine();

                int minimumAmountAllowed = 0;
                while (true) {
                    System.out.print("Enter minimum amount allowed: ");
                    try {
                        minimumAmountAllowed = scanner.nextInt();
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid integer for minimum amount allowed.");
                        scanner.next();
                    }
                }
                scanner.nextLine();

                System.out.print("Enter warehouse location: ");
                String warehouseLocation = scanner.nextLine();

                System.out.print("Enter store location: ");
                String storeLocation = scanner.nextLine();

                String result = buisnessManager.addProduct(name, buyingCost, sellingCost, categories, manufacturer,
                        minimumAmountAllowed, warehouseLocation, storeLocation);


            }
            else if (input == "addItem")
            {
                System.out.print("Enter product name for item: ");
                String productName = scanner.nextLine();

                int itemID = 0;
                while (true) {
                    System.out.print("Enter item ID: ");
                    try {
                        itemID = scanner.nextInt();
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid integer for item ID.");
                        scanner.next();
                    }
                }
                scanner.nextLine();

                System.out.print("Enter expired date (yyyy-mm-dd): ");
                String expiredDate = scanner.nextLine();

                boolean isInWarehouse = false;
                while (true) {
                    System.out.print("Is the item in warehouse? (true/false): ");
                    try {
                        isInWarehouse = scanner.nextBoolean();
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter 'true' or 'false' for warehouse status.");
                        scanner.next();
                    }
                }

                String result = buisnessManager.addItem(productName, itemID, expiredDate, isInWarehouse);

            }
            else if (input == "getReportByCategory")
            {
                List<String> reportCategories = new LinkedList<String>();
                System.out.println("Enter categories for the report (type 'done' to finish): ");
                while (true) {
                    System.out.print("Enter category: ");
                    String category = scanner.nextLine();
                    if (category.equalsIgnoreCase("done")) {
                        break;
                    }
                    reportCategories.add(category);
                }

                String result = buisnessManager.getReportByCategory(reportCategories);

            }
            else if (input == "getReportAlmostMissing")
            {
                String result = buisnessManager.getReportAlmostMissing();
            }
            else if (input == "getReportBadProducts")
            {
                String result = buisnessManager.getReportBadProducts();
            }
            else if (input == "changeProductBuyingCost")
            {

            }

            else if (input == "changeProductSellingCost")
            {

            }
            else if (input == "changeProductCategories")
            {

            }
            else if (input == "disconnect")
            {
                run = false;
            }
            else
            {
                System.out.println("You entered an unknown command");
            }
        }

        System.out.println("Thank you for using our website\n\nPlease give us 100 :) <3");
        //clean?

         */
    }




}
