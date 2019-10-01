package com.bgenterprise.transporterapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.babbangona.bg_face.LuxandActivity;
import com.babbangona.bg_face.LuxandInfo;
import com.bgenterprise.transporterapp.Database.PopulateLocation;
import com.bgenterprise.transporterapp.InputPages.AddOperatingAreas;
import com.bgenterprise.transporterapp.InputPages.AddTransporter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

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
        Intent LuxandIntent = new Intent(this, LuxandActivity.class);
        startActivityForResult(LuxandIntent, 519);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 519 && data != null && data.getIntExtra("RESULT", 0) == 1){
            //Face detected
            String faceTemplate = new LuxandInfo(this).getTemplate();
            sessionM.SET_REG_TEMPLATE(faceTemplate);
            startActivity(new Intent(LandingPage.this, AddTransporter.class));
        }else{
            //NO Face was captured.
            Toast.makeText(LandingPage.this, "Unable to capture facial template", Toast.LENGTH_LONG).show();
        }
    }
    public void importLocations(){
        if(!sessionM.getImportStatus()){
            @SuppressLint("StaticFieldLeak") PopulateLocation populateLocation = new PopulateLocation(LandingPage.this){
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
