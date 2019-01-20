package com.example.mark.fyputable;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//Reference 1: https://www.youtube.com/watch?v=V-z3Y8s31gY
public class Main2Activity extends AppCompatActivity {

    ListView lstView;
    FirebaseUser user;
    FirebaseDatabase database;
    ArrayAdapter<String> adapter;
    List<String> itemlist;
    String uid;
    List<ModuleReg> ModuleRegList;
    
    DateFormat dmy = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat day = new SimpleDateFormat("EEEE");
    Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        lstView = (ListView) findViewById(R.id.lstUserData);
        user = FirebaseAuth.getInstance().getCurrentUser(); //Find the current user
        uid = user.getUid(); //Current user's id needs to be turned to variable to it can match with specific node in Firebase Database
        itemlist = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        ModuleRegList = new ArrayList<>();
        Button btnNext = (Button) findViewById(R.id.btnAnnouncement);

        dataLoad();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Main3Activity.class));
            }
        });
    }

    public void dataLoad(){

        Query ModuleRegQuery = database.getReference("ModuleReg")
                .orderByChild("StudentID")
                .equalTo(uid);
        ModuleRegQuery.addListenerForSingleValueEvent(valueEventListener);
    }

    public void doQuery(){
        ModuleRegList.forEach(ModuleReg ->
        {
            String modID;
            modID = ModuleReg.getModuleID();

           Query TTentryQuery = database.getReference("TTentry")
                    .orderByChild("ModuleID")
                    .equalTo(modID);
            TTentryQuery.addListenerForSingleValueEvent(valueEventListener2);


        });
    }



                ValueEventListener valueEventListener = new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      if (dataSnapshot.exists()){
                          for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                              ModuleReg modReg = snapshot.getValue(ModuleReg.class);
                              ModuleRegList.add(modReg);
                              doQuery();

                          }
                      }
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError databaseError) {

                  }
              };


              ValueEventListener valueEventListener2 = new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      if (dataSnapshot.exists()){
                      for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                          TTentry entry = snapshot.getValue(TTentry.class);

                          itemlist.add(entry.getName().toString());
                          itemlist.add(entry.getLocation().toString());
                          itemlist.add(entry.getEntryID().toString());
                          itemlist.add(entry.getModuleID().toString());
                          itemlist.add(" ");


                          adapter = new ArrayAdapter<>(Main2Activity.this, R.layout.messageinfo, R.id.textView, itemlist);
                          lstView.setAdapter(adapter);
                      }
                          //     }
                      }
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError databaseError) {

                  }
              };





}
