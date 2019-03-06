package com.example.mark.fyputable;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
ref 1: Navigation Bar: https://www.youtube.com/watch?v=tPV8xA7m-iw

 */

public class StaffAdminActivity extends AppCompatActivity {

    Button addModule;
    Button openPersonal;
    Button addLecture;
    Button addUser;
    BottomNavigationView navBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_admin);


       //ref 1
        navBar = findViewById(R.id.NavAdmin);

        navBar.setOnNavigationItemSelectedListener(navListener);

      //  navBar.setSelectedItemId(R.id.NavSettings);


        addModule =  findViewById(R.id.btnOpenAdminModule);
        openPersonal = findViewById(R.id.btnOpenAccount);
        addLecture = findViewById(R.id.btnOpenAdminLecture);
        addUser = findViewById(R.id.btnOpenAdminUser);

        addModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateModuleActivity.class));
            }
        });

        openPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PersonalAdmin.class));
            }
        });

        addLecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateLectureActivity.class));
            }
        });

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateUserActivity.class));
            }
        });
    }



    //ref 1
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    MyApplication myapp = ((MyApplication) getApplicationContext());


                    switch (item.getItemId()) {
                        case R.id.NavAnnounceMenu:

                            startActivity(new Intent(getApplicationContext(), AnnouncementFeedActivity.class));

                            break;
                        case R.id.NavCalendar:

                            startActivity(new Intent(getApplicationContext(), CalendarActivity.class));

                            break;

                        case R.id.NavSettings:


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
                    }
                    return true;
                }
            };
}
