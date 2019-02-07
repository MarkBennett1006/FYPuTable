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
   Ref 1 CalendarPickerView https://codinginflow.com/tutorials/android/timesquare-calendarpickerview
   Ref 2 Java Parse Exception https://examples.javacodegeeks.com/core-java/text/parseexception/java-text-parseexception-how-to-solve-parseexception/


 --------Copy of Licence for the TimeSquare CalendarPickerView-------
   Copyright 2012 Square, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


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


        Date firstDayOfSemester = new Date();
        Date lastDayOfSemester = new Date();
        Date today = new Date();

        //Ref 2
        try {
        lastDayOfSemester = dmy.parse("05/04/2019");
        firstDayOfSemester = dmy.parse("01/01/2019");
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        };

        //Ref 1
        calPicker = findViewById(R.id.calendar);
        calPicker.init(firstDayOfSemester, lastDayOfSemester)
                .withSelectedDate(today);


        //Ref 1
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

            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });







    }

}
