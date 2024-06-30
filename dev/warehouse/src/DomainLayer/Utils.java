package DomainLayer;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class Utils {
    public  static boolean isValidLocation(String input) {
        if (input == null) {
            return false;
        }
        if (input.equals(""))
            return true;
        Pattern pattern = Pattern.compile("^[A-Za-z]\\d$");
        return pattern.matcher(input).matches();
    }
    public static LocalDate parseDate(String dateString) {
        // Define the date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            // Try to parse the string into a LocalDate
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            // If parsing fails, return null
            return null;
        }
    }
    public static String isLegalProduct(String dateString) {
        // Define the date format
        return "";
    }

    public static double getBestDiscountedPrice(double originPrice, Product product, List<Discount> discounts){
        double ret = originPrice;
        for(Discount discount: discounts)
        {
            if(discount.isDiscounted(product))
            {
                if(ret > discount.discountedPrice(product,originPrice))
                {
                    ret = discount.discountedPrice(product,originPrice);
                }
            }
        }
        return ret;
    }

    public static String showDiscounts(List<Discount> discounts){
        String output = "";
        for (int i = 0; i < discounts.size(); i++) {
            output += i+1+". "+ discounts.get(i) +"\n";
        }
        return output;
    }
    public static boolean isTodaySunday() {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SUNDAY;
    }

    public static String categoriesToString(Category[] categories){
        if (categories.length ==0)
            return "";
        String output = categories[0].toString();
        for (Category c : Arrays.copyOfRange(categories,1,categories.length)){
            output += "#" + c.toString();
        }
        return  output;
    }
    public static String ListToStringJoined(List<String> names){
        if (names.size() == 0)
            return "";

        // Make a copy of the list to avoid modifying the original list
        List<String> namesCopy = new ArrayList<>(names);

        String output = namesCopy.remove(0);
        for (String s : namesCopy){
            output += "#" + s;
        }
        return output;
    }

    public static List<String[]> ListOfStringArraysFromString(String input){
        if (input.length() ==0)
            return new ArrayList<>();
        List<String[]> output = new ArrayList<>();
        String [] categories = input.split("#");
        for (String s : categories){
            output.add(s.split("\\|"));
        }
        return  output;
    }
    public static String  DateToString(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }
}
