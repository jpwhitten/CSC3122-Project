package com.mantarakk.pantree;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Date;

/**
 * A class modelling a pantry storing all the users foods and food history.
 */
public class Pantry {

    private static ArrayList<PantryItem> foodItems = new ArrayList<>();
    private static ArrayList<FoodItem> knownFoods = new ArrayList<>();
    private static ArrayList<PantryItem> selectedFoods = new ArrayList<>();



    public static ArrayList<PantryItem> getSelectedFoods() {
        return selectedFoods;
    }

    public static void setSelectedFoods(ArrayList<PantryItem> selectedFoods) {
        Pantry.selectedFoods = selectedFoods;
    }


    public Pantry(){

    }

    public static ArrayList<PantryItem> getFoodItems() {
        return Pantry.foodItems;
    }

    public void setFoodItems(ArrayList<PantryItem> foodItems) {
        Pantry.foodItems = foodItems;
    }

    public static ArrayList<FoodItem> getKnownFoods() {
        return knownFoods;
    }

    public static void setKnownFoods(ArrayList<FoodItem> knownFoods) {
        Pantry.knownFoods = knownFoods;
    }

    public static void addFoodItem(FoodItem foodItem, LocalDate useBy) {
        Pantry.getFoodItems().add(new PantryItem(foodItem, useBy));
    }

    public static void addFoodItem(PantryItem pantryItem) {
        Pantry.getFoodItems().add(pantryItem);
    }
}
