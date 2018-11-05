package com.mantarakk.pantree;

import org.joda.time.LocalDate;

/**
 * Created by Joseph Whitten.
 */
public class PantryItem {

    private FoodItem foodItem;
    private LocalDate useBy;

    public PantryItem(FoodItem foodItem, LocalDate useBy) {
        this.foodItem = foodItem;
        this.useBy = useBy;
    }

    public LocalDate getUseBy() {
        return useBy;
    }

    public void setUseBy(LocalDate useBy) {
        this.useBy = useBy;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }
}
