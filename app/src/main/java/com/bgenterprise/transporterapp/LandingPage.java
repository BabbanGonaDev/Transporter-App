package com.bgenterprise.transporterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bgenterprise.transporterapp.Database.PopulateLocation;
import com.google.android.material.button.MaterialButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LandingPage extends AppCompatActivity {
    @BindView(R.id.btn_add_transporter)
    MaterialButton btnAddTransporter;

    SessionManager sessionM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        ButterKnife.bind(this);
        sessionM = new SessionManager(LandingPage.this);
        importLocations();
        sessionM.CLEAR_ALL_SESSION();



        //TODO --> Check Phone date if it's earlier than date of app development.
    }

    @OnClick(R.id.btn_add_transporter)
    public void addTransport(){
        startActivity(new Intent(LandingPage.this, AddTransporter.class));
    }

    public void importLocations(){
        if(!sessionM.getImportStatus()){
            PopulateLocation populateLocation = new PopulateLocation(LandingPage.this){
                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    sessionM.SET_IMPORT_LOCATION(true);
                    Toast.makeText(LandingPage.this, "Locations Successfully inserted", Toast.LENGTH_LONG).show();
                }
            };populateLocation.execute();
        }
    }
}
