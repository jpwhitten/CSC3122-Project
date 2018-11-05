package com.mantarakk.pantree;

import org.joda.time.LocalDate;

import java.util.Date;

/**
 * A class modelling a food item.
 * Created by Joseph Whitten.
 */
public class FoodItem {

    private String scanID;
    private String name;
    private boolean isFrozen;

    private Measurement amount;
    private Measurement portion;
    private double calories;
    private double carbs;
    private double protien;
    private double fat;

    public FoodItem(String name) {
        this.name = name;
    }

    public String getScanID() {
        return scanID;
    }

    public void setScanID(String scanID) {
        this.scanID = scanID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void setFrozen(boolean frozen) {
        isFrozen = frozen;
    }

    public Measurement getAmount() {
        return amount;
    }

    public void setAmount(Measurement amount) {
        this.amount = amount;
    }

    public Measurement getPortion() {
        return portion;
    }

    public void setPortion(Measurement portion) {
        this.portion = portion;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public double getProtien() {
        return protien;
    }

    public void setProtien(double protien) {
        this.protien = protien;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }
}
