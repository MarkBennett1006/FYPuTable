package com.example.mark.fyputable;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class EditModuleActivity extends AppCompatActivity {

    Spinner spStaff;
    Spinner spStudents;
    ListView lstStudents;
    ListView lstStaff;
    Button btnAddUsers;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> regStudentList;
    List<String> regStaffList;
    String strModuleCodeLookup;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_module);


        Intent incomingIntent = getIntent();
        strModuleCodeLookup = incomingIntent.getStringExtra("modulePath");


        spStaff = (Spinner) findViewById(R.id.spAddStaff);
        spStudents = (Spinner) findViewById(R.id.spAddStudents);
        btnAddUsers = (Button) findViewById(R.id.btnAddUsers);
        lstStudents = (ListView) findViewById(R.id.lstAddModStudents);
        lstStaff = (ListView) findViewById(R.id.lstAddModStaff);
        regStudentList = new ArrayList<>();
        regStudentList = new ArrayList<>();
        ArrayAdapter<String> adapterStaff = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, regStaffList);
        ArrayAdapter<String> adapterStudent = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, regStudentList);

    }


}
