package com.golf.dss.golf_project.DialogWindows;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.golf.dss.golf_project.Activities.MapsActivity;
import com.golf.dss.golf_project.AsyncTask.AsyncTaskWeather;
import com.golf.dss.golf_project.Classes.Club;
import com.golf.dss.golf_project.Classes.Shot;
import com.golf.dss.golf_project.Database.GolfDatabase;
import com.golf.dss.golf_project.R;
import com.golf.dss.golf_project.Tools.MapTools;
import com.golf.dss.golf_project.Tools.OnCompleteListenerAsync;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CustomDialogMessage {

    public void dialogClubAdvise(final Context context, int shootDistance, String windDirection, String windSpeed, int elevation, final Club adviseClub, final Location startLocation) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_advise_club);

        //Get shared preferences nb shots
        SharedPreferences prefs = context.getSharedPreferences("SHOTS", 0);
        int nbShots = prefs.getInt("nbShots", 0);

        ImageButton dialogbBnValidateShoot = dialog.findViewById(R.id.dialogbBnValidateShoot);
        TextView dialogWindSpeed = dialog.findViewById(R.id.dialogWindSpeed);
        TextView dialogElevation = dialog.findViewById(R.id.dialogElevation);
        TextView dialogClubName = dialog.findViewById(R.id.dialogClubName);
        TextView dialogDistance = dialog.findViewById(R.id.dialogDistance);

        dialogDistance.setText(shootDistance + " yards");
        dialogWindSpeed.setText(windDirection + " - " + windSpeed + " m/s");
        dialogElevation.setText(String.valueOf(elevation) + " m");
        if(nbShots == 0){
            dialogClubName.setText("Driver");
        }else{
            dialogClubName.setText(adviseClub.getName());
        }

        dialogbBnValidateShoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get shared preferences nb shots
                SharedPreferences prefs = context.getSharedPreferences("SHOTS", 0);
                int nbShots = prefs.getInt("nbShots", 0);
                //Edit shared preferences shot + 1
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("nbShots");
                editor.putInt("nbShots", nbShots + 1);
                editor.commit();

                CustomDialogMessage c = new CustomDialogMessage();
                c.dialogWalkBall(context, startLocation, adviseClub);
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void dialogWalkBall(final Context context, final Location startLocation, final Club adviseClub) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_walk_ball);
        dialog.show();

        final ImageButton dialogBtnGetBall = dialog.findViewById(R.id.dialogBtnGetBall);
        final ImageButton dialogBtnFinish = dialog.findViewById(R.id.dialogBtnFinish);

        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        @SuppressLint("MissingPermission") final Task location = mFusedLocationProviderClient.getLastLocation();

        location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    final Location currentLocation = (Location) task.getResult();
                    final Shot shot = new Shot(startLocation.getLatitude(),
                            startLocation.getLongitude(),
                            currentLocation.getLatitude(),
                            currentLocation.getLongitude(),
                            startLocation.distanceTo(currentLocation)
                    );

                    final GolfDatabase db = GolfDatabase.getInstance(context);

                    dialogBtnGetBall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO Get ball action
                            db.insertShot(shot, adviseClub.getId());
                            MapsActivity.mapsActivity.finish(); //Close the old map activity
                            context.startActivity(new Intent(context, MapsActivity.class)); //Start new activity
                            dialog.cancel();
                        }
                    });

                    dialogBtnFinish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO Finish action

                            //Get shared preferences nb shots
                            SharedPreferences prefs = context.getSharedPreferences("SHOTS", 0);
                            int nbShots = prefs.getInt("nbShots", 0);
                            //Edit shared preferences shot + 1
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.remove("nbShots");
                            editor.putInt("nbShots", 0); //Reset nb shot 0 -> User finished playing
                            editor.commit();

                            MapsActivity.mapsActivity.finish(); //Close the old map activity
                            context.startActivity(new Intent(context, MapsActivity.class)); //Start new activity
                            dialog.cancel();
                        }
                    });
                }
            }
        });


    }
}
