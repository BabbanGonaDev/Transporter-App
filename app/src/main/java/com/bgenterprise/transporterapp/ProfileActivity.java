package com.bgenterprise.transporterapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bgenterprise.transporterapp.Database.Tables.Drivers;
import com.bgenterprise.transporterapp.Database.Tables.OperatingAreas;
import com.bgenterprise.transporterapp.Database.Tables.Vehicles;
import com.bgenterprise.transporterapp.Database.TransporterDatabase;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textview.MaterialTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar xToolbar;

    @BindView(R.id.appbar)
    AppBarLayout appbar;

    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.mtv_driver_name)
    MaterialTextView mtv_driver_name;

    @BindView(R.id.mtv_driver_id)
    MaterialTextView mtv_driver_id;

    @BindView(R.id.mtv_driver_phone)
    MaterialTextView mtv_driver_phone;

    @BindView(R.id.mtv_driver_vehicle)
    MaterialTextView mtv_driver_vehicles;

    @BindView(R.id.mtv_driver_state)
    MaterialTextView mtv_driver_state;

    @BindView(R.id.mtv_driver_lga)
    MaterialTextView mtv_driver_lga;

    @BindView(R.id.mtv_driver_ward)
    MaterialTextView mtv_driver_ward;

    @BindView(R.id.tabs_layout)
    TabLayout tabs_layout;

    String driver_id;
    TransporterDatabase transportdb;
    Drivers driver;
    OperatingAreas driverAreas;
    Vehicles driverVehicles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        transportdb = TransporterDatabase.getInstance(ProfileActivity.this);

        try{
            driver_id = getIntent().getStringExtra("driver_id");
        }catch(Exception e){
            e.printStackTrace();
            startActivity(new Intent(this, Main2Activity.class));
        }

        collapsingToolbarTitle();
        setSupportActionBar(xToolbar);
        getDriverDetails(driver_id);
        initUIDisplay();

    }

    public void collapsingToolbarTitle(){
        //Use this function to display the title in the collapsing toolbar, ONLY when the toolbar has been collapsed.
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(driver_id);
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbar.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
    }

    public void initUIDisplay(){
        try{
            mtv_driver_id.setText("Driver ID: " + driver.getDriver_id());
            mtv_driver_name.setText("Driver Name: " + driver.getFirst_name() + " " + driver.getLast_name());
            mtv_driver_phone.setText(driver.getPhone_number());
            mtv_driver_vehicles.setText(driver.getNo_of_vehicles() + " Vehicle(s)");
            mtv_driver_state.setText("State: " + driver.getDriver_state());
            mtv_driver_lga.setText("LGA: " + driver.getDriver_lga());
            mtv_driver_ward.setText("Ward: " + driver.getDriver_ward());


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getDriverDetails(String driverID){

        AppExecutors.getInstance().diskIO().execute(() -> {
            try{
                driver = transportdb.getDriverDao().getDriverDetails(driverID);
                runOnUiThread(() -> initUIDisplay());
            }catch (Exception e){
                runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Unable to get driver details", Toast.LENGTH_LONG).show());
            }

        });

    }
}
