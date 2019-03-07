package com.example.mark.fyputable.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mark.fyputable.MyApplication;
import com.example.mark.fyputable.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/*
Ref 1 : Password Email: https://www.youtube.com/watch?v=0-DRdI_xpvQ
 */

public class PersonalAdmin extends AppCompatActivity {


    Button btnNewPass;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_admin);


        btnNewPass = findViewById(R.id.btnNewPass);
        user = FirebaseAuth.getInstance().getCurrentUser();

        btnNewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyApplication myapp = ((MyApplication)getApplicationContext());
                String temp = myapp.getGlobalTemp();
                String email = myapp.getGlobalTempEmail();
                FirebaseAuth auth = FirebaseAuth.getInstance();



                   //Ref 1

                    AuthCredential credential = EmailAuthProvider.getCredential(email,temp);

                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Toast.makeText(PersonalAdmin.this, "Password Change Email sent, check your inbox.", Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }
                            else{
                                Toast.makeText(PersonalAdmin.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }


        });
    }
}
