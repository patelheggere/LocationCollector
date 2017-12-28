package com.patelheggere.locationcollector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.patelheggere.locationcollector.models.PAModel;

import java.util.Date;

public class AddPAActivity extends AppCompatActivity {

    private double lat, lon;
    private EditText etHouseNo, etHouseName, etStreetName, etLocality, etSBL1, etSB2, etSB3, etPin, etLat, etLon;
    private Button mSubmit, mCancel;
    private String mName = "";
    private String mMoobile = "";
    private String mUid = "";
    private  boolean flag = false;
    private  PAModel paModel = new PAModel();
    private DatabaseReference mDBRef;
    private FirebaseAuth mAuth;
    //test
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupwindowfor_pa);
        lat = getIntent().getDoubleExtra("lat", 0.0f);
        lon = getIntent().getDoubleExtra("lon", 0.0f);
        mName = getIntent().getStringExtra("Name");
        mMoobile = getIntent().getStringExtra("mobile");
        mUid = getIntent().getStringExtra("uid");
        mAuth = FirebaseAuth.getInstance();
        initialiseUI();
    }

    private void initialiseUI()
    {
        getSupportActionBar().setTitle("Add PA Details");
        etHouseNo = findViewById(R.id.etHouseNo);
        etHouseName = findViewById(R.id.etHouseName);
        etStreetName = findViewById(R.id.etStreetName);
        etLocality = findViewById(R.id.etLocality);
        etSBL1 = findViewById(R.id.etSubLocality1);
        etSB2 = findViewById(R.id.etSubLocality2);
        etSB3 = findViewById(R.id.etSubLocality3);
        etPin = findViewById(R.id.etPIN);
        etLat = findViewById(R.id.etLat);
        etLon = findViewById(R.id.etLon);
        etLat.setText(String.valueOf(lat));
        etLon.setText(String.valueOf(lon));
        mSubmit = findViewById(R.id.btnSubmit);
        mCancel = findViewById(R.id.cancel);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void submit()
    {



        if(!etLocality.getText().toString().equals(""))
            paModel.setEtLocality((etLocality.getText().toString()));
        else {
            etLocality.setError("Should not be empty");
            flag = false;
            return;
        }

        if(!etPin.getText().toString().equals("") || etPin.getText().length()==6)
            paModel.setEtPin((etPin.getText().toString()));
        else {
            etPin.setError("Should not be empty or PIN must be 6 digit");
            flag = false;
            return;
        }
        flag = true;
        paModel.setEtSBL1(etSBL1.getText().toString());
        paModel.setEtSB2(etSB2.getText().toString());
        paModel.setEtSB3(etSB3.getText().toString());
        paModel.setEtLat(String.valueOf(lat));
        paModel.setEtLon(String.valueOf(lon));
        paModel.setAddedById(mUid);
        paModel.setAddedByName(mAuth.getCurrentUser().getDisplayName().toString());
        paModel.setAddedByPhone(mMoobile);
        if(flag)
            uploadData();
        else {
            Toast.makeText(AddPAActivity.this, "check all the feilds", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadData()
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mDBRef = firebaseDatabase.getReference().child("PACollections").child(mMoobile);
        paModel.setmDate( new Date().getTime());
        mDBRef.push().setValue(paModel);
        etHouseNo.setText("");
        etHouseName.setText("");
        etStreetName.setText("");
        etLocality.setText("");
        etSBL1.setText("");
        etSB2.setText("");
        etSB3.setText("");
        etPin.setText("");
        //Toast.makeText(AddPAActivity.this, etHouseNo.getText().toString()+"\n"+ etHouseName.getText().toString()+"\n"+etStreetName.getText().toString()+"\n"+etLat.getText().toString()+"\n"+etLon.getText().toString(), Toast.LENGTH_SHORT).show();
    }

}
