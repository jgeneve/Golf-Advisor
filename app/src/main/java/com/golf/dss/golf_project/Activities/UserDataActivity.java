package com.golf.dss.golf_project.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.golf.dss.golf_project.Classes.User;
import com.golf.dss.golf_project.Database.GolfDatabase;
import com.golf.dss.golf_project.R;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.File;

public class UserDataActivity extends AppCompatActivity implements OnClickListener, OnRequestPermissionsResultCallback {
    private GolfDatabase db;
    private RadioGroup rbGender;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private MaterialSpinner spinnerAge;
    private MaterialSpinner spinnerHeight;
    private MaterialSpinner spinnerWeight;
    private MaterialSpinner spinnerLevel;
    private MaterialSpinner spinnerStyle;
    private MaterialSpinner spinnerFrequency;
    private MaterialSpinner spinnerExpTime;
    private EditText etFirstname;
    private Button btnSaveUserData;

    public UserDataActivity() {
        this.db = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        if (this.checkPermissionForLocation() == false) {
            try {
                this.requestPermissionForLocation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Create or open database
        this.db = GolfDatabase.getInstance(this);

        //Components
        this.rbGender = findViewById(R.id.rbGender);
        this.rbMale = findViewById(R.id.rbMale);
        this.rbFemale = findViewById(R.id.rbFemale);
        this.spinnerAge = findViewById(R.id.spinnerAge);
        this.spinnerHeight = findViewById(R.id.spinnerHeight);
        this.spinnerWeight = findViewById(R.id.spinnerWeight);
        this.spinnerLevel = findViewById(R.id.spinnerLevel);
        this.spinnerStyle = findViewById(R.id.spinnerStylePlay);
        this.spinnerFrequency = findViewById(R.id.spinnerFrequency);
        this.spinnerExpTime = findViewById(R.id.spinnerExpTime);
        this.etFirstname = findViewById(R.id.etFirstName);
        this.btnSaveUserData = findViewById(R.id.btnSaveUserData);

        //All adapters for spinners
        ArrayAdapter adapterAge = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.age_array));
        adapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAge.setAdapter(adapterAge);

        ArrayAdapter adapterHeight = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.height_array));
        adapterHeight.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHeight.setAdapter(adapterHeight);

        ArrayAdapter adapterWeight = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.weight_array));
        adapterWeight.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWeight.setAdapter(adapterWeight);

        ArrayAdapter adapterLevel = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.level_array));
        adapterLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevel.setAdapter(adapterLevel);

        ArrayAdapter adapterStyle = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.stylePlay_array));
        adapterStyle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStyle.setAdapter(adapterStyle);

        ArrayAdapter adapterFrequency = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.trainingFrequency_array));
        adapterFrequency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrequency.setAdapter(adapterFrequency);

        ArrayAdapter adapterExpTime = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.experienceTime_array));
        adapterExpTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExpTime.setAdapter(adapterExpTime);

        //Listeners
        this.rbGender.setOnClickListener(this);
        this.btnSaveUserData.setOnClickListener(this);

        if (getIntent().getStringExtra("modify") != null){
            //If the user wants to modify his data
            User user = this.db.getConnectedUser();
            etFirstname.setText(user.getFirstname()); //Fill firstname
            if(user.getGender().toLowerCase().equals("male")){ //Select gender
                rbMale.setChecked(true);
                rbFemale.setChecked(false);
            }else{
                rbFemale.setChecked(true);
                rbMale.setChecked(false);
            }
            spinnerAge.setSelectedIndex(adapterAge.getPosition(user.getAge()));
            spinnerHeight.setSelectedIndex(adapterHeight.getPosition(user.getHeight()));
            spinnerWeight.setSelectedIndex(adapterWeight.getPosition(user.getWeight()));
            spinnerLevel.setSelectedIndex(adapterLevel.getPosition(user.getLevel()));
            spinnerStyle.setSelectedIndex(adapterStyle.getPosition(user.getStyle()));
            spinnerFrequency.setSelectedIndex(adapterFrequency.getPosition(user.getFrequency()));
            spinnerExpTime.setSelectedIndex(adapterExpTime.getPosition(user.getExpTime()));
        }else if (this.db.getConnectedUser() != null) { //If the user already get an account -> Skip to the map
            startActivity(new Intent(getApplicationContext(), MapsActivity.class)); //Start new activity
            this.finish(); //Close this activity
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == this.rbGender.getId()) { //Click on RadioButton
            int selectedId = this.rbGender.getCheckedRadioButtonId();
            if (selectedId == this.rbMale.getId()) {
                this.rbFemale.setSelected(false);
            } else if (selectedId == this.rbFemale.getId()) {
                this.rbMale.setSelected(false);
            }
        } else if (v.getId() == this.btnSaveUserData.getId()) { //Click on button save
            if(etFirstname.getText() != null){
                RadioButton selectedRadioButton = findViewById(rbGender.getCheckedRadioButtonId());
                User user = new User(
                        etFirstname.getText().toString(),
                        spinnerAge.getText().toString(),
                        selectedRadioButton.getText().toString(),
                        spinnerHeight.getText().toString(),
                        spinnerWeight.getText().toString(),
                        spinnerLevel.getText().toString(),
                        spinnerStyle.getText().toString(),
                        spinnerFrequency.getText().toString(),
                        spinnerExpTime.getText().toString()
                );
                if (getIntent().getStringExtra("modify") != null){
                    db.deleteConnectedUser();
                    db.insertConnectedUser(user);
                    MapsActivity.mapsActivity.finish(); //Close the old map activity
                    startActivity(new Intent(getApplicationContext(), MapsActivity.class)); //Start new activity
                    finish(); //Close this activity
                }else{
                    SharedPreferences settings = getSharedPreferences("SHOTS", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("nbShots", 0);
                    editor.commit();

                    db.deleteAllData();
                    db.insertConnectedUser(user);
                    db.insertDefaultClubs();
                    startActivity(new Intent(getApplicationContext(), MapsActivity.class)); //Start new activity
                    finish(); //Close this activity
                }
            }else{
                etFirstname.setError("Please enter your firstname");
            }


        }
    }

    public boolean checkPermissionForLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForLocation() {
        try {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean checkPermissionForInternet() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = this.checkSelfPermission(Manifest.permission.INTERNET);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForInternet() {
        try {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 2);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            if (this.checkPermissionForInternet() == false) {
                try {
                    this.requestPermissionForInternet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}