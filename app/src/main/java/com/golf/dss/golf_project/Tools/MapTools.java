package com.golf.dss.golf_project.Tools;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.golf.dss.golf_project.Activities.MapsActivity;
import com.golf.dss.golf_project.AsyncTask.AsyncTaskElevation;
import com.golf.dss.golf_project.Classes.Club;
import com.golf.dss.golf_project.Classes.User;
import com.golf.dss.golf_project.Database.GolfDatabase;
import com.golf.dss.golf_project.DialogWindows.CustomDialogMessage;
import com.golf.dss.golf_project.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.ContentValues.TAG;
import static com.golf.dss.golf_project.Activities.MapsActivity.aimElevation;

public class MapTools {
    double myElevation;
    private static FusedLocationProviderClient mFusedLocationProviderClient;

    public static void getPointElevation(final Context context, double latitude, double longitude) {

    }

    public static void getAdviceClub(final Context context, final LatLng aimLocation, final String windDirection, final String windSpeed) {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        @SuppressLint("MissingPermission") final Task location = mFusedLocationProviderClient.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    final Location currentLocation = (Location) task.getResult();
                    currentLocation.setAltitude(currentLocation.getAltitude()*0.3048); // Convert to metters
                    final Location destLocation = new Location("aimLocation");
                    destLocation.setLatitude(aimLocation.latitude);
                    destLocation.setLongitude(aimLocation.longitude);

                    final Double[] elevation = new Double[1];
                    HashMap<String, String> myParams = new HashMap<>();
                    myParams.put("key", "AIzaSyCQ3Gtm26b1IUrmMI4UUjf3b3jvpZboAJE");
                    myParams.put("locations", String.valueOf(aimLocation.latitude) + "," + String.valueOf(aimLocation.longitude));
                    AsyncTaskElevation elevationApi = new AsyncTaskElevation(context, myParams);
                    elevationApi.setOnCompleteListener(new OnCompleteListenerAsync() {
                        @Override
                        public void onCompleteAsync(String str) {
                            Log.d(TAG, str);
                            try {
                                JSONObject jsonObj = new JSONObject(str.toString());
                                JSONArray resultEl = jsonObj.getJSONArray("results");
                                JSONObject current = resultEl.getJSONObject(0);
                                destLocation.setAltitude(Double.parseDouble(current.getString("elevation")));

                                // CALCULATE DISTANCE
                                int shootingDistance = (int) (currentLocation.distanceTo(destLocation)*1.0936);
                                int shootingElevation = (int) (destLocation.getAltitude() - currentLocation.getAltitude());

                                GolfDatabase db = GolfDatabase.getInstance(context);
                                User user = db.getConnectedUser();

                                String[] ageArr = context.getResources().getStringArray(R.array.age_array);
                                String[] heightArr = context.getResources().getStringArray(R.array.height_array);
                                String[] weightArr = context.getResources().getStringArray(R.array.weight_array);
                                String[] levelArr = context.getResources().getStringArray(R.array.level_array);
                                String[] stylePlayArr = context.getResources().getStringArray(R.array.stylePlay_array);
                                String[] trainingFreqArr = context.getResources().getStringArray(R.array.trainingFrequency_array);
                                String[] expTimeArr = context.getResources().getStringArray(R.array.experienceTime_array);

                                // ELEVATION RATIO
                                float elevationBonus = (float) (shootingElevation * 3.28084 * 1.0936);
                                // ALTITUDE RATIO
                                float altitudeBonus = (float) (currentLocation.getAltitude() * shootingDistance / (100 * 152.4));
                                double playerRatio = 1.0;

                                // ============== AGE ==============
                                if (user.getAge().equals(ageArr[0])){ // < 11
                                    playerRatio *= 0.75;
                                } else if (user.getAge().equals(ageArr[1])){ // 11-15
                                    playerRatio *= 0.85;
                                } else if (user.getAge().equals(ageArr[2])){ // 16-29
                                    playerRatio *= 1.01;
                                } else if (user.getAge().equals(ageArr[3])){ // 30-40
                                    playerRatio *= 1;
                                } else if (user.getAge().equals(ageArr[4])){ // 50+
                                    playerRatio *= 0.95;
                                }

                                // ============== HEIGHT ==============
                                if (user.getHeight().equals(heightArr[0])){ // < 150
                                    playerRatio *= 0.95;
                                } else if (user.getHeight().equals(heightArr[1])){ // 151-170
                                    playerRatio *= 0.97;
                                } else if (user.getHeight().equals(heightArr[2])){ // 171-190
                                    playerRatio *= 1;
                                } else if (user.getHeight().equals(heightArr[3])){ // > 191
                                    playerRatio *= 1.02;
                                }

                                // ============== WEIGHT ==============
                                if (user.getWeight().equals(weightArr[0])){ // < 50
                                    playerRatio *= 0.95;
                                } else if (user.getWeight().equals(weightArr[1])){ // 51-70
                                    playerRatio *= 0.97;
                                } else if (user.getWeight().equals(weightArr[2])){ // 71-90
                                    playerRatio *= 1;
                                } else if (user.getWeight().equals(weightArr[3])){ // > 91
                                    playerRatio *= 1.02;
                                }

                                // ============== LEVEL ==============
                                if (user.getLevel().equals(levelArr[0])){ // beginner
                                    playerRatio *= 0.9;
                                } else if (user.getLevel().equals(levelArr[1])){ // average
                                    playerRatio *= 0.97;
                                } else if (user.getLevel().equals(levelArr[2])){ // good
                                    playerRatio *= 1;
                                } else if (user.getLevel().equals(levelArr[3])){ // expert
                                    playerRatio *= 1.05;
                                }

                                // ============== PLAYING FOR ==============
                                if (user.getStyle().equals(stylePlayArr[0])){ // FOR FUN
                                    playerRatio *= 0.975;
                                } else if (user.getStyle().equals(stylePlayArr[1])){ // TRAINING
                                    playerRatio *= 1;
                                } else if (user.getStyle().equals(stylePlayArr[2])){ // COMPETITION
                                    playerRatio *= 1.025;
                                }

                                // ============== TRAINING FREQUENCY ==============
                                if (user.getFrequency().equals(trainingFreqArr[0])){ // DAILY
                                    playerRatio *= 1.025;
                                } else if (user.getFrequency().equals(trainingFreqArr[1])){ // WEEKLY
                                    playerRatio *= 1.0125;
                                } else if (user.getFrequency().equals(trainingFreqArr[2])){ // MONTHKY
                                    playerRatio *= 1;
                                } else if (user.getFrequency().equals(trainingFreqArr[3])){ // OCCASIONALLY
                                    playerRatio *= 0.975;
                                }

                                // ============== EXP TIME ==============
                                if (user.getExpTime().equals(expTimeArr[0])){ // < 2 years
                                    playerRatio *= 0.95;
                                } else if (user.getExpTime().equals(expTimeArr[1])){ // < 5 years
                                    playerRatio *= 1;
                                } else if (user.getExpTime().equals(expTimeArr[2])){ // < 10 years
                                    playerRatio *= 1.025;
                                } else if (user.getExpTime().equals(expTimeArr[3])){ // > 10 years
                                    playerRatio *= 1.05;
                                }

                                // ============== GENDER ==============
                                if (user.getGender().equals("Male")){

                                } else if (user.getGender().equals("Female")){

                                }

                                //Display the window of the final advise
                                CustomDialogMessage c = new CustomDialogMessage();
                                c.dialogClubAdvise(context, windDirection, windSpeed, shootingElevation, "CLUB NAME");

                                Toast.makeText(context, "Distance: " + (shootingDistance+elevationBonus) + "yards (Elevation included)\n Player ration:" + playerRatio, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    elevationApi.execute();
                }
            }
        });
        String destElevation = String.valueOf((int) aimElevation);
        // Toast.makeText(context, destElevation +" meters", Toast.LENGTH_LONG).show();
    }

    public static String getWindDirection(int deg){
        if (deg>11.25 && deg<33.75){
            return "NNE";
        }else if (deg>33.75 && deg<56.25){
            return "ENE";
        }else if (deg>56.25 && deg<78.75){
            return "E";
        }else if (deg>78.75 && deg<101.25){
            return "ESE";
        }else if (deg>101.25 && deg<123.75){
            return "ESE";
        }else if (deg>123.75 && deg<146.25){
            return "SE";
        }else if (deg>146.25 && deg<168.75){
            return "SSE";
        }else if (deg>168.75 && deg<191.25){
            return "S";
        }else if (deg>191.25 && deg<213.75){
            return "SSW";
        }else if (deg>213.75 && deg<236.25){
            return "SW";
        }else if (deg>236.25 && deg<258.75){
            return "WSW";
        }else if (deg>258.75 && deg<281.25){
            return "W";
        }else if (deg>281.25 && deg<303.75){
            return "WNW";
        }else if (deg>303.75 && deg<326.25){
            return "NW";
        }else if (deg>326.25 && deg<348.75){
            return "NNW";
        }else{
            return "N";
        }
    }

}
