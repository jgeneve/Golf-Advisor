package com.golf.dss.golf_project.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.LinearLayout;

import com.golf.dss.golf_project.Tools.OnCompleteListenerAsync;
import com.google.android.gms.tasks.OnCompleteListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class AsyncTaskWeather extends AsyncTask<Void, Void, String> {
    public ArrayList<OnCompleteListenerAsync> listeners;
    private HashMap<String, String> myParams;
    private Context myContext;
    private String url;

    public AsyncTaskWeather(Context myContext, HashMap<String, String> myParams) {
        this.listeners = new ArrayList();
        this.myParams = myParams;
        this.myContext = myContext;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        try {
            url = getPostDataString(myParams);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    protected String doInBackground(Void... arg0) {
        return performPostCall("http://api.openweathermap.org/data/2.5/weather?", myParams); //Call the webservice
    }

    protected void onPostExecute(String result) {
        for (OnCompleteListenerAsync listener : listeners) {
            listener.onCompleteAsync(result);
        }
    }

    public String performPostCall(String requestURL, HashMap<String, String> postDataParams) {
        String response = "";
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(requestURL+url).openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            os.close();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
            result.append(URLEncoder.encode((String) entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode((String) entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    public void setOnCompleteListener(OnCompleteListenerAsync listener) {
        this.listeners.add(listener);
    }
}
