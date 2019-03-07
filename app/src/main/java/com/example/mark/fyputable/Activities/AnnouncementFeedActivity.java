package com.example.mark.fyputable.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.example.mark.fyputable.Services.AnnouncementAdapter;
import com.example.mark.fyputable.Services.AnnouncementDialogue;
import com.example.mark.fyputable.MyApplication;
import com.example.mark.fyputable.Objects.Announcement;
import com.example.mark.fyputable.Objects.Users;
import com.example.mark.fyputable.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


/*
Ref 1 Compound Query: https://www.youtube.com/watch?v=VBEzqahgKmw&list=PLrnPJCHvNZuDrSqu-dKdDi3Q6nM-VUyxD&index=10
Ref 2 Recycler View + FS : https://www.youtube.com/watch?v=lAGI6jGS4vs&list=PLrnPJCHvNZuAXdWxOzsN5rgG2M4uJ8bH1&index=3
Ref 3 Navigation Bar: https://www.youtube.com/watch?v=tPV8xA7m-iw
Ref 4 Passing arguments to Dialog: https://stackoverflow.com/questions/15459209/passing-argument-to-dialogfragment
Ref 5 Custom Dialogue: https://www.youtube.com/watch?v=ARezg1D9Zd0&list=PLrnPJCHvNZuBkhcesO6DfdCghl6ZejVPc&index=5

 */


public class AnnouncementFeedActivity extends AppCompatActivity {

    FirebaseFirestore db;
    CollectionReference feedRef;
    FirebaseUser user;
    String uid;
    String type;
    private static final String TAG = AnnouncementFeedActivity.class.getSimpleName();

    //Ref 3
    NotificationManagerCompat notificationManagerCompat;
    BottomNavigationView navbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_feed);

        notificationManagerCompat = NotificationManagerCompat.from(this);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        navbar = findViewById(R.id.NavAnnounceFeed);
        navbar.setOnNavigationItemSelectedListener(navListener);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PublishAnnounce.class));
            }
        });

        db = FirebaseFirestore.getInstance();
        feedRef= db.collection("Announcement");
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();


        db.collection("User")
                .whereEqualTo("userID", uid)
        .get()
        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot snap: queryDocumentSnapshots){
                    Users currentUser = snap.toObject(Users.class);

                  type=currentUser.getUserType();

                  if (type.equals("L")){
                    }

                } }});
        setUpRecycler();
    }


    //Ref 2 (Whole Method)
    public void setUpRecycler(){

        Query query = feedRef.whereEqualTo("userID", uid).orderBy("Date", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Announcement> options = new FirestoreRecyclerOptions.Builder<Announcement>()
                .setQuery(query, Announcement.class)
                .build();

        AnnouncementAdapter adapter = new AnnouncementAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.announcement_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

        adapter.setOnItemClickListener(new AnnouncementAdapter.onItemClickListener() {
            @Override
            public void onAnnounceClick(DocumentSnapshot docSnap, int position) {
                String path = docSnap.getReference().getPath();

                //Ref 4 + 5
                AnnouncementDialogue dialogue = new AnnouncementDialogue();
                Bundle args = new Bundle();
                args.putString("announcepath", path);
                dialogue.setArguments(args);
                dialogue.show(getSupportFragmentManager(), "dialogue");
            }});
        }


    //Ref 3 (Whole Method)
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    MyApplication myapp = ((MyApplication) getApplicationContext());
                    int itemid = item.getItemId();

                    switch (item.getItemId()) {
                        case R.id.NavAnnounceMenu:

                            startActivity(new Intent(getApplicationContext(), AnnouncementFeedActivity.class));

                            break;
                        case R.id.NavCalendar:

                            startActivity(new Intent(getApplicationContext(), CalendarActivity.class));

                            break;

                        case R.id.NavSettings:

                          //  Global Variables: https://stackoverflow.com/questions/1944656/android-global-variable
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
