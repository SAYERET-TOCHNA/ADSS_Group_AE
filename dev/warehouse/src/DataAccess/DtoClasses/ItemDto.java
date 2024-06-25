package DataAccess.DtoClasses;

import DataAccess.ControllerClasses.Controller;
import DomainLayer.Utils;

import java.time.LocalDate;

public class ItemDto extends Dto {
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getItemID() {
        return itemID;
    }

    public LocalDate getExpiredDate() {
        return Utils.parseDate(expiredDate);
    }

    public boolean isInWareHouse() {
        return isInWareHouse;
    }

    public boolean isDamaged() {
        return isDamaged;
    }


    private String productName;
    private int itemID;
    private String expiredDate;
    private boolean isInWareHouse;
    private boolean isDamaged;

    public ItemDto(String productName, int itemID, LocalDate expiredDate, boolean isInWareHouse, boolean isDamaged, Controller controller, boolean fromDB) {
        super(controller, fromDB);
        this.productName = productName;
        this.itemID = itemID;
        this.expiredDate = Utils.DateToString(expiredDate);
        this.isInWareHouse = isInWareHouse;
        this.isDamaged = isDamaged;
    }
    @Override
    public void persist() {
        insert(new Object[]{itemID,productName,  expiredDate, isInWareHouse, isDamaged});
        isPersisted = true;
    }
    public void updateIsInWareHouse(boolean isInWareHouse) {
        this.isInWareHouse = isInWareHouse;
        if (isPersisted) {
            update(new Object[]{itemID}, "isInWareHouse", isInWareHouse);
        }
    }

    public void updateIsDamaged(boolean isDamaged) {
        this.isDamaged = isDamaged;
        if (isPersisted) {
            update(new Object[]{itemID}, "isDamaged", isDamaged);
        }
    }

    public void deleteItem() {
        if (isPersisted) {
            delete(new Object[]{itemID});
            isPersisted = false;
        }
    }
}
