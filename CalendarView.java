package com.mantarakk.pantree;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
/**
 * Calendar View displays a calendar with highlighted event days to the user.
 * Contains the methods used to handle date clicks, update the calendar and moving between months.
 *
 * @author Joseph Whitten
 * @version 3
 */
public class CalendarView extends LinearLayout {

    // Amount of days in the calendar grid.
    private static final int DAYS_COUNT = 42;

    // default date format
    private static final String DATE_FORMAT = "MMM yyyy";

    //Date format
    private String dateFormat;

    //Current displayed month
    private Calendar currentDate = Calendar.getInstance();

    //Event handling
    public EventHandler eventHandler = null;

    //Internal elements
    private LinearLayout header;
    private ImageView btnPrev;
    private ImageView btnNext;
    private TextView txtDate;
    public GridView grid;
    private Date dateSelected;

    //The view
    public View view;

    //Constructors
    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    /**
     * Load control xml layout and updates the calendar
     */
    private void initControl(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.control_calendar, this);

        loadDateFormat(attrs);
        assignUiElements();
        assignClickHandlers();

        updateCalendar();
    }

    /**
     * Load the date format from the attribute set, if it hasn't been set then load the default
     *
     * @param attrs The Attribute Set
     */
    private void loadDateFormat(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);

        try {
            // try to load provided date format, and fallback to default otherwise
            dateFormat = ta.getString(R.styleable.CalendarView_dateFormat);
            if (dateFormat == null)
                dateFormat = DATE_FORMAT;
        } finally {
            ta.recycle();
        }
    }

    /**
     * Initialize all the UI elements from the View.
     */
    private void assignUiElements() {
        // layout is inflated, assign local variables to components
        header = (LinearLayout) findViewById(R.id.calendar_header);
        btnPrev = (ImageView) findViewById(R.id.calendar_prev_button);
        btnNext = (ImageView) findViewById(R.id.calendar_next_button);
        txtDate = (TextView) findViewById(R.id.calendar_date_display);
        grid = (GridView) findViewById(R.id.calendar_grid);
    }

    /**
     * Initialize the click event handlers.
     */
    private void assignClickHandlers() {
        // add one month and refresh UI
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, 1);
                updateCalendar();
            }
        });

        // subtract one month and refresh UI
        btnPrev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, -1);
                updateCalendar();
            }
        });

        // long-pressing a day
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> view, View cell, int position, long id) {
                dateSelected = (Date) view.getItemAtPosition(position);
                eventHandler.onDayClick(dateSelected);
            }
        });


    }

    /**
     * Call this to update the calendar with a set of dates assigned by calling setEvents
     */
    //public void updateCalendar() {
      //  updateCalendar();
    //}

    /**
     * Displays the dates in a grid and changes their background if there is an event on that day.
     */
    private void updateCalendar() {
        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar) currentDate.clone();

        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        // fill cells
        while (cells.size() < DAYS_COUNT) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // update grid
        grid.setAdapter(new CalendarAdapter(getContext(), cells, calendar));

        // update title
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        txtDate.setText(sdf.format(currentDate.getTime()));
    }


    private class CalendarAdapter extends ArrayAdapter<Date> {

        // for view inflation
        private LayoutInflater inflater;

        private Calendar c;

        public CalendarAdapter(Context context, ArrayList<Date> days, Calendar c) {
            super(context, R.layout.control_calendar_day, days);
            this.c = c;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            // day in question
            Date date = getItem(position);
            int day = date.getDate();
            int month = date.getMonth();
            int year = date.getYear();

            // today
            Date today = new Date();

            // inflate item if it does not exist yet
            if (view == null)
                view = inflater.inflate(R.layout.control_calendar_day, parent, false);

            // if this day has an event, specify event image
            view.setBackgroundColor(getResources().getColor(R.color.colorInvisible));

            // clear styling
            ((TextView) view).setTypeface(null, Typeface.NORMAL);
            ((TextView) view).setTextColor(Color.BLACK);

            if (month != c.get(Calendar.MONTH) - 1 ^ month == 11) {
                // if this day is outside current month, grey it out
                ((TextView) view).setTextColor(getResources().getColor(R.color.colorInvisible));
                view.setBackgroundColor(getResources().getColor(R.color.colorInvisible));
            }
            if (day == today.getDate() && month == today.getMonth() && year == today.getYear() && month == c.get(Calendar.MONTH) - 1) {
                //Highlight today
                ((TextView) view).setTypeface(null, Typeface.BOLD);
                ((TextView) view).setTextColor(getResources().getColor(R.color.colorPrimary));
            }

            // set text
            ((TextView) view).setText(String.valueOf(date.getDate()));

            return view;
        }
    }

    /**
     * Assign event handler to be passed needed events
     */
    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    /**
     * This interface defines what events to be reported to
     * the outside world
     */
    public interface EventHandler {
        void onDayClick(Date date);
    }


}
