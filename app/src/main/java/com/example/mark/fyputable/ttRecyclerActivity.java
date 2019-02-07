package com.example.mark.fyputable;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
 import com.example.mark.fyputable.OnSwipeTouchListener;

import java.lang.ref.Reference;
import java.sql.Ref;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/*
Ref 1 Compound Query: https://www.youtube.com/watch?v=VBEzqahgKmw&list=PLrnPJCHvNZuDrSqu-dKdDi3Q6nM-VUyxD&index=10
Ref 2 Recycler View + FS : https://www.youtube.com/watch?v=lAGI6jGS4vs&list=PLrnPJCHvNZuAXdWxOzsN5rgG2M4uJ8bH1&index=3
Ref 3 Date Incrementer : https://stackoverflow.com/questions/20582632/how-to-get-the-next-date-when-click-on-button-in-android
Ref 4 Receiving and Intent: https://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-in-android-application
Reference 5: OnClick for RecyclerView: https://youtu.be/3WR4QAiVuCw?t=467
Ref 5 Swipe Listener: https://stackoverflow.com/questions/4139288/android-how-to-handle-right-to-left-swipe-gestures

 */
public class ttRecyclerActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
   // CollectionReference moduleRegRef = db.collection("ModuleReg");
   // CollectionReference entryRef = db.collection("TT_Entry");

    DocumentReference entryDocRef;
    entryAdapter adapter;
    FirebaseUser user;
    String uid;
    List<ModuleReg> RegistrationList;
    DateFormat dmy = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat activityTitleDate = new SimpleDateFormat("EEE, MMM d");
    Calendar cal = Calendar.getInstance();
    String date;
    String titleDate;
    Button btnNext;
    Button btnPrev;
    OnSwipeTouchListener onSwipeTouchListener;
    CollectionReference CollectRefForIndex;
    private static final String TAG = ttRecyclerActivity.class.getSimpleName();
    String buildID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tt_recycler);

        date = dmy.format(cal.getTime());

        FirebaseFirestoreSettings settings;
        settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);







        user = FirebaseAuth.getInstance().getCurrentUser(); //Find the current user
        uid = user.getUid(); //Current user's id needs to be turned to variable to it can match with specific node in Firebase Database
        RegistrationList = new ArrayList<>();
        entryDocRef = db.collection("Timetable").document(uid);

        //Following two lines are from Ref 4
        Intent incomingIntent = getIntent();
        String incomingDate = incomingIntent.getStringExtra("dateExtra");

     try{
        cal.setTime(sdf.parse(incomingDate));
        String dtTest = sdf.format(cal.getTime());
        date = dmy.format(cal.getTime());
        titleDate = activityTitleDate.format(cal.getTime());
    } catch (java.text.ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

        TextView dateView = (TextView) findViewById(R.id.current_date);
        dateView.setText(titleDate);

        btnNext = findViewById(R.id.btnNextDay);
        btnPrev = findViewById(R.id.btnLastDay);





        setUpRecycler(date);
      //  dataLoad();




        //Ref 5
        onSwipeTouchListener = new OnSwipeTouchListener(ttRecyclerActivity.this) {
            //Ref 3
            public void onSwipeRight() {

                cal.add(Calendar.DAY_OF_MONTH, -1);
                date = dmy.format(cal.getTime());
                titleDate = activityTitleDate.format(cal.getTime());
                dateView.setText(titleDate);
                setUpRecycler(date);

            }
            //Ref 3
            public void onSwipeLeft() {

                cal.add(Calendar.DAY_OF_MONTH, 1);
                date = dmy.format(cal.getTime());
                titleDate = activityTitleDate.format(cal.getTime());
                dateView.setText(titleDate);
                setUpRecycler(date);

            }

        };

        dateView.setOnTouchListener(onSwipeTouchListener);


        //Ref 3
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.DAY_OF_MONTH, 1);
                date = dmy.format(cal.getTime());
                titleDate = activityTitleDate.format(cal.getTime());
                dateView.setText(titleDate);
                setUpRecycler(date);

            }
        });

        //Ref 3
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.DAY_OF_MONTH, -1);
                date = dmy.format(cal.getTime());
                titleDate = activityTitleDate.format(cal.getTime());
                dateView.setText(titleDate);
                setUpRecycler(date);

            }
        });

    }

    //Reference 1
    public void setUpRecycler(String passDate){
        Query query = entryDocRef.collection("TT_Entries")
                .whereEqualTo("Date", passDate)
                .orderBy("startTime", Query.Direction.ASCENDING)
                ;


        CollectRefForIndex = entryDocRef.collection("TT_Entries");

        CollectRefForIndex.whereEqualTo("Date", passDate)
        .orderBy("startTime", Query.Direction.ASCENDING)
        .get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "");
            }
        });







                ;

        FirestoreRecyclerOptions<Entry> options = new FirestoreRecyclerOptions.Builder<Entry>()
                .setQuery(query, Entry.class)
                .build();

        adapter = new entryAdapter(options);

       RecyclerView recyclerView = findViewById(R.id.recycler_view);
     //   recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.startListening();



        adapter.setOnItemClickListener(new entryAdapter.onItemClickListener() {
            @Override
            public void onEntryClick(DocumentSnapshot docSnap, int position) {

                String path = docSnap.getReference().getPath();

                Entry ent =  docSnap.toObject(Entry.class);
                String build = ent.getBuilding();
                doQuery(build, path);




                //   Toast.makeText(ttRecyclerActivity.this, ent.getBuilding(), Toast.LENGTH_SHORT).show();


            }
        });


    }


    public void doQuery(String build1, String path1){
        CollectionReference buildRef = db.collection("Building");

        buildRef.whereEqualTo("buildingName", build1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    Building building = documentSnapshot.toObject(Building.class);
                    buildID = building.getPlaceID();

                    Intent intent = new Intent(ttRecyclerActivity.this, EntryActivity.class);
                    //    intent.putExtra("entryPath", path);

                    Bundle extras = new Bundle();
                    extras.putString("entryPath",path1);
                    extras.putString("entryPlaceID",buildID);
                    intent.putExtras(extras);
                    startActivity(intent);



                }
            }
        });

    }





}
