package com.example.mark.fyputable;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import com.squareup.timessquare.CalendarPickerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {


    /*
   ref Calendar https://codinginflow.com/tutorials/android/timesquare-calendarpickerview

     */

    private static final String TAG = "CalendarActivity";
    CalendarPickerView calPicker;
    DateFormat dmy = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); */

        Date firstDayOfSemester = new Date();
        Date lastDayOfSemester = new Date();
        Date today = new Date();

        try {
        lastDayOfSemester = dmy.parse("05/04/2019");
        firstDayOfSemester = dmy.parse("01/01/2019");
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        };

        calPicker = findViewById(R.id.calendar);
        calPicker.init(firstDayOfSemester, lastDayOfSemester)
              //  .inMode(CalendarPickerView.SelectionMode.RANGE)
                .withSelectedDate(today);



        calPicker.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                Calendar calSelected = Calendar.getInstance();
                calSelected.setTime(date);

                String selectedDate = calSelected.get(Calendar.YEAR)
                        + "/" + (calSelected.get(Calendar.MONTH) + 1) +
                   "/" + calSelected.get(Calendar.DAY_OF_MONTH)
                        ;

               Intent intent = new Intent(CalendarActivity.this, ttRecyclerActivity.class);
                intent.putExtra("dateExtra", selectedDate);


                startActivity(intent);




         //       Toast.makeText(CalendarActivity.this, selectedDate, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });


     /*  CalendarView mCalendarView = (CalendarView) findViewById(R.id.calendarView);

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String date = year + "/" + month +1 + "/" + dayOfMonth;

                Intent intent = new Intent(CalendarActivity.this, ttRecyclerActivity.class);
                intent.putExtra("dateExtra", selectedDate);

                startActivity(intent);
            }
        }); */





    }

}
