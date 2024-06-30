package DomainLayer;

import DataAccess.ControllerClasses.DiscountController;
import DataAccess.DtoClasses.DiscountDto;
import DataAccess.DtoClasses.Dto;
import org.junit.platform.commons.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class Discount {
    private boolean isToClients;
    private LocalDate startDate;
    private LocalDate endDate;
    private Category[] categories;
    private double discountParameter;//between zero and 1
    private List<String> discountedProducts;
    private int discountId;
    private DiscountDto myDto;


    public Discount(LocalDate startDate, LocalDate endDate, Category[] categories, double discountParameter,
                    List<String> discountedProducts, boolean isToClients, int discountId, DiscountController discountController, boolean fromDB) {
        this.isToClients = isToClients;
        this.startDate = startDate;
        this.endDate = endDate;
        this.categories = categories;
        this.discountParameter = discountParameter;
        this.discountedProducts = discountedProducts;
        this.discountId = discountId;
        this.myDto = new DiscountDto( isToClients,
         startDate,
         endDate,
         Utils.categoriesToString(categories),
         discountParameter,
            Utils.ListToStringJoined(discountedProducts),
         discountController,
         fromDB,
         discountId );
        if (!fromDB)
            this.myDto.persist();
    }

    public boolean isToClients() {
        return isToClients;
    }

    public void setToClients(boolean toClients) {
        isToClients = toClients;
    }


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

    public void deleteYourself(){
        myDto.deleteDiscount();
    }
}
