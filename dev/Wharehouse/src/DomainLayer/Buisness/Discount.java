package DomainLayer.Buisness;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;


public class Discount {
    public Discount(LocalDate startDate, LocalDate endDate,Category[] categories, double discountParameter) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.categories = categories;
        this.discountParameter = discountParameter;
    }

    private LocalDate startDate;
    private LocalDate endDate;
    private Category[] categories;
    private double discountParameter;//between zero and 1
    public boolean isDiscounted(Category category){
        if (!isCurrentDateBetween())
            return false;
        for (Category c : categories) {
            if (c.equals(category))
                return true;
        }
        return false;

    }
    public double discountedPrice(Category category, double oldPrice){
        if (!isDiscounted(category)){
            return oldPrice;
        }
        return oldPrice*discountParameter;
    }
    public boolean isCurrentDateBetween() {
        LocalDate currentDate = LocalDate.now();
        return (currentDate.isEqual(startDate) || currentDate.isAfter(startDate)) &&
                (currentDate.isEqual(endDate) || currentDate.isBefore(endDate));
    }

    @Override
    public String toString() {
        StringBuilder categoriesString = new StringBuilder();
        for (int i = 0; i < categories.length; i++) {
            categoriesString.append(categories[i].toString());
            if (i < categories.length - 1) {
                categoriesString.append("|");
            }
        }
        return "From:" +startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+
                ", To:"+endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+", "+
                categoriesString + ", parameter:" + discountParameter;
    }
}
