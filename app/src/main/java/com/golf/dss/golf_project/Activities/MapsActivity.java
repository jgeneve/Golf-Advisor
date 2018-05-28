package com.golf.dss.golf_project.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.golf.dss.golf_project.AsyncTask.AsyncTaskWeather;
import com.golf.dss.golf_project.Classes.User;
import com.golf.dss.golf_project.Database.GolfDatabase;
import com.golf.dss.golf_project.DialogWindows.CustomDialogMessage;
import com.golf.dss.golf_project.R;
import com.golf.dss.golf_project.Tools.OnCompleteListenerAsync;
import com.golf.dss.golf_project.Tools.MapTools;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by User on 10/2/2017.
 */

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, OnCompleteListenerAsync {


    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 18f;
    private Marker clickedMarker = null;
    private Polyline polyline = null;
    private GolfDatabase db = null;
    private ImageButton btnValidateShoot;
    private ImageButton btnFinish;
    private TextView textViewWind;
    private TextView textViewWindDirection;
    private TextView tvUserName;
    private TextView tvUserLevel;
    private TextView tvElevation;
    private TextView tvNbShoots;
    private ImageView ivSettings;
    private ImageView ivUserPic;

    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LatLng aimLocation;
    public static double aimElevation;
    private JSONObject jsonWeather;
    String windSpeed;
    String windDirection;
    public static Activity mapsActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getLocationPermission();
        mapsActivity = this;

        this.db = GolfDatabase.getInstance(this);
        User user = this.db.getConnectedUser();

        //Get shared preferences nb shots
        SharedPreferences prefs = getSharedPreferences("SHOTS", 0);
        int nbShots = prefs.getInt("nbShots", 0);

        //Find elements
        this.aimElevation = 0.0;
        this.btnValidateShoot = findViewById(R.id.btnValidateShoot);
        this.textViewWind = findViewById(R.id.textViewWind);
        this.textViewWindDirection = findViewById(R.id.textViewWindDirection);
        this.textViewWindDirection = findViewById(R.id.textViewWindDirection);
        this.tvUserName = findViewById(R.id.tvUserName);
        this.tvUserLevel = findViewById(R.id.tvUserLevel);
        this.tvNbShoots = findViewById(R.id.tvNbShoots);
        this.ivSettings = findViewById(R.id.ivSettings);
        this.ivUserPic = findViewById(R.id.ivUserPic);
        this.btnFinish = findViewById(R.id.btnFinish);

        //Fill textview
        this.tvUserName.setText(user.getFirstname());
        this.tvUserLevel.setText("Level : "+user.getLevel().toLowerCase());
        tvNbShoots.setText(String.valueOf(nbShots));
        if(user.getGender().toLowerCase().equals("female")){
            this.ivUserPic.setImageResource(R.mipmap.ic_user_woman);
        }

        //Set listeners
        btnValidateShoot.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        this.ivSettings.setOnClickListener(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

            try{
                if(mLocationPermissionsGranted){

                    final Task location = mFusedLocationProviderClient.getLastLocation();
                    location.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Log.d(TAG, "onComplete: found location!");
                                Location currentLocation = (Location) task.getResult();

                                HashMap<String, String> myParams = new HashMap<>();
                                myParams.put("APPID", getResources().getString(R.string.weatherApiKey));
                                myParams.put("lat", String.valueOf(currentLocation.getLatitude()));
                                myParams.put("lon", String.valueOf(currentLocation.getLongitude()));
                                // myParams.put("units", "metric");

                                AsyncTaskWeather weatherApi = new AsyncTaskWeather(getApplicationContext(), myParams);
                                weatherApi.setOnCompleteListener(new OnCompleteListenerAsync() {
                                    @Override
                                    public void onCompleteAsync(String str) {
                                        Log.d(TAG, str);
                                        try {
                                            jsonWeather = new JSONObject(str);
                                            JSONObject windObj = jsonWeather.getJSONObject("wind");
                                            windSpeed = windObj.getString("speed");
                                            windDirection =  MapTools.getWindDirection(windObj.getInt("deg"));
                                            textViewWind.setText(windSpeed + "m/s");
                                            textViewWindDirection.setText(windDirection+" - ");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                weatherApi.execute();
                            }
                        }
                    });
                }
            }catch (SecurityException e){
                Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
            }


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(false); // Disable itinerary and streetview on map
            mMap.setPadding(0, 450, 0, 400);

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(final LatLng latLng) {
                    if (clickedMarker != null) {
                        clickedMarker.remove();
                        polyline.remove();
                    }
                    MarkerOptions options = new MarkerOptions()
                            .position(latLng)
                            .title("Aiming point")
                            .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("ic_target",60,60)))
                            .anchor(0.5f, 0.5f);
                    clickedMarker = mMap.addMarker(options);
                    aimLocation = latLng;

                    mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
                    try{
                        if(mLocationPermissionsGranted){
                            final Task location = mFusedLocationProviderClient.getLastLocation();
                            location.addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()){
                                        Location currentLocation = (Location) task.getResult();
                                        PolylineOptions polylineOptions = new PolylineOptions()
                                                .add(latLng, new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                                                .width(10)
                                                .color(Color.RED);
                                        polyline = mMap.addPolyline(polylineOptions);

                                    }else{
                                        Log.d(TAG, "onComplete: current location is null");
                                        Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }catch (SecurityException e){
                        Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
                    }
                }
            });
        }
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),DEFAULT_ZOOM);

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapsActivity.this);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnValidateShoot.getId()){
            if(aimLocation != null){
                MapTools.getAdviceClub(MapsActivity.this, aimLocation, windDirection, windSpeed);
            }else{
                Toast.makeText(this, "Please click where you want to aim before validate", Toast.LENGTH_LONG).show();
            }
        }

        if(v.getId() == btnFinish.getId()){
            CustomDialogMessage c = new CustomDialogMessage();
            c.dialogFinish(this);
        }

        if(v.getId() == this.ivSettings.getId()){
            PopupMenu popupMenu = new PopupMenu(this,this.ivSettings);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { //Listener for click on an item of the menu
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getTitle().toString().toLowerCase().equals("my information")){
                        Intent userData = new Intent(getApplicationContext(), UserDataActivity.class); //Create new activity
                        userData.putExtra("modify", "true"); //Say to the activity that the user want to modify his information
                        getApplicationContext().startActivity(userData); //Start new activity
                    }
                    return true;
                }
            });

            popupMenu.show();
        }
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "mipmap", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    public void onCompleteAsync(String result) {
        Log.d(TAG, result);
    }
}