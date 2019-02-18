package com.example.mark.fyputable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
/*

Reference 1: Writing to Firestore: https://www.youtube.com/watch?v=di5qmolrFVs&list=PLrnPJCHvNZuDrSqu-dKdDi3Q6nM-VUyxD&index=3
Reference 2: Spinner: https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
Reference 3: Spinner On Selected Item: https://stackoverflow.com/questions/1337424/android-spinner-get-the-selected-item-change-event
Reference4: Remove Duplicates from an array: https://stackoverflow.com/questions/1879700/how-to-remove-duplicate-value-from-arraylist-in-android

 */
public class CreateModuleActivity extends AppCompatActivity {

    EditText etModuleName;
    EditText etModuleCode;
    Spinner spRegStaff;
    Spinner spRegStudents;
    Button btnCreateModule;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> regStudentList;
    List<String> regStaffList;
    List<String> createRegStudentList;
    List<String> createRegStaffList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_module);

        etModuleCode = (EditText) findViewById(R.id.crNewModuleCode);
        etModuleCode.setHint("Module Code");
        etModuleName = (EditText) findViewById(R.id.crNewModuleName);
        etModuleName.setHint("Module Name");
        spRegStaff = (Spinner) findViewById(R.id.spRegStaff);
        spRegStudents = (Spinner) findViewById(R.id.spRegStudents);
        btnCreateModule = (Button) findViewById(R.id.btnCreateModule);

        regStudentList = new ArrayList<>();
        regStaffList = new ArrayList<>();
        createRegStaffList = new ArrayList<>();
        createRegStudentList = new ArrayList<>();

        ArrayAdapter<String> adapterStaff = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, regStaffList);
        ArrayAdapter<String> adapterStudent = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, regStudentList);

        db.collection("Users").whereEqualTo("userType", "L")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshotStaff : queryDocumentSnapshots) {

                            Users staff = documentSnapshotStaff.toObject(Users.class);

                            String staffID = staff.getInstitutionID();
                            regStaffList.add(staffID);
                        }

                        spRegStaff.setAdapter(adapterStaff);

                    }
                });

        db.collection("Users").whereEqualTo("userType", "S")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshotStaff : queryDocumentSnapshots) {

                            Users student = documentSnapshotStaff.toObject(Users.class);

                            String studentID = student.getInstitutionID();
                            regStudentList.add(studentID);
                        }

                        spRegStudents.setAdapter(adapterStudent);

                    }
                });




        btnCreateModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateModule();
            }
        });

        spRegStaff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                String strStaffCode = spRegStaff.getItemAtPosition(position).toString();
              //  createRegStaffList.add(strStaffCode);
                getUserID(strStaffCode);



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spRegStudents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String strStudentCode = spRegStudents.getItemAtPosition(position).toString();
               // createRegStudentList.add(strStudentCode);
                getUserID(strStudentCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






    }

    public void getUserID(String code){

        db.collection("Users").whereEqualTo("institutionID", code).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot documentSnapshotUser : queryDocumentSnapshots) {
                            Users user = documentSnapshotUser.toObject(Users.class);

                            if (user.getUserType().equals("S")){
                                createRegStudentList.add(user.getUserID());
                            }
                            else if (user.getUserType().equals("L")){
                                createRegStaffList.add(user.getUserID());
                            }


                        }

                    }
                });
    }


    public void CreateModule(){

       //Reference 4
        HashSet<String> hashSetStaff = new HashSet<String>();
        hashSetStaff.addAll(createRegStaffList);
        createRegStaffList.clear();
        createRegStaffList.addAll(hashSetStaff);


        //Reference 4
        HashSet<String> hashSetStudent = new HashSet<String>();
        hashSetStudent.addAll(createRegStudentList);
        createRegStudentList.clear();
        createRegStudentList.addAll(hashSetStudent);



        String modName = etModuleName.getEditableText().toString();
        String modCode = etModuleCode.getEditableText().toString();



        //Reference 1
        Map<String, Object> module = new HashMap<>();
        module.put("RegisteredStudents", createRegStudentList);
        module.put("RegisteredStaff", createRegStaffList);
        module.put("ModuleCode", modCode);
        module.put("ModuleName", modName);

        db.collection("Modules").document().set(module);

        createRegStaffList.clear();
        createRegStaffList.clear();
    }
}
