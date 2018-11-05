package com.mantarakk.pantree;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 0;

    ListView listView;
    RelativeLayout addFoodScanButton;
    RelativeLayout removeFoodButton;
    RelativeLayout freezeFoodButton;

    FoodListAdapter foodListAdapter;

    boolean finalRemove = false;
    boolean finalFreeze = false;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context = this;

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            if(getIntent().getBooleanExtra("hasJustAddedPantryItem", false))
            {
                final GsonBuilder builder = new GsonBuilder()
                        .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                        .registerTypeAdapter(LocalTime.class, new LocalTimeSerializer());
                final Gson gson = builder.create();
                String newPantryItemJson = extras.getString("newPantryItem");
                PantryItem newPantryItem = gson.fromJson(newPantryItemJson, PantryItem.class);
                Pantry.getKnownFoods().add(newPantryItem.getFoodItem());
                Pantry.addFoodItem(newPantryItem);
            }
        }

        FoodItem fditm = new FoodItem("fuck off");
        fditm.setScanID("u dick");

        listView = (ListView) findViewById(R.id.home_list_view);
        foodListAdapter = new FoodListAdapter(this, Pantry.getFoodItems());
        listView.setAdapter(foodListAdapter);
        foodListAdapter.sortByExpire();


        addFoodScanButton = (RelativeLayout) findViewById(R.id.home_add_button);
        addFoodScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpCameraPermissions();
            }
        });

        removeFoodButton = (RelativeLayout) findViewById(R.id.home_remove_button);
        removeFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!finalRemove) {
                    foodListAdapter.setIsGroupSelecting(true);
                    foodListAdapter.setIsFreezeSelecting(false);
                } else {
                    foodListAdapter.setIsGroupSelecting(false);
                    ArrayList<PantryItem> items = Pantry.getSelectedFoods();
                    for(PantryItem item : items) {
                        System.out.println(item.toString());
                        Pantry.getFoodItems().remove(item);
                    }
                    listView = (ListView) findViewById(R.id.home_list_view);
                    foodListAdapter = new FoodListAdapter(context, Pantry.getFoodItems());
                    listView.setAdapter(foodListAdapter);
                    foodListAdapter.sortByExpire();
                }
                finalRemove = !finalRemove;
            }
        });


        freezeFoodButton = (RelativeLayout) findViewById(R.id.home_freeze_button);
        freezeFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!finalFreeze) {
                    foodListAdapter.setIsGroupSelecting(false);
                    foodListAdapter.setIsFreezeSelecting(true);
                    System.out.println("not final freeze");
                } else {
                    foodListAdapter.setIsFreezeSelecting(false);
                    ArrayList<PantryItem> items = Pantry.getSelectedFoods();
                    System.out.println(items.size());
                    for(int i = 0; i < items.size(); i++) {
                        System.out.println(items.get(i).toString());
                        for(PantryItem currentitem : Pantry.getFoodItems()) {
                            if (items.get(i).getFoodItem().getScanID().equals(currentitem.getFoodItem().getScanID()) && items.get(i).getUseBy().equals(currentitem.getUseBy())) {
                                Pantry.getFoodItems().get(i).getFoodItem().setFrozen(true);
                                System.out.println("Frozen Food Item");
                            }
                        }
                    }
                    listView = (ListView) findViewById(R.id.home_list_view);
                    foodListAdapter = new FoodListAdapter(context, Pantry.getFoodItems());
                    listView.setAdapter(foodListAdapter);
                    foodListAdapter.sortByExpire();
                }
                finalFreeze = !finalFreeze;
            }
        });
    }

    @TargetApi(23)
    private void setUpCameraPermissions()
    {
        String permissionString = "android.permission.CAMERA";
        if (ContextCompat.checkSelfPermission(this, permissionString) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{permissionString}, REQUEST_CAMERA);
        }
        else
        {
            initiateScan();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case REQUEST_CAMERA:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initiateScan();
                }
            }
        }
    }

    private void initiateScan()
    {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setCameraId(0);
        intentIntegrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            boolean foodNotFound = true;
            for(FoodItem food : Pantry.getKnownFoods()) {
                if (food.getScanID().equals(scanResult.getContents())) {
                    foodNotFound = false;
                    Intent addPantryItem = new Intent(this, AddPantryItemActivity.class);
                    addPantryItem.putExtra("newFoodItem", new Gson().toJson(food));
                    startActivity(addPantryItem);
                    }
                }
            if(foodNotFound) {
                Intent addNewfood = new Intent(this, AddFoodItemActivity.class);
                addNewfood.putExtra("scanresult", scanResult.getContents());
                startActivity(addNewfood);
            }
         }
    }
}

