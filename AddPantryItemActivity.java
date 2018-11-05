package com.mantarakk.pantree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Date;

/**
 * Activity for adding pantry items.
 * Created by Joseph Whitten
 */
public class AddPantryItemActivity extends AppCompatActivity {

    EditText itemName;
    TextView selectedUseByDate;
    com.mantarakk.pantree.CalendarView calendarView;
    Button addFoodButton;
    FoodItem newFoodItem;

    LocalDate currentSelectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pantry_item);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String newFoodJson = extras.getString("newFoodItem");
            newFoodItem = new Gson().fromJson(newFoodJson, FoodItem.class);
        }

        currentSelectedDate = new LocalDate();

        itemName = (EditText) findViewById(R.id.add_pantry_item_search);
        itemName.setText(newFoodItem.getName());

        selectedUseByDate = (TextView) findViewById(R.id.add_food_use_by_value);
        selectedUseByDate.setText(new LocalDate().toString());

        calendarView = (com.mantarakk.pantree.CalendarView) findViewById(R.id.add_food_date_picker);
        calendarView.setEventHandler(new com.mantarakk.pantree.CalendarView.EventHandler() {
            @Override
            public void onDayClick(Date date) {
                currentSelectedDate = new LocalDate(date);
                selectedUseByDate.setText(currentSelectedDate.toString());
            }
        });

        addFoodButton = (Button) findViewById(R.id.add_pantry_item_add_food);
        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPantryItem();
            }
        });
    }

    /**
     * Add the pantry item, return to home page.
     */
    private void addPantryItem() {
        PantryItem pantryItem = new PantryItem(newFoodItem, currentSelectedDate);
        Pantry.addFoodItem(pantryItem);
        Intent addItem = new Intent(this, HomeActivity.class);
        startActivity(addItem);
    }

}
