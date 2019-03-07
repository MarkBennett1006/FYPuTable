package com.example.mark.fyputable.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.mark.fyputable.MyApplication;
import com.example.mark.fyputable.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.timessquare.CalendarPickerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {


    /*
   Ref 1 CalendarPickerView https://codinginflow.com/tutorials/android/timesquare-calendarpickerview
   Ref 2 Java Parse Exception https://examples.javacodegeeks.com/core-java/text/parseexception/java-text-parseexception-how-to-solve-parseexception/
   Ref 3 Timesquare Calendar Picker View https://www.youtube.com/watch?v=AN6UNJ-UC54&t=438s
   Ref 4 Navigation Bar: https://www.youtube.com/watch?v=tPV8xA7m-iw


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
    NotificationManagerCompat notificationManagerCompat;
    String uid;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        notificationManagerCompat = NotificationManagerCompat.from(this);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


       //Ref 4
        bottomNavigationView = findViewById(R.id.NavActivityCalendar);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);


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

            }});
    }



    //Ref 4
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    MyApplication myapp = ((MyApplication) getApplicationContext());

                    switch (item.getItemId()) {
                        case R.id.NavCalendar:

                            startActivity(new Intent(getApplicationContext(), CalendarActivity.class));

                            break;

                        case R.id.NavSettings:

                           //Global Variables: https://stackoverflow.com/questions/1944656/android-global-variable
                            switch (myapp.getGlobalUserType()) {
                                case "A":
                                    startActivity(new Intent(getApplicationContext(), StaffAdminActivity.class));
                                    break;

                                case "L":
                                    startActivity(new Intent(getApplicationContext(), PersonalAdmin.class));
                                    break;

                                case "S":
                                    startActivity(new Intent(getApplicationContext(), PersonalAdmin.class));
                                    break;
                            }
                            break;

                        case R.id.NavAnnounceMenu:

                            startActivity(new Intent(getApplicationContext(), AnnouncementFeedActivity.class));

                            break;
                    }
                    return true;
                }
            };


}
