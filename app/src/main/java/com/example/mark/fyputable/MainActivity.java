package com.example.mark.fyputable;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;




// Reference 1: https://www.youtube.com/watch?v=tJVBXCNtUuk



//Bill, if you need to log on please use

//email: email1@email.com
//password: password




public class MainActivity extends AppCompatActivity {
    EditText txtEmail,txtPass; //Textboxes for entering email and password
    Button Login;  //Login button
    FirebaseAuth firebaseAuth; //Need to declare Firebase Auth


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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






    }


    public void OpenActivity() {

        String emailText = txtEmail.getText().toString(); //Convert what user has entered in email and password textboxes to Strings
        String passText= txtPass.getText().toString();


        if (TextUtils.isEmpty(emailText)  || TextUtils.isEmpty(passText)){
            Toast.makeText(getApplicationContext(),"Please Enter Email & Password",Toast.LENGTH_LONG).show();
        } else {

          //refer to reference 1 for explanation of this code.
        firebaseAuth.signInWithEmailAndPassword(emailText, passText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {



                        if(task.isSuccessful()){

                          //  startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                            startActivity(new Intent(getApplicationContext(), ttRecyclerActivity.class));

                        }


                        else {
                            Toast.makeText(getApplicationContext(),"Incorrect Login Details!",Toast.LENGTH_LONG).show();
                        }

                    }
                }); }






    }




}
