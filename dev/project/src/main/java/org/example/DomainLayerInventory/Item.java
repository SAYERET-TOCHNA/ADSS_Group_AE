package org.example.DomainLayerInventory;

import org.example.DataAccessInventory.ControllerClasses.ItemController;
import org.example.DataAccessInventory.DtoClasses.Dto;
import org.example.DataAccessInventory.DtoClasses.ItemDto;

import java.time.LocalDate;

public class Item {
    private String productName;
    private int itemID;
    private LocalDate expiredDate;
    private boolean isInWareHouse;

    private boolean isDamaged;

    private ItemDto myDto;

    public Item(String productName, int itemID, LocalDate expiredDate,
                boolean onShelves, boolean isDamaged, ItemController itemController, boolean fromDB) {
        this.productName = productName;
        this.itemID = itemID;
        this.expiredDate = expiredDate;
        this.isDamaged = isDamaged;
        this.isInWareHouse = onShelves;
        this.myDto = new ItemDto(productName, itemID, expiredDate, isInWareHouse, isDamaged,
         itemController, fromDB );
        if (!fromDB)
            this.myDto.persist();
    }

    public String getProductName() {
        return this.productName;
    }

    public boolean isExpired(){
        LocalDate currentDate = LocalDate.now();
        return currentDate.isAfter(expiredDate)&&!isDamaged;
    }

    public boolean isInWareHouse() {
        return isInWareHouse;
    }

    public void setInWareHouse(boolean inWareHouse) {
        this.isInWareHouse = inWareHouse;
        myDto.updateIsInWareHouse(inWareHouse);
    }

    public boolean isDamaged() {
        return isDamaged;
    }

    public void setDamaged(boolean damaged) {
        isDamaged = damaged;
        myDto.updateIsDamaged(isDamaged);
    }

    public void deleteYourself(){
        myDto.deleteItem();
    }

    public LocalDate getExpiredDate(){
        return this.expiredDate;
    }
}