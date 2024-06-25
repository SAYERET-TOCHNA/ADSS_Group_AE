package DataAccess.DtoClasses;

import DataAccess.ControllerClasses.Controller;

public class OrderDto extends Dto {
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getAmount() {
        return amount;
    }



    public int getDayOfMonth() {
        return dayOfMonth;
    }



    public int getOrderId() {
        return orderId;
    }



    private String productName;
    private int amount;
    private int dayOfMonth;

    private int orderId;

    public OrderDto(String productName, int amount, int dayOfMonth, Controller controller,
                    boolean fromDB,int orderId) {
        super(controller, fromDB);
        this.productName = productName;
        this.amount = amount;
        this.dayOfMonth = dayOfMonth;
        this.orderId = orderId;

    }

    @Override
    public void persist() {
        insert(new Object[]{orderId,productName, amount, dayOfMonth});
        isPersisted = true;
    }

    public void updateAmount(int amount) {
        this.amount = amount;
        if (isPersisted) {
            update(new Object[]{orderId}, "amount", amount);
        }
    }
    public void deleteOrder() {
        if (isPersisted) {
            delete(new Object[]{orderId});
            isPersisted = false;
        }
    }
}
