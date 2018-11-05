package com.mantarakk.pantree;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Custom List adapter to present a users current food items.
 * Created by Joseph Whitten.
 */
public class FoodListAdapter extends BaseAdapter {

    //Different zone for different days to expiry.
    private final int RED_ZONE = 1;
    private final int ORANGE_ZONE = 2;
    private final int EXPIRED_ZONE = 0;

    private final String EXPIRED = "Expired";
    private final String FROZEN = "Frozen";

    private ArrayList<PantryItem> foodItems;

    private boolean isGroupSelecting;
    private boolean isFreezeSelecting;

    private ArrayList<PantryItem> selectedItems;
    Context context;

    private static LayoutInflater inflater = null;

    public FoodListAdapter(Context activity,  ArrayList<PantryItem> food) {
        foodItems = sortFoodItems(food);
        context = activity;
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return foodItems.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView name;
        TextView daysToExpire;
        TextView days;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent)
    {

        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.food_list, null);
        PantryItem pantryItem = foodItems.get(position);
        holder.name = (TextView) rowView.findViewById(R.id.food_list_text_field_name);
        holder.days = (TextView) rowView.findViewById(R.id.food_list_text_field_days_to_expire_days);
        holder.daysToExpire = (TextView) rowView.findViewById(R.id.food_list_text_field_days_to_expire);
        holder.name.setText(foodItems.get(position).getFoodItem().getName());
        if (!(new LocalDate().isAfter(pantryItem.getUseBy())) && !pantryItem.getFoodItem().isFrozen())
        {
            holder.daysToExpire.setText(Integer.toString(Days.daysBetween(new LocalDate(), pantryItem.getUseBy()).getDays()));
        }
        else
        {
            holder.days.setVisibility(View.INVISIBLE);
            holder.daysToExpire.setText(EXPIRED);
        }
        if (pantryItem.getFoodItem().isFrozen())
        {
            holder.days.setVisibility(View.INVISIBLE);
            holder.daysToExpire.setText(FROZEN);
        }




        rowView.setBackgroundColor(this.getItemColour(foodItems.get(position)));

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (isGroupSelecting) {
                    v.setBackgroundColor(context.getColor(R.color.colorPrimary));
                    Pantry.getSelectedFoods().add(foodItems.get(position));
                    System.out.println("Group Select");
                    System.out.println(Pantry.getSelectedFoods().get(0).toString());
                }
                if (isFreezeSelecting) {
                    v.setBackgroundColor(context.getColor(R.color.colorFrozen));
                    Pantry.getSelectedFoods().add(foodItems.get(position));
                    System.out.println("Freeze Select");
                }
            }
        });

        return rowView;
    }

    /**
     * Sort food items from closest to expiring to least.
     * @param foodItems list of food items
     * @return sorted list of food items
     */
    private ArrayList<PantryItem> sortFoodItems(ArrayList<PantryItem> foodItems)
    {

        Collections.sort(foodItems, new Comparator<PantryItem>()
        {
            @Override
            public int compare(PantryItem food1, PantryItem food2)
            {
                return  Days.daysBetween(new LocalDate(), food1.getUseBy()).compareTo( Days.daysBetween(new LocalDate(), food2.getUseBy()));
            }
        });
        return foodItems;
    }

    /**
     * Sort the list
     */
    public void sortByExpire() {

        this.sortFoodItems(foodItems);

    }

    /**
     * Return a list items background color based on its expiry date.
     * @param pantryItem An item from the list
     * @return background color for this item.
     */
    public int getItemColour(PantryItem pantryItem)
    {

        int dayCount = Days.daysBetween(new LocalDate(), pantryItem.getUseBy()).getDays();

        if(dayCount <= this.ORANGE_ZONE && dayCount > this.RED_ZONE && !pantryItem.getFoodItem().isFrozen())
        {
            return context.getColor(R.color.colorOrange);
        }
        else if (dayCount <= this.RED_ZONE && dayCount > this.EXPIRED_ZONE && !pantryItem.getFoodItem().isFrozen())
        {
            return context.getColor(R.color.colorRed);
        }
        else if (dayCount <= this.EXPIRED_ZONE && !pantryItem.getFoodItem().isFrozen())
        {
            return context.getColor(R.color.colorExpired);
        }
        else if (pantryItem.getFoodItem().isFrozen()) {
            return context.getColor(R.color.colorFrozen);
        }
        else
        {
            return context.getColor(R.color.colorInvisible);
        }

    };

    /**
     * Is the user currently selecting items to freeze.
     * @param toggle
     */
    public void setIsFreezeSelecting(boolean toggle) {
        this.isFreezeSelecting = toggle;
        if(toggle)
        {
            Pantry.setSelectedFoods(new ArrayList<PantryItem>());
        }
    }

    /**
     * Is the user currently selecting items to remove.
     * @param toggle
     */
    public void setIsGroupSelecting(boolean toggle) {
        this.isGroupSelecting = toggle;
        if(toggle)
        {
            Pantry.setSelectedFoods(new ArrayList<PantryItem>());
        }
    }

    public ArrayList<PantryItem> getSelectedItems() {
        return selectedItems;
    }
}