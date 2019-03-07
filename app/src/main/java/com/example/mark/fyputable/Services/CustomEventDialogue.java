package com.example.mark.fyputable.Services;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.mark.fyputable.Objects.Building;
import com.example.mark.fyputable.Objects.Users;
import com.example.mark.fyputable.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


// ref 1: Custom Dialogue: https://www.youtube.com/watch?v=ARezg1D9Zd0&list=PLrnPJCHvNZuBkhcesO6DfdCghl6ZejVPc&index=5



// ************ ALL CODE COMES FROM REF 1 ****************

public class CustomEventDialogue extends AppCompatDialogFragment {

    EditText txtDate;
    EditText txtStart;
    EditText txtEnd;
    EditText txtEmail;
    EditText txtRoom;
    Spinner spBuildings;
    Button btnAdd;
    ListView lstViewEmails;
    List<String> lstBuildings;
    List<String> emails;
    List<String> UIDList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Boolean failure = false;
    int a;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_event_dialogue, null);

        builder.setView(view).setTitle("Create Meeting")
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })
        .setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createEntry();
            }
        });

        txtDate = view.findViewById(R.id.txtCustomDate);
        txtStart = view.findViewById(R.id.txtCustomStart);
        txtEnd = view.findViewById(R.id.txtCustomEnd);
        txtEmail = view.findViewById(R.id.txtCustomEmail);
        txtRoom = view.findViewById(R.id.txtCustomRoom);
        spBuildings = view.findViewById(R.id.spCustomBuildings);
        btnAdd = view.findViewById(R.id.btnCustomAddEmail);
        lstViewEmails = view.findViewById(R.id.lstCustomEmails);

        lstBuildings = new ArrayList<>();
        emails = new ArrayList<>();
        UIDList = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, lstBuildings);
        ArrayAdapter<String> adapterEmail = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, emails);

        lstViewEmails.setAdapter(adapterEmail);

        db.collection("Building").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot snap: queryDocumentSnapshots){
                    Building building = snap.toObject(Building.class);

                    String currentBuilding = building.getBuildingName();
                    lstBuildings.add(currentBuilding);
                }
                spBuildings.setAdapter(adapter);
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentemail = txtEmail.getEditableText().toString();
                emails.add(currentemail);
                adapterEmail.notifyDataSetChanged();
                txtEmail.setText("");

                db.collection("Users")
                        .whereEqualTo("email", currentemail )
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot snap : queryDocumentSnapshots){
                            Users user = snap.toObject(Users.class);
                            UIDList.add(user.getUserID());
                        }

                    }
                });

            }
        });

        return builder.create();
    }


    public void createEntry() {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        UIDList.add(uid);
        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(UIDList);
        UIDList.clear();
        UIDList.addAll(hashSet);

        for (int i = 0; i < UIDList.size(); i++) {

            String ModuleCode = "Custom Meeting";
            String Type = "C";
            String Title = "Custom Meeting";
            FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();

            String Date = txtDate.getEditableText().toString();
            String End = txtEnd.getEditableText().toString();
            String Start = txtStart.getEditableText().toString();
            String Room = txtRoom.getEditableText().toString();
            String Building = spBuildings.getSelectedItem().toString();
            int randomNumber = (int) (Math.random() * 100);
            String entryID = Integer.toString(randomNumber);

            HashMap<String, Object> map = new HashMap();
            map.put("Building", Building);
            map.put("Date", Date);
            map.put("Room", Room);
            map.put("classType", Type);
            map.put("entryID", entryID);
            map.put("endTime", End);
            map.put("moduleName", Title);
            map.put("moduleCode", ModuleCode);
            map.put("startTime", Start);
            map.put("userID", UIDList.get(i));

            db.collection("Timetable_Entries").document().set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }
            });
        }
    }





    }

