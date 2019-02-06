package com.example.mark.fyputable;

import android.content.Intent;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import javax.annotation.Nullable;

public class EntryActivity extends AppCompatActivity implements OnMapReadyCallback {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference currentEntryRef;
    CollectionReference buildingRef;
    TextView txtName;
    TextView txtType;
    TextView txtDate;
    TextView txtTimes;
    TextView txtBuilding;
    TextView txtRoom;
    String TAG;
    MapView venueMap;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    LatLng lattylingy;
    String strBuilding;
    String strBuildID;
    PlaceDetectionClient detect;


    GeoDataClient geo;

    FusedLocationProviderClient fuse;

    Bundle mapViewBundle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);




        TAG = EntryActivity.class.getSimpleName();



        Intent incomingIntent = getIntent();
        String incomingPath = incomingIntent.getStringExtra("entryPath");
        String incomingPlaceID = incomingIntent.getStringExtra("entryPlaceID");

         currentEntryRef = db.document(incomingPath);
         buildingRef = db.collection("Building");

       // Toast.makeText(EntryActivity.this, incomingPath, Toast.LENGTH_SHORT).show();

        txtName = (TextView) findViewById(R.id.entName);
        txtType = (TextView) findViewById(R.id.entType);
        txtTimes = (TextView) findViewById(R.id.entTimes);
        txtBuilding = (TextView) findViewById(R.id.entBuilding);
        txtDate = (TextView) findViewById(R.id.entDate);
        venueMap = (MapView)  findViewById(R.id.entMap);

        currentEntryRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Entry ent = documentSnapshot.toObject(Entry.class);



                txtName.setText(ent.getModuleCode() + " - " + ent.getModuleName());
                txtDate.setText(ent.getDate());
                txtTimes.setText(ent.getStartTime() + " - " + ent.getEndTime());
                txtBuilding.setText(ent.getBuilding() + " " + ent.getRoom());

                strBuilding = ent.getBuilding();


                if (ent.getClassType().equals("L")) {
                    txtType.setText("Lecture");
                }
                else if (ent.getClassType().equals("T")){
                    txtType.setText("Tutorial");
                }

                //     getIDForBuilding(strBuilding);







            }
        });







      //  Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }







        detect = Places.getPlaceDetectionClient(this,null);

       geo = Places.getGeoDataClient(this, null);

   fuse = LocationServices.getFusedLocationProviderClient(this);


        geo.getPlaceById(incomingPlaceID).addOnSuccessListener(new OnSuccessListener<PlaceBufferResponse>() {
            @Override
            public void onSuccess(PlaceBufferResponse places) {
                Place placey = places.get(0);

                lattylingy =   placey.getLatLng();

                //    venueMap.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("marker"));

                Log.d(TAG, "Name is " + placey.getName());

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Places thing aint work");
                    }
                });

        venueMap.onCreate(mapViewBundle);

        venueMap.getMapAsync(this);

        String URL = "https://api.openweathermap.org/data/2.5/forecast?q=cork,ie&mode=json&appid=cb191071b4dcca0d09fab79daa6bdb30";







//


    //    venueMap.onCreate(mapViewBundle);

    //    venueMap.getMapAsync(this);






    }


  //  public void loadEntryData(){




  //  }





    public String getApiID(){

        buildingRef.whereEqualTo("BuildingName", strBuilding).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots){
                    Building building = documentSnapshot1.toObject(Building.class);

                    strBuildID = building.getPlaceID();



                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Firestore Query failed, chief");
            }
        });

        return strBuildID;


    }




    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        venueMap.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        venueMap.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();





        venueMap.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        venueMap.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {

   //     String smile4theDebugger = strBuildID;



        map.addMarker(new MarkerOptions().position(lattylingy).title(strBuilding));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(lattylingy, 18));
    }

    @Override
    protected void onPause() {
        venueMap.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        venueMap.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        venueMap.onLowMemory();
    }


}
