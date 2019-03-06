package com.example.mark.fyputable;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


//Ref 1 Create Notifications: https://www.youtube.com/watch?v=tTbd1Mfi-Sk


public class PublishAnnounce extends AppCompatActivity {

    Spinner spModules;
    EditText txtTitle;
    EditText txtContent;
    Button btnPublish;
    FirebaseFirestore db;
    CollectionReference announceRef;
    CollectionReference usersRef;
    CollectionReference modulesRef;
    String uid;
    FirebaseUser currentUser;
    List<String> modules;
    String name;
    String userType;
    List<String> userList;
    String modCode;
    NotificationManagerCompat notificationManagerCompat;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_announce);

        spModules = (Spinner)findViewById(R.id.spAnnouncementModules);
        txtTitle = (EditText)findViewById(R.id.crMsgTitle);
        txtContent = (EditText)findViewById(R.id.crMsgContent);
        btnPublish = (Button)findViewById(R.id.btnPublish);


        //ref 1
        notificationManagerCompat = NotificationManagerCompat.from(this);

        db = FirebaseFirestore.getInstance();

        usersRef = db.collection("Users");
        modulesRef = db.collection("Modules");
        announceRef = db.collection("Announcement");
        modules = new ArrayList<>();
        userList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, modules);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = currentUser.getUid();


        modulesRef.whereArrayContains("RegisteredStaff",uid).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot snap: queryDocumentSnapshots){
                            Modules mod = snap.toObject(Modules.class);
                            String currentModule = mod.getModuleCode();
                            modules.add(currentModule);

                        }
                        spModules.setAdapter(adapter);

                    }
                });


        usersRef.whereEqualTo("userID", uid).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot snap: queryDocumentSnapshots){
                            Users user = snap.toObject(Users.class);
                            name = user.getUserName();
                           userType = user.getUserType();


                        }

                    }
                });



        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modCode = spModules.getSelectedItem().toString();
                getUserList(modCode);
            }
        });





    }

    public void getUserList(String code){

        modulesRef.whereEqualTo("ModuleCode", code).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot snap: queryDocumentSnapshots){
                            Modules mod = snap.toObject(Modules.class);
                            for (int i = 0; i < mod.getRegisteredStudents().size(); i++){

                                String student4userList = mod.getRegisteredStudents().get(i);

                                userList.add(student4userList);

                            }

                            for (int i = 0; i < mod.getRegisteredStaff().size(); i++){

                                String staff4userList = mod.getRegisteredStaff().get(i);

                                userList.add(staff4userList);

                            }

                        }

                        publish();

                    }
                });



    }

    public void publish(){


        String title = txtTitle.getEditableText().toString();
        String content = txtContent.getEditableText().toString();

        HashSet<String> hashSetUsers = new HashSet<String>();
        hashSetUsers.addAll(userList);
        userList.clear();
        userList.addAll(hashSetUsers);


        for (int i = 0; i < userList.size(); i++){

            String userForPublishing = userList.get(i);
            String date = "06/03/2019";
            int randomNumber = (int)(Math.random()*100);
            String announceID = Integer.toString(randomNumber);

            Map<String, Object> announcement = new HashMap<>();
            announcement.put("Date",date);
            announcement.put("Title", title);
            announcement.put("announcementID", announceID);
            announcement.put("authID", uid);
            announcement.put("authName",name);
            announcement.put("moduleCode", modCode);
            announcement.put("msgContent", content);
            announcement.put("userID", userForPublishing);



            announceRef.document().set(announcement).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            });


            //ref 1

            createNotificationChannel();

            String ChanelID = "1";


            Notification builder = new NotificationCompat.Builder(this, "channel1")
                    .setContentTitle(modCode + " - " + title)
                    .setContentText(content)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .build();

            notificationManagerCompat.notify(1, builder);



        }





    }



    //ref 1
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        String CHANNEL_ID = "channel1";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name1);
            String description = getString(R.string.channel_description1);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
