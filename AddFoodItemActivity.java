package com.mantarakk.pantree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class AddFoodItemActivity extends AppCompatActivity {

    EditText nameField;
    EditText weightField;
    EditText servingSizeField;
    EditText carbs;
    EditText protein;
    EditText fat;

    Spinner weightPicker;
    Spinner servingSizePicker;

    Button addFoodButton;

    String scanResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_item);

        scanResult = getIntent().getStringExtra("scanresult");

        nameField = (EditText) findViewById(R.id.add_food_item_name);
        weightField = (EditText) findViewById(R.id.add_food_item_weight);
        servingSizeField = (EditText) findViewById(R.id.add_food_item_serving_size);
        carbs = (EditText) findViewById(R.id.add_food_item_cps);
        protein = (EditText) findViewById(R.id.add_food_item_pps);
        fat = (EditText) findViewById(R.id.add_food_item_fps);

        List<Units> units = new ArrayList<>();
        for(Units unit : Units.values()) {
            units.add(unit);
        }

        ArrayAdapter<Units> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        weightPicker = (Spinner) findViewById(R.id.add_food_item_wieght_picker);
        weightPicker.setAdapter(unitAdapter);
        servingSizePicker = (Spinner) findViewById(R.id.add_food_item_serving_size_picker);
        servingSizePicker.setAdapter(unitAdapter);

        addFoodButton = (Button) findViewById(R.id.add_food_item_add_food_button);
        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nameField.getText().toString().equals("")) {
                    backToHome();
                }
            }
        });


    }

    private void backToHome() {
        Intent addFood = new Intent(this, AddPantryItemActivity.class);
        FoodItem newFoodItem = new FoodItem(nameField.getText().toString());
        newFoodItem.setScanID(scanResult);
        if(!weightField.getText().toString().equals("")) {
            newFoodItem.setAmount(new Measurement(Integer.parseInt(weightField.getText().toString()), (Units) weightPicker.getSelectedItem()));
        }
        if(!servingSizeField.getText().toString().equals("")) {
            newFoodItem.setPortion(new Measurement(Integer.parseInt(servingSizeField.getText().toString()), (Units) weightPicker.getSelectedItem()));
        }
        if(!carbs.getText().toString().equals("")) {
            newFoodItem.setCarbs(Double.parseDouble(carbs.getText().toString()));
        }
        if(!protein.getText().toString().equals("")) {
            newFoodItem.setProtien(Double.parseDouble(protein.getText().toString()));
        }
        if(!fat.getText().toString().equals("")) {
            newFoodItem.setFat(Double.parseDouble(fat.getText().toString()));
        }
        Pantry.getKnownFoods().add(newFoodItem);
        addFood.putExtra("newFoodItem", new Gson().toJson(newFoodItem));
        startActivity(addFood);
    }



}
