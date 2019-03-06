package com.example.mark.fyputable;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;




//Ref 1: Global Variables: https://stackoverflow.com/questions/1944656/android-global-variable
// Ref 2 Create Notifications: https://www.youtube.com/watch?v=tTbd1Mfi-Sk

import java.util.List;

import javax.annotation.Nullable;

public class MyApplication extends Application {

    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";
    FirebaseFirestore db;
    NotificationManagerCompat notificationManagerCompat;

    private int entryNotifCount = 1;
    private int announceNotifCount =1;

    private String globalUID;

    private String globalUserType;
    private String globalUserName;
    private String globalTemp;
    private String globalTempEmail;

    @Override
    public void onCreate() {
        super.onCreate();

        //Ref 2
        notificationManagerCompat = NotificationManagerCompat.from(this);
        db = FirebaseFirestore.getInstance();

        createNotificationChannels();

    }


    public void notifSetup(){

            db.collection("Timetable_Entries").whereEqualTo("userID", globalUID).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                        List<DocumentChange> changeList = queryDocumentSnapshots.getDocumentChanges();
                        int listSize = changeList.size();

                        if(entryNotifCount>listSize) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Entry ent2 =  dc.getDocument().toObject(Entry.class);
                                    String date1 = ent2.getDate();
                                    String time1 = ent2.getStartTime();
                                    String code1 = ent2.getModuleCode();

                                    //Ref 2
                                    Notification builder = new NotificationCompat.Builder(MyApplication.this, CHANNEL_1_ID)
                                            .setContentTitle("New Timetable Entry Added")
                                            .setContentText(code1 + " on " + date1 +" at " + time1)
                                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                                            .build();
                                    notificationManagerCompat.notify(1, builder);
                                    break;
                                case MODIFIED:
                                  Entry ent1 =  dc.getDocument().toObject(Entry.class);
                                  String date = ent1.getDate();
                                  String time = ent1.getStartTime();
                                  String code = ent1.getModuleCode();
                                    Notification builder2 = new NotificationCompat.Builder(MyApplication.this, CHANNEL_1_ID)
                                            .setContentTitle("Change to Timetable")
                                            .setContentText("Changes have been made to " + code + " " + date)
                                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                                            .build();
                                    notificationManagerCompat.notify(1, builder2);
                                    break;
                                case REMOVED:
                                    break;

                            }
                       }

                        entryNotifCount+=1;

                    }

                }
            });


        db.collection("Announcements").whereEqualTo("userID", globalUID).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    List<DocumentChange> changeList = queryDocumentSnapshots.getDocumentChanges();
                    int listSize = changeList.size();

                    if(announceNotifCount>listSize) {
                        switch (dc.getType()) {
                            case ADDED:
                                //Ref 2
                                Announcement announcement1 =  dc.getDocument().toObject(Announcement.class);
                                String module = announcement1.getModuleCode();
                                String Auth =announcement1.getAuthName();

                                Notification builder = new NotificationCompat.Builder(MyApplication.this, CHANNEL_1_ID)
                                        .setContentTitle("New Announcement!")
                                        .setContentText("By " + Auth + " for " + module)
                                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                                        .build();
                                notificationManagerCompat.notify(1, builder);
                                break;
                            case MODIFIED:

                                Notification builder2 = new NotificationCompat.Builder(MyApplication.this, CHANNEL_1_ID)
                                        .setContentTitle("")
                                        .setContentText("")
                                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                                        .build();
                                notificationManagerCompat.notify(1, builder2);
                                break;
                            case REMOVED:
                                break;

                        }
                    }

                    announceNotifCount+=1;

                }

            }
        });





    }

    //Ref 2
    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Channel 1");

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel2.setDescription("This is Channel 2");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }





    public String getGlobalUID() {
        return globalUID;
    }

    public void setGlobalUID(String globalUID) {
        this.globalUID = globalUID;
    }

    public String getGlobalUserType() {
        return globalUserType;
    }

    public void setGlobalUserType(String globalUserType) {
        this.globalUserType = globalUserType;
    }

    public String getGlobalUserName() {
        return globalUserName;
    }

    public void setGlobalUserName(String globalUserName) {
        this.globalUserName = globalUserName;
    }

    public int getEntryNotifCount() {
        return entryNotifCount;
    }

    public String getGlobalTemp() {
        return globalTemp;
    }

    public void setGlobalTemp(String globalTemp) {
        this.globalTemp = globalTemp;
    }

    public String getGlobalTempEmail() {
        return globalTempEmail;
    }

    public void setGlobalTempEmail(String globalTempEmail) {
        this.globalTempEmail = globalTempEmail;
    }
}
