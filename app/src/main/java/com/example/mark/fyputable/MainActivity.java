package com.example.mark.fyputable;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationProvider;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.maps.*;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.Place;
//import com.google.android.libraries.places.*;


import com.google.android.gms.location.places.PlacesOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import io.opencensus.tags.Tag;


// Reference 1: https://www.youtube.com/watch?v=tJVBXCNtUuk
//Reference 2: https://google-developer-training.gitbooks.io/android-developer-advanced-course-practicals/content/unit-4-add-geo-features-to-your-apps/lesson-8-places/8-1-p-places-api/8-1-p-places-api.html
// Reference 3: Global Variables: https://stackoverflow.com/questions/1944656/android-global-variable



//Bill, if you need to log on please use

//email: email1@email.com
//password: password




public class MainActivity extends AppCompatActivity {
    EditText txtEmail,txtPass; //Textboxes for entering email and password
    Button Login;  //Login button
    FirebaseAuth firebaseAuth; //Need to declare Firebase Auth
    String TAG;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference ref;
    String temp;
    String tempEmail;




    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ref = db.collection("Users");

        txtEmail = (EditText) findViewById(R.id.txtName); // Assign UI elements to their XML specification in activity_main.xml
        txtPass = (EditText) findViewById(R.id.txtPass);
        Login = (Button) findViewById(R.id.btnLogin);

        firebaseAuth = FirebaseAuth.getInstance(); //Retrieves and instance of current Firebase Authentication assigned to this project
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenActivity();
            }
        }); //Call the method to be executed when Login button is clicked.

        TAG = MainActivity.class.getSimpleName();


        //Reference 2
        //I have no idea why this needs to be here but EntryActivity stopped working when I deleted it

        PlaceDetectionClient detect = Places.getPlaceDetectionClient(this,null);

        GeoDataClient geo = Places.getGeoDataClient(this, null);

        FusedLocationProviderClient fuse = LocationServices.getFusedLocationProviderClient(this);

      geo.getPlaceById("ChIJBdhPXiSQREgRv6gLTX5whFo").addOnSuccessListener(new OnSuccessListener<PlaceBufferResponse>() {
          @Override
          public void onSuccess(PlaceBufferResponse places) {
              Place placey = places.get(0);

              Log.d(TAG, "Name is " + placey.getName());

          }
      })
              .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Place fetch failed");
            }
        });


    }


    public void OpenActivity() {

        String emailText = txtEmail.getText().toString(); //Convert what user has entered in email and password textboxes to Strings
        String passText= txtPass.getText().toString();
        temp = passText;
        tempEmail = emailText;


        if (TextUtils.isEmpty(emailText)  || TextUtils.isEmpty(passText)){
            Toast.makeText(getApplicationContext(),"Please Enter Email & Password",Toast.LENGTH_LONG).show();
        } else {

          //refer to reference 1 for explanation of this code.
        firebaseAuth.signInWithEmailAndPassword(emailText, passText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                          FirebaseAuth auth = FirebaseAuth.getInstance();
                            FirebaseUser user = auth.getCurrentUser();
                            String uid = user.getUid();
                            ContinueOpen(uid);
                        }

                        else {
                            Toast.makeText(getApplicationContext(),"Incorrect Login Details!",Toast.LENGTH_LONG).show();
                        }
                    }
                }); }
    }


    public void ContinueOpen(String uid1){

        ref.whereEqualTo("userID", uid1).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot snap: queryDocumentSnapshots){

                            Users user = snap.toObject(Users.class);
                            //Ref 3
                            MyApplication myapp = ((MyApplication)getApplicationContext());
                            myapp.setGlobalUID(uid1);
                            myapp.setGlobalUserType(user.getUserType());
                            myapp.setGlobalUserName(user.getUserName());
                            myapp.setGlobalTemp(temp);
                            myapp.setGlobalTempEmail(tempEmail);
                            myapp.notifSetup();
                        }
                       //startActivity(new Intent(getApplicationContext(), AnnouncementFeedActivity.class));
                            startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
                      }
                });


    }




}
