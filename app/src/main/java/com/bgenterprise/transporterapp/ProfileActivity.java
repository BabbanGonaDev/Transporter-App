package com.bgenterprise.transporterapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bgenterprise.transporterapp.Database.Tables.Drivers;
import com.bgenterprise.transporterapp.Database.Tables.OperatingAreas;
import com.bgenterprise.transporterapp.Database.Tables.Vehicles;
import com.bgenterprise.transporterapp.Database.TransporterDatabase;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

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

    @BindView(R.id.mtv_vehicles)
    MaterialTextView mtv_vehicles;

    @BindView(R.id.mtv_areas)
    MaterialTextView mtv_areas;

    String driver_id;
    TransporterDatabase transportdb;
    Drivers driver;
    List<OperatingAreas> driverAreas;
    List<Vehicles> driverVehicles;

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
        getVehiclesDetails(driver_id);
        getAreaDetails(driver_id);
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
            mtv_vehicles.setText("");
            mtv_areas.setText("");

            int i = 1;
            for (Vehicles vehicles: driverVehicles) {
                mtv_vehicles.append(i + ". Type: " + vehicles.getVehicle_type() + "\n" +
                        "    Plate No: " + vehicles.getVehicle_plate_no() + "\n" +
                        "    Capacity: " + vehicles.getVehicle_capacity() + "\n" + "\n");
                i++;

            }

            int j = 1;
            for (OperatingAreas opArea: driverAreas) {
                if (opArea.getLga_id().equals("") && opArea.getWard_id().equals("") && opArea.getVillage_id().equals("")) {
                    mtv_areas.append(j + ". " + opArea.getState_id() + "\n");
                } else if (opArea.getWard_id().equals("") && opArea.getVillage_id().equals("")) {
                    mtv_areas.append(j + ". " + opArea.getState_id() + ", " + opArea.getLga_id() + "\n");
                } else if (opArea.getVillage_id().equals("")) {
                    mtv_areas.append(j + ". " + opArea.getState_id() + ", " + opArea.getLga_id() + ", " + opArea.getWard_id() + "\n");
                } else {
                    mtv_areas.append(j + ". " + opArea.getState_id() + ", " + opArea.getLga_id() + ", " + opArea.getWard_id() + ", " + opArea.getVillage_id() + "\n");
                }
                j++;
            }


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

    public void getVehiclesDetails(String driverID){

        AppExecutors.getInstance().diskIO().execute(() -> {
            try{
                driverVehicles = transportdb.getVehicleDao().getDriversVehicles(driverID);
                runOnUiThread(() -> initUIDisplay());
            }catch (Exception e){
                runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Unable to get vehicle details", Toast.LENGTH_LONG).show());
            }
        });

    }

    public void getAreaDetails(String driverID){

        AppExecutors.getInstance().diskIO().execute(() -> {
            try{
                driverAreas = transportdb.getOperatingAreaDao().getDriversAreas(driverID);
                runOnUiThread(() -> initUIDisplay());
            }catch (Exception e){
                runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Unable to get operating areas details", Toast.LENGTH_LONG).show());
            }
        });
    }
}
