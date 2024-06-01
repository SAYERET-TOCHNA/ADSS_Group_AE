package DomainLayer.Buisness;

import java.time.LocalDate;
import java.util.Locale;

public class Item {
    private String productName; //should be enhareted
    private int itemID;
    private LocalDate expiredDate;
    private boolean isSold;
    private boolean isDamaged;

    public Item(String productName, int itemID, LocalDate expiredDate) {
        this.productName = productName;
        this.itemID = itemID;
        this.expiredDate = expiredDate;
        this.isDamaged = false;
        this.isDamaged = false;
    }

    public String getProductName() {
        return this.productName;
    }

    public boolean isExpired(){
        LocalDate currentDate = LocalDate.now();
        return currentDate.isAfter(expiredDate);
    }

    public boolean isDamaged()
    {
        return this.isDamaged;
    }
}