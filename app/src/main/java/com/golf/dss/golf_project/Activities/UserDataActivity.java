package com.golf.dss.golf_project.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.golf.dss.golf_project.Classes.User;
import com.golf.dss.golf_project.Database.GolfDatabase;
import com.golf.dss.golf_project.R;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class UserDataActivity extends AppCompatActivity implements View.OnClickListener{
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

    public UserDataActivity () {this.db = null;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
       if(this.checkPermissionForReadExtertalStorage() == false){
           try {
               this.requestPermissionForReadExtertalStorage();
           } catch (Exception e) {
               e.printStackTrace();
           }
       }

        if(this.checkPermissionForWriteExtertalStorage() == false){
            try {
                this.requestPermissionForWriteExtertalStorage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.db = GolfDatabase.getInstance(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //Components
        this.rbGender = (RadioGroup) findViewById(R.id.rbGender);
        this.rbMale = (RadioButton) findViewById(R.id.rbMale);
        this.rbFemale = (RadioButton) findViewById(R.id.rbFemale);
        this.spinnerAge = (MaterialSpinner) findViewById(R.id.spinnerAge);
        this.spinnerHeight = (MaterialSpinner) findViewById(R.id.spinnerHeight);
        this.spinnerWeight = (MaterialSpinner) findViewById(R.id.spinnerWeight);
        this.spinnerLevel = (MaterialSpinner) findViewById(R.id.spinnerLevel);
        this.spinnerStyle = (MaterialSpinner) findViewById(R.id.spinnerStylePlay);
        this.spinnerFrequency = (MaterialSpinner) findViewById(R.id.spinnerFrequency);
        this.spinnerExpTime = (MaterialSpinner) findViewById(R.id.spinnerExpTime);
        this.etFirstname = (EditText) findViewById(R.id.etFirstName);
        this.btnSaveUserData = (Button) findViewById(R.id.btnSaveUserData);


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.age_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAge.setAdapter(adapter);

        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.height_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHeight.setAdapter(adapter);

        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.weight_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWeight.setAdapter(adapter);

        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.level_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevel.setAdapter(adapter);

        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.stylePlay_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStyle.setAdapter(adapter);

        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.trainingFrequency_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrequency.setAdapter(adapter);

        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.experienceTime_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExpTime.setAdapter(adapter);

        //Listeners
        this.rbGender.setOnClickListener(this);
        this.btnSaveUserData.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == this.rbGender.getId()){
            int selectedId = this.rbGender.getCheckedRadioButtonId();
            if(selectedId == this.rbMale.getId()){
                this.rbFemale.setSelected(false);
            }else if(selectedId == this.rbFemale.getId()){
                this.rbMale.setSelected(false);
            }
        }else if(v.getId() == this.btnSaveUserData.getId()){
            RadioButton selectedRadioButton = (RadioButton) findViewById(rbGender.getCheckedRadioButtonId());
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
            db.insertConnectedUser(user);
        }
    }

    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public boolean checkPermissionForWriteExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void requestPermissionForWriteExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}