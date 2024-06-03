package DomainLayer;

import java.time.LocalDate;
import java.util.Locale;

public class Item {
    private String productName; //should be enhareted
    private int itemID;
    private LocalDate expiredDate;

    public Item(String productName, int itemID, LocalDate expiredDate) {
        this.productName = productName;
        this.itemID = itemID;
        this.expiredDate = expiredDate;
    }

    public String getProductName() {
        return this.productName;
    }

    public boolean isExpired(){
        LocalDate currentDate = LocalDate.now();
        return currentDate.isAfter(expiredDate);
    }


}