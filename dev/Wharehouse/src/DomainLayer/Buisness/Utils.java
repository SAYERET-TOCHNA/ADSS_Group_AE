package DomainLayer.Buisness;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
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

    public static double getBestDiscountedPrice(double originPrice, Category category, List<Discount> discounts){
        double ret = originPrice;
        for(Discount discount: discounts)
        {
            if(discount.isDiscounted(category))
            {
                if(ret > discount.discountedPrice(category,originPrice))
                {
                    ret = discount.discountedPrice(category,originPrice);
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
}
