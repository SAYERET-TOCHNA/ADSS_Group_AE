package org.example.DomainLayerInventory;

import org.example.DataAccessInventory.ControllerClasses.OrderController;
import org.example.DataAccessInventory.DtoClasses.Dto;
import org.example.DataAccessInventory.DtoClasses.OrderDto;

import java.time.LocalDate;

public class Order {
    private String productName;
    private int amount;
    private int dayOfMonth;

    private int orderId;
    private OrderDto myDto;

    @Override
    public String toString() {
        if (dayOfMonth ==-1){
            return "{" +
                    "productName='" + productName + '\'' +
                    ", amount=" + amount +
                    ", dayOfMonth = unknown" +
                    '}';
        }
        return "{" +
                "productName='" + productName + '\'' +
                ", amount=" + amount +
                ", dayOfMonth=" + dayOfMonth +
                '}';
    }

    public Order(String productName, int amount, int dayOfMonth, int orderId, OrderController orderController, boolean fromDB) {
        this.productName = productName;
        this.amount = amount;
        this.dayOfMonth = dayOfMonth;
        this.orderId = orderId;
        this.myDto = new OrderDto(productName,amount,dayOfMonth,orderController,fromDB,orderId);
        if (!fromDB)
            this.myDto.persist();
    }



    public String getProductName() {
        return productName;
    }

    public int getAmount() {
        return amount;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }


    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setAmount(int amount) {
        this.amount = amount;
        myDto.updateAmount(amount);
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }
    public boolean isToday(){
        LocalDate today = LocalDate.now();
        return today.getDayOfMonth() == dayOfMonth;
    }
    public boolean isTomorrow(){
        LocalDate today = LocalDate.now();
        if(dayOfMonth == 1){
            return today.getDayOfMonth() == today.lengthOfMonth();
        }
        return today.getDayOfMonth() == dayOfMonth -1;
    }

    public void deleteYourself(){
        myDto.deleteOrder();
    }
}
