package org.example.DataAccessInventory.DtoClasses;

import org.example.DataAccessInventory.ControllerClasses.Controller;
import org.example.DomainLayerInventory.Utils;

import java.time.LocalDate;
import java.util.List;

public class DiscountDto extends Dto {
    private boolean isToClients;
    private String  startDate;
    private String endDate;
    private String categories;
    private double discountParameter;

    public boolean isToClients() {
        return isToClients;
    }


    public LocalDate getStartDate() {
        return Utils.parseDate(startDate);
    }



    public LocalDate getEndDate() {
        return Utils.parseDate(endDate);
    }



    public String getCategories() {
        return categories;
    }



    public double getDiscountParameter() {
        return discountParameter;
    }


    public String getDiscountedProducts() {
        return discountedProducts;
    }
    public int getDiscountId() {
        return discountId;
    }

    private String discountedProducts;
    private int discountId;

    public DiscountDto(boolean isToClients, LocalDate startDate, LocalDate endDate, String categories, double discountParameter,
                       String discountedProducts, Controller controller, boolean fromDB,int discountId) {
        super(controller, fromDB);
        this.isToClients = isToClients;
        this.startDate = Utils.DateToString(startDate);
        this.endDate = Utils.DateToString(endDate);
        this.categories = categories;
        this.discountParameter = discountParameter;
        this.discountedProducts = discountedProducts;
        this.discountId = discountId;
    }

    @Override
    public void persist() {
        insert(new Object[]{discountId, startDate, endDate, isToClients, discountParameter, categories,discountedProducts});
        isPersisted = true;
    }

    public void deleteDiscount() {
        if (isPersisted) {
            delete(new Object[]{discountId});
            isPersisted = false;
        }
    }
}
