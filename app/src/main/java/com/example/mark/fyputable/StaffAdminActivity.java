package com.example.mark.fyputable;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class StaffAdminActivity extends AppCompatActivity {

    TextView addModule;
    TextView timetable;
    TextView addLecture;
    TextView addUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_admin);

        addModule = (TextView) findViewById(R.id.txtAdminModule);
        timetable = (TextView) findViewById(R.id.txtAdminTimetable);
        addLecture = (TextView) findViewById(R.id.txtAdminLecture);
        addUser = (TextView) findViewById(R.id.txtAdminUser);

        addModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateModuleActivity.class));
            }
        });

        timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
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
}
