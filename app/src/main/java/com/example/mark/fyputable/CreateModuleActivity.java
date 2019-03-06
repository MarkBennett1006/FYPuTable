package com.example.mark.fyputable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    int StaffIncrementer;
    int StudentIncrementer;
    List<String> lstSelectedStaff;
    List<String> lstSelectedStudents;
    ListView StaffView;
    ListView StudentView;
    ArrayAdapter<String> ViewStaffAdapter;
    ArrayAdapter<String> ViewStudentAdapter;
    Button btnDelete;




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
        StaffView = (ListView)findViewById(R.id.lstCreatModStaff);
        StudentView = (ListView)findViewById(R.id.lstCreatModStudents);


        regStudentList = new ArrayList<>();
        regStaffList = new ArrayList<>();
        createRegStaffList = new ArrayList<>();
        createRegStudentList = new ArrayList<>();
        lstSelectedStaff  = new ArrayList<>();
        lstSelectedStudents = new ArrayList<>();

         StaffIncrementer = 0;
         StudentIncrementer = 0;

        //Ref2
        ArrayAdapter<String> adapterStaff = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, regStaffList);
        ArrayAdapter<String> adapterStudent = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, regStudentList);

        ViewStaffAdapter = new ArrayAdapter<>(CreateModuleActivity.this, R.layout.messageinfo, R.id.textView, lstSelectedStaff);
        ViewStudentAdapter = new ArrayAdapter<>(CreateModuleActivity.this, R.layout.messageinfo, R.id.textView, lstSelectedStudents);
        StaffView.setAdapter(ViewStaffAdapter);
        StudentView.setAdapter(ViewStudentAdapter);







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

                if (StaffIncrementer != 0){

                    String strStaffCode = spRegStaff.getItemAtPosition(position).toString();
                    lstSelectedStaff.add(strStaffCode);
                    getUserID(strStaffCode);
                    ViewStaffAdapter.notifyDataSetChanged();

                }

                else {
                    StaffIncrementer += 1;
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spRegStudents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (StudentIncrementer != 0) {
                    String strStudentCode = spRegStudents.getItemAtPosition(position).toString();
                    lstSelectedStudents.add(strStudentCode);
                    getUserID(strStudentCode);
                    ViewStudentAdapter.notifyDataSetChanged();
                }

                else {
                    StudentIncrementer += 1;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        StaffView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                btnDelete = (Button) view.findViewById(R.id.delUser);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                String removedUser = lstSelectedStaff.get(position);
                lstSelectedStaff.remove(position);
                ViewStaffAdapter.notifyDataSetChanged();
                Toast.makeText(CreateModuleActivity.this, removedUser + " removed.", Toast.LENGTH_SHORT).show();


            }
        });

        StudentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String removedUser = lstSelectedStudents.get(position);
                lstSelectedStudents.remove(position);
                ViewStudentAdapter.notifyDataSetChanged();
                Toast.makeText(CreateModuleActivity.this, removedUser + " removed.", Toast.LENGTH_SHORT).show();
            }
        });
     /*   StaffView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                btnDelete = (Button) view.findViewById(R.id.delUser);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lstSelectedStaff.remove(position);
                        ViewStaffAdapter.notifyDataSetChanged();
                        Toast.makeText(CreateModuleActivity.this, "Worked", Toast.LENGTH_SHORT).show();
                    }
                });

                Toast.makeText(CreateModuleActivity.this, "Click", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }); */









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
