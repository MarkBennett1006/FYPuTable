package com.example.mark.fyputable.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mark.fyputable.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/*
Reference 1: Writing to Firestore: https://www.youtube.com/watch?v=di5qmolrFVs&list=PLrnPJCHvNZuDrSqu-dKdDi3Q6nM-VUyxD&index=3
Reference 2: Create User: https://firebase.google.com/docs/auth/android/password-auth#create_a_password-based_account
Reference 3: Various Auth methods: https://firebase.google.com/docs/auth/android/manage-users
Reference 4: System Timer: https://stackoverflow.com/questions/38005366/how-do-i-have-java-wait-a-second-before-executing-the-next-line-without-try-cat

 */

public class CreateUserActivity extends AppCompatActivity {

    EditText crUserName;
    EditText crUserType;
    EditText crEmail;
    EditText crInsitutionID;
    EditText crPassword;
    Button btnCreateUser;
    String NewUserName;
    String NewUserType;
    String NewEmail;
    String NewInstID;
    String NewUserID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    //Ref 2
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        crUserName = (EditText) findViewById(R.id.crUserName);
        crUserName.setHint("Name");

        crUserType = (EditText) findViewById(R.id.crUserType);
        crUserType.setHint("User Type");

        crEmail = (EditText) findViewById(R.id.crEmail);
        crEmail.setHint("Email");

        crInsitutionID = (EditText) findViewById(R.id.crInstitutionID);
        crInsitutionID.setHint("ID Number");

        btnCreateUser = (Button) findViewById(R.id.btnCreateUser);

        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });



    }

    public void createUser(){


        String strEmail = crEmail.getEditableText().toString();
        String strUserName = crUserName.getEditableText().toString();
        String strInstituionID = crInsitutionID.getEditableText().toString();
        String strUserType = crUserType.getEditableText().toString();
       // String strPassword = crPassword.getEditableText().toString();
        String strPassword = "password";



        //Ref 2
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentEmail = currentUser.getEmail();


         //Ref 2
        mAuth.createUserWithEmailAndPassword(strEmail,strPassword);
        pause();

        //Ref 3
        mAuth.signOut();
        pause();
        mAuth.signInWithEmailAndPassword(strEmail,strPassword);
        pause();
        FirebaseUser tempUser = FirebaseAuth.getInstance().getCurrentUser();
        pause();
        pause();
        String strUID = tempUser.getUid();
        pause();
        mAuth.signOut();
        pause();
        mAuth.signInWithEmailAndPassword(currentEmail,strPassword);





        //Ref 1
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("email", strEmail);
        newUser.put("institutionID", strInstituionID);
        newUser.put("userID", strUID);
        newUser.put("userName", strUserName);
        newUser.put("userType", strUserType);


        //Ref 1
        db.collection("Users").document().set(newUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CreateUserActivity.this, "Success", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });







    }


    //Ref 4
    static void pause(){
        long Time0 = System.currentTimeMillis();
        long Time1;
        long runTime = 0;
        while(runTime<500){
            Time1 = System.currentTimeMillis();
            runTime = Time1 - Time0;
        }
    }
}
