package DomainLayer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class Discount {
    public Discount(LocalDate startDate, LocalDate endDate,Category[] categories, double discountParameter,
                    List<String> discountedProducts) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.categories = categories;
        this.discountParameter = discountParameter;
        this.discountedProducts = discountedProducts;
    }

    private LocalDate startDate;
    private LocalDate endDate;
    private Category[] categories;
    private double discountParameter;//between zero and 1
    private List<String> discountedProducts;

    public boolean isDiscounted(Product product){

        if (!isCurrentDateBetween())
            return false;
        Category category = product.getCategory();
        for (Category c : categories) {
            if (c.equals(category))
                return true;
        }
        String productName = product.getName();
        for (String s : discountedProducts) {
            if (s.equals(productName))
                return true;
        }
        return false;

    }
    public double discountedPrice(Product product, double oldPrice){
        if (!isDiscounted(product)){
            return oldPrice;
        }
        return oldPrice*(1-discountParameter);
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
