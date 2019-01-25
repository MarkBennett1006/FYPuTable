package com.example.mark.fyputable;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

//Reference 1: https://www.youtube.com/watch?v=V-z3Y8s31gY
//Reference 2: https://stackoverflow.com/questions/20582632/how-to-get-the-next-date-when-click-on-button-in-android

public class Main2Activity extends AppCompatActivity {

    ListView lstView;
    FirebaseUser user;
    FirebaseDatabase database;
    ArrayAdapter<String> adapter;
    List<String> itemlist;
    String uid;
    List<ModuleReg> ModuleRegList;
    List<TTentry> EntryList;
    

    //Date Variables
    DateFormat dmy = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat day = new SimpleDateFormat("EEEE");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    Calendar cal = Calendar.getInstance();
    Calendar cal2 = Calendar.getInstance();
   // String dtToday = day.format(cal.getTime());
    String dtToday;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        database = FirebaseDatabase.getInstance();

        //User Data
        user = FirebaseAuth.getInstance().getCurrentUser(); //Find the current user
        uid = user.getUid(); //Current user's id needs to be turned to variable to it can match with specific node in Firebase Database

        //Lists
        itemlist = new ArrayList<>();
        ModuleRegList = new ArrayList<>();
        EntryList = new ArrayList<>();

        //UI Elements
        Button btnNext = (Button) findViewById(R.id.btnAnnouncement);
        Button btnNxtDay = (Button) findViewById(R.id.btnNxtDay);
        Button btnLstDay = (Button) findViewById(R.id.btnLstDay);
        TextView txtText = (TextView) findViewById(R.id.txtDay) ;
        lstView = (ListView) findViewById(R.id.lstUserData);
        adapter = new ArrayAdapter<>(Main2Activity.this, R.layout.messageinfo, R.id.textView, itemlist);

        try{



        Intent incomingIntent = getIntent();
        String incomingDate = incomingIntent.getStringExtra("dateExtra");
      //  Date date = sdf.parse(incomingDate);

        cal2.setTime(sdf.parse(incomingDate));
        String dtTest = sdf.format(cal2.getTime());
        dtToday = day.format(cal2.getTime());
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



        dataLoad();

        txtText.setText(dtToday);

        //Buttons

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Main3Activity.class));
            }
        });
        
         btnNxtDay.setOnClickListener(new View.OnClickListener() {    //This takes you to the next day on the timetable
             @Override
             public void onClick(View v)  {

             cal.add(Calendar.DATE, +1);
             dtToday = day.format(cal.getTime());



              Log.v("NEXT DATE : ", dtToday);
              txtText.setText(dtToday);
              itemlist.clear();
              adapter.clear();
               ModuleRegList.clear();
               EntryList.clear();
               dataLoad();
             }
         });


             btnLstDay.setOnClickListener(new View.OnClickListener() {       //This takes you to the previous day on the timetable
                 @Override
                 public void onClick(View v)  {

               //  cal.add(Calendar.DATE, -1);
               //  dtToday = day.format(cal.getTime());


                     String dtTest = sdf.format(cal2.getTime());
                     String strInc[] = dtTest.split("/");
                 String str1 = strInc[0];
                 String str2 = strInc[1];
                 String str3 = strInc[2];

                 int inc = Integer.parseInt(str3);
                 int inc2 = inc -1;
                 String strPreviousDate = str1 + "/" + str2 + "/" + Integer.toString(inc2);
                     try{
                 cal2.setTime(sdf.parse(strPreviousDate));
                 dtToday = day.format(cal2.getTime());


                     } catch (java.text.ParseException e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                     }




                  Log.v("PREVIOUS DATE : ", dtToday);
                  txtText.setText(dtToday);
                   adapter.clear();
                   itemlist.clear();
                   ModuleRegList.clear();
                   EntryList.clear();

                    dataLoad();  //Restart the process of retrieving and filtering
                 }
             });
        }




    public void dataLoad(){  //This is where we begin querying the DB

        Query ModuleRegQuery = database.getReference("ModuleReg")         //We filter ModuleReg Collection  to only include entries
                .orderByChild("StudentID")                                   // where the Student ID matches our UserID
                .equalTo(uid);
        ModuleRegQuery.addListenerForSingleValueEvent(valueEventListener);   // Executes first ValueEventListener
    }

    public void doQuery(){                                         //Here we will filter the TTentry collection in the DB
        ModuleRegList.forEach(ModuleReg ->
        {
            String modID;                                          //By the ModuleID of from each Module Registration we filtered previously
            modID = ModuleReg.getModuleID();

           Query TTentryQuery = database.getReference("TTentry")
                    .orderByChild("ModuleID")
                    .equalTo(modID)
                    
                    ;
            TTentryQuery.addListenerForSingleValueEvent(valueEventListener2);      //Second Value Event Listener


        });
    }



                ValueEventListener valueEventListener = new ValueEventListener() {      //This includes the method to add the filtered Data Snapshot
                  @Override                                                             //To an array list we can work with
                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      if (dataSnapshot.exists()){
                          for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                              ModuleReg modReg = snapshot.getValue(ModuleReg.class);
                              ModuleRegList.add(modReg);
                              doQuery();                                               // Executes the second query

                          }
                      }
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError databaseError) {

                  }
              };


              ValueEventListener valueEventListener2 = new ValueEventListener() {         //Here we will actually show the timetable
                  @Override
                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  if (dataSnapshot.exists()){
                  for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                      TTentry entry = snapshot.getValue(TTentry.class);

                    String dtCurrentEntryDay = entry.getDay().toString();


                    if (dtCurrentEntryDay.equals(dtToday)) {             //We only want the entry of the day we're viewing, which is the current system date
                                                                         //by default
                                itemlist.add(entry.getName().toString());
                                itemlist.add(entry.getLocation().toString());
                                itemlist.add(entry.getEntryID().toString());
                                itemlist.add(entry.getModuleID().toString());
                                itemlist.add(entry.getTime());
                                itemlist.add(" ");
                                lstView.setAdapter(adapter);
                                } ;
                        };

                      }
                          //     }
                      }
                  

                  @Override
                  public void onCancelled(@NonNull DatabaseError databaseError) {

                  }
              };








}


