package com.golf.dss.golf_project.Tools;

import android.content.Context;
import android.util.Log;

import com.golf.dss.golf_project.Activities.MapsActivity;
import com.golf.dss.golf_project.AsyncTask.AsyncTaskElevation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class MapTools {
    double myElevation;

    public static void getPointElevation(Context context, double latitude, double longitude){
        final Double[] elevation = new Double[1];
        HashMap<String, String> myParams = new HashMap<>();
        myParams.put("key", "AIzaSyCQ3Gtm26b1IUrmMI4UUjf3b3jvpZboAJE");
        myParams.put("locations", String.valueOf(latitude) + "," + String.valueOf(longitude));
        AsyncTaskElevation elevationApi = new AsyncTaskElevation(context, myParams);
        elevationApi.setOnCompleteListener(new OnCompleteListenerAsync() {
            @Override
            public void onCompleteAsync(String str) {
                Log.d(TAG, str);
                try {
                    JSONObject jsonObj = new JSONObject(str.toString());
                    JSONArray resultEl = jsonObj.getJSONArray("results");
                    JSONObject current = resultEl.getJSONObject(0);
                    MapsActivity.aimElevation = Double.parseDouble(current.getString("elevation"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        elevationApi.execute();

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
