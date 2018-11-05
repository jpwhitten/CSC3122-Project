package com.mantarakk.pantree;

/**
 * Created by Joe on 10/07/2016.
 */
public enum Units {

    GRAMS("g"),
    KILOGRAMS("Kg"),
    OUNCES("oz"),
    MILLILITRES("ml"),
    LITRES("L"),
    PINTS("Pints"),
    GALLONS("Gallons");


    private final String symbol;
    Units(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

}
