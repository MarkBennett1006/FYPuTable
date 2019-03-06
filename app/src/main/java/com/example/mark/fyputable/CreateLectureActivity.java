package com.example.mark.fyputable;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*

Reference 1: Writing to Firestore: https://www.youtube.com/watch?v=di5qmolrFVs&list=PLrnPJCHvNZuDrSqu-dKdDi3Q6nM-VUyxD&index=3
Reference 2: Spinner: https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
Reference 3: Spinner On Selected Item: https://stackoverflow.com/questions/1337424/android-spinner-get-the-selected-item-change-event





 */


public class CreateLectureActivity extends AppCompatActivity {
    private static final String TAG = "CreateLetureActivity";
    EditText createID;
    EditText createStudentID;
    EditText createStart;
    EditText createEnd;
    EditText createDate;
    EditText createType;
    EditText createModule;
    EditText createModName;
    EditText createBuilding;
    EditText createRoom;
    Button btnCreate;
    FirebaseUser user;
    String uid;
    List<String > ModuleList;
    Spinner moduleCodeDrop;
    String pageLoadName;
    String currentModule;

    FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lecture);

        createID = (EditText)findViewById(R.id.crID);
        createID.setHint("LectureID");
    //    createStudentID = (EditText)findViewById(R.id.crStudentID);
     //   createStudentID.setHint("StudentID");
        createStart = (EditText)findViewById(R.id.crStart);
        createStart.setHint("Start Time");
        createEnd = (EditText)findViewById(R.id.crEnd);
        createEnd.setHint("End Time");
        createDate = (EditText)findViewById(R.id.crDate);
        createDate.setHint("Date");
        createType = (EditText)findViewById(R.id.crType);
        createType.setHint("Type");

        createModName = (EditText)findViewById(R.id.crModuleName);

        createBuilding = (EditText)findViewById(R.id.crBuilding);
        createBuilding.setHint("Building");
        createRoom = (EditText)findViewById(R.id.crRoom);
        createRoom.setHint("Room");
        btnCreate = (Button)findViewById(R.id.btnCreateEntry);
        moduleCodeDrop = (Spinner)findViewById(R.id.spModule);


        ModuleList = new ArrayList<>();

        //Ref1
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ModuleList);




        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create();
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser(); //Find the current user
        uid = user.getUid(); //Current user's id needs to be turned to variable to it can match with specific node in Firebase Database
        db.collection("Modules").whereArrayContains("RegisteredStaff", uid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots){

                    Modules mod = documentSnapshot1.toObject(Modules.class);

                    String code = mod.getModuleCode();
                    pageLoadName = mod.getModuleName();
                    createModName.setText(pageLoadName);

                    ModuleList.add(code);




                }

                moduleCodeDrop.setAdapter(adapter);

            }
        });

      //Ref2
      moduleCodeDrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

              currentModule = (String) moduleCodeDrop.getItemAtPosition(position);

              db.collection("Modules").whereEqualTo("ModuleCode", currentModule).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                  @Override
                  public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                      for (QueryDocumentSnapshot snap : queryDocumentSnapshots){
                          Modules mod = snap.toObject(Modules.class);

                          createModName.setText(mod.getModuleName());
                      }
                  }
              });

          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {

          }
      });
    }



    public void create() {



        db.collection("Modules").whereEqualTo("ModuleCode", currentModule).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot snap : queryDocumentSnapshots){
                    Modules mod1 = snap.toObject(Modules.class);

                    for (int i = 0; i < mod1.getRegisteredStudents().size(); i++) {



                        String strBuilding = createBuilding.getText().toString();
                        String strDate = createDate.getText().toString();
                        String strRoom = createRoom.getText().toString();
                        String strType = createType.getText().toString();
                        String strEnd = createEnd.getText().toString();
                        String strEntID = createID.getText().toString();
                        String strModCode = moduleCodeDrop.getSelectedItem().toString();
                        String strName = createModName.getText().toString();
                        String strStart = createStart.getText().toString();
                        String strUserID = createStudentID.getText().toString();



                        //Ref 1
                        Map<String, Object> entry = new HashMap<>();
                        entry.put("Building",strBuilding);
                        entry.put("Date",strDate);
                        entry.put("Room",strRoom);
                        entry.put("classType",strType);
                        entry.put("endTime",strEnd);
                        entry.put("entryID",strEntID);
                        entry.put("moduleCode",strModCode);
                        entry.put("moduleName",strName);
                        entry.put("startTime",strStart);
                        entry.put("userID",mod1.getRegisteredStudents().get(i));

                        db.collection("Timetable_Entries").document().set(entry)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });




                    }

                    Toast.makeText(CreateLectureActivity.this, "Success", Toast.LENGTH_SHORT).show();



                }


            }
        });




    }




}
