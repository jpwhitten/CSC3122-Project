package com.mantarakk.pantree;

/**
 * Created by Joseph Whitten.
 */
public class Measurement {


    private int amount;
    private Units unit;

    public Measurement(int amount, Units unit) {
        this.amount = amount;
        this.unit = unit;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Units getUnit() {
        return unit;
    }

    public void setUnit(Units unit) {
        this.unit = unit;
    }
}
