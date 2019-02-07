package com.example.mark.fyputable;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Nullable;



/*

Reference 1: Declaring Maps Objects:
Reference 2: Several methods for populating MapView: https://github.com/googlemaps/android-samples/blob/master/ApiDemos/java/app/src/main/java/com/example/mapdemo/RawMapViewDemoActivity.java
Reference 3: Set focus for map: https://stackoverflow.com/questions/18932325/android-how-to-focus-on-current-position
Reference 4: Pulling Location Data from firebase:https://www.youtube.com/watch?v=X5AGMpMV7Ks&t=376s
Reference 5: Populating Textviews from Firebase: https://www.youtube.com/watch?v=di5qmolrFVs&index=3&list=PLrnPJCHvNZuDrSqu-dKdDi3Q6nM-VUyxD
Reference 6: Getting Weather Data: https://www.youtube.com/watch?v=8-7Ip6xum6E&list=LLFAzQ3TDMkLzWEEv3gyfvFQ&index=11&t=0s
Reference 7: Extracting Latitute/Longitude co-ordinates from lat/lng variable: https://stackoverflow.com/questions/24256478/pattern-to-extract-text-between-parenthesis
Reference 8: Opening Maps App: https://developers.google.com/maps/documentation/urls/android-intents



*/
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
    TextView txtTemp;
    TextView txtWeather;
    String TAG;
    MapView venueMap;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    LatLng latLng;
    String strBuilding;
    String strBuildID;
    PlaceDetectionClient detect;
    String timeForAPI;
    String dateForAPI;
    String dateTimeForAPI;
    SimpleDateFormat api = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat dmy = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat HH = new SimpleDateFormat("HH:mm:ss");
    String URL;
    Button btnMaps;

    Calendar cal = Calendar.getInstance();


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


        txtName = (TextView) findViewById(R.id.entName);
        txtType = (TextView) findViewById(R.id.entType);
        txtTimes = (TextView) findViewById(R.id.entTimes);
        txtBuilding = (TextView) findViewById(R.id.entBuilding);
        txtDate = (TextView) findViewById(R.id.entDate);

        txtWeather = (TextView)findViewById(R.id.entWeather);
        btnMaps = (Button) findViewById(R.id.btnOpenMaps);

        venueMap = (MapView)  findViewById(R.id.entMap);


        //Reference 4
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

                String DateParam = ent.getDate();
                setTimeForWeatherAPI(ent.getStartTime());
                setDateForWeatherAPI(DateParam);







            }
        });


        //Ref 2
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }






        //Ref 1
        detect = Places.getPlaceDetectionClient(this,null);

       geo = Places.getGeoDataClient(this, null);

   fuse = LocationServices.getFusedLocationProviderClient(this);


        geo.getPlaceById(incomingPlaceID).addOnSuccessListener(new OnSuccessListener<PlaceBufferResponse>() {
            @Override
            public void onSuccess(PlaceBufferResponse places) {
                Place placey = places.get(0);

                latLng =   placey.getLatLng();

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

        //Reference 2
        venueMap.onCreate(mapViewBundle);

        venueMap.getMapAsync(this);

        String URL1 = "https://api.openweathermap.org/data/2.5/forecast?q=cork,ie&mode=json&appid=cb191071b4dcca0d09fab79daa6bdb30&units=Metric";

        //Reference 6
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET,URL1,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            try{
                int i = 0;
                while(i < 40){

                    JSONArray array = response.getJSONArray("list");
                    JSONObject obj1 = array.getJSONObject(i);
                    JSONObject objMain = obj1.getJSONObject("main");
                    Double temp = objMain.getDouble("temp");
                    JSONArray array2 = obj1.getJSONArray("weather");
                    JSONObject objWeath = array2.getJSONObject(0);
                    String Weath = objWeath.getString("main");
                    String sysDate = obj1.getString("dt_txt");

                    if (sysDate.equals(dateTimeForAPI)){

                        Long Temperature = Math.round(temp);
                        txtWeather.setText(Weath +", "+Temperature.toString()+"°C"  );
                    }

                    i++;
                }

                if (txtWeather.getText().equals("Weather Loading...")){
                    txtWeather.setText("Weather info unavailable");

                }

            }catch(JSONException e){
                e.printStackTrace();
            }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);


       btnMaps.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               //Reference 7
               String latlong = latLng.toString();
               String coordiates = latlong.substring(latlong.indexOf("(")+1,latlong.indexOf(")"));

               //Reference 8
               Uri gmUri = Uri.parse("google.navigation:q="+coordiates+"&mode=w");
               Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmUri);
               mapIntent.setPackage("com.google.android.apps.maps");
               startActivity(mapIntent);
           }
       });




    }



    public void setTimeForWeatherAPI(String startTime){

        if (startTime.equals("08:00") || startTime.equals("09:00") || startTime.equals("10:00")){

            timeForAPI = "09:00:00";
        }
        else  if (startTime.equals("11:00") || startTime.equals("12:00") || startTime.equals("13:00")){

            timeForAPI = "12:00:00";
        }
        else  if (startTime.equals("14:00") || startTime.equals("15:00") || startTime.equals("16:00")){

            timeForAPI = "15:00:00";
        }
        else  if (startTime.equals("17:00") || startTime.equals("18:00") || startTime.equals("19:00")){

            timeForAPI = "18:00:00";
        }

    }

    public void setDateForWeatherAPI(String date1){

        try{
            String currentTime = HH.format(cal.getTime());
            String currentDate = dmy.format(cal.getTime());


            //Isn't being used yet
            if (date1.equals(currentDate)){

                URL = "https://api.openweathermap.org/data/2.5/weather?q=cork,ie&mode=json&appid=cb191071b4dcca0d09fab79daa6bdb30&units=Metric";

            }

            Date date = dmy.parse(date1);
            cal.setTime(date);
            String dtTest = api.format(cal.getTime());
            dateForAPI = dtTest;
            dateTimeForAPI = (dateForAPI + " " + timeForAPI);

            System.out.print(dateTimeForAPI);

        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }




    //Reference 4
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
                Log.d(TAG, "Firestore Query failed");
            }
        });

        return strBuildID;

    }



    //Reference 2
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

    //Reference 2
    @Override
    protected void onResume() {
        super.onResume();
        venueMap.onResume();
    }

    //Reference 2
    @Override
    protected void onStart() {
        super.onStart();
        venueMap.onStart();
    }

    //Reference 2
    @Override
    protected void onStop() {
        super.onStop();
        venueMap.onStop();
    }

    //Reference 2
    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(latLng).title(strBuilding));
        //Ref 3
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
    }

    //Reference 2
    @Override
    protected void onPause() {
        venueMap.onPause();
        super.onPause();
    }

    //Reference 2
    @Override
    protected void onDestroy() {
        venueMap.onDestroy();
        super.onDestroy();
    }

    //Reference 2
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        venueMap.onLowMemory();
    }


}
