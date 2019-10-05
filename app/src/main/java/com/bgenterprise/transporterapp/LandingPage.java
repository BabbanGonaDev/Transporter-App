package com.bgenterprise.transporterapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.babbangona.bg_face.LuxandActivity;
import com.babbangona.bg_face.LuxandInfo;
import com.bgenterprise.transporterapp.Database.DatabaseApiCalls;
import com.bgenterprise.transporterapp.Database.PopulateLocation;
import com.bgenterprise.transporterapp.Database.Tables.Drivers;
import com.bgenterprise.transporterapp.Database.Tables.OperatingAreas;
import com.bgenterprise.transporterapp.Database.Tables.Vehicles;
import com.bgenterprise.transporterapp.InputPages.AddTransporter;
import com.bgenterprise.transporterapp.Network.Responses.DriverResponse;
import com.bgenterprise.transporterapp.Network.Responses.DriverSync;
import com.bgenterprise.transporterapp.Network.Responses.OperatingAreaResponse;
import com.bgenterprise.transporterapp.Network.Responses.OperatingAreaSync;
import com.bgenterprise.transporterapp.Network.Responses.VehicleResponse;
import com.bgenterprise.transporterapp.Network.Responses.VehicleSync;
import com.bgenterprise.transporterapp.Network.RetrofitApiCalls;
import com.bgenterprise.transporterapp.Network.RetrofitClient;
import com.bgenterprise.transporterapp.RecyclerAdapters.ViewTransporterAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.opencsv.bean.CsvBindByName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LandingPage extends AppCompatActivity {
    @BindView(R.id.btn_add_transporter)
    MaterialButton btnAddTransporter;

    @BindView(R.id.rv_view_transporters)
    RecyclerView rv_view_transporters;

    @BindView(R.id.mtv_copyright)
    MaterialTextView mtv_copyright;

    SessionManager sessionM;
    HashMap<String, String> transport_details;
    ViewTransporterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        ButterKnife.bind(this);
        sessionM = new SessionManager(LandingPage.this);
        importLocations();
        sessionM.CLEAR_REGISTRATION_SESSION();
        transport_details = sessionM.getTransporterDetails();
        initDriverRecycler();
        mtv_copyright.setText("(C) Enterprise Systems 2019. Version: " + BuildConfig.VERSION_NAME);

        //TODO --> Check Phone date if it's earlier than date of app development.
    }

    @OnClick(R.id.btn_add_transporter)
    public void addTransport(){
        //startActivity(new Intent(LandingPage.this, AddTransporter.class));
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
                    Toast.makeText(LandingPage.this, "Location data successfully inserted", Toast.LENGTH_LONG).show();
                }
            };populateLocation.execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.landing_page_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.sync_table:
                executeDriverSyncFunctions();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void executeDriverSyncFunctions(){
        @SuppressLint("StaticFieldLeak") DatabaseApiCalls.getAllUnsyncedTransporters unsyncedDriversApi = new DatabaseApiCalls.getAllUnsyncedTransporters(LandingPage.this){
            @Override
            protected void onPostExecute(List<Drivers> drivers) {
                Gson gson = new Gson();
                RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
                //Log.d("CHECK", gson.toJson(drivers));
                Call<List<DriverResponse>> call = service.syncUpDrivers(gson.toJson(drivers));

                call.enqueue(new Callback<List<DriverResponse>>() {
                    @Override
                    public void onResponse(Call<List<DriverResponse>> call, Response<List<DriverResponse>> response) {
                        if(response.isSuccessful()){

                            List<DriverResponse> driverRes = response.body();
                            Log.d("CHECK", Objects.requireNonNull(driverRes).toString());
                            updateDriverSyncStatus(driverRes);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<DriverResponse>> call, Throwable t) {
                        Toast.makeText(LandingPage.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        };unsyncedDriversApi.execute();
    }

    public void executeOtherSyncFunctions(){
        @SuppressLint("StaticFieldLeak") DatabaseApiCalls.getAllUnsyncedVehicles unsyncedVehicles = new DatabaseApiCalls.getAllUnsyncedVehicles(LandingPage.this){
            @Override
            protected void onPostExecute(List<Vehicles> vehicles) {
                Gson gson = new Gson();
                RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
                Log.d("CHECK", "Vehicles " + gson.toJson(vehicles));
                Call<List<VehicleResponse>> call = service.syncUpVehicles(gson.toJson(vehicles));

                call.enqueue(new Callback<List<VehicleResponse>>() {
                    @Override
                    public void onResponse(Call<List<VehicleResponse>> call, Response<List<VehicleResponse>> response) {
                        if(response.isSuccessful()){

                            List<VehicleResponse> vehicleRes = response.body();
                            updateVehicleSyncStatus(vehicleRes);

                        }
                    }

                    @Override
                    public void onFailure(Call<List<VehicleResponse>> call, Throwable t) {
                        Toast.makeText(LandingPage.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        };unsyncedVehicles.execute();

        @SuppressLint("StaticFieldLeak") DatabaseApiCalls.getAllUnsyncedAreas unsyncedAreas = new DatabaseApiCalls.getAllUnsyncedAreas(LandingPage.this){
            @Override
            protected void onPostExecute(List<OperatingAreas> operatingAreas) {
                Gson gson = new Gson();
                RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
                Log.d("CHECK", "OpAreas " + gson.toJson(operatingAreas));
                Call<List<OperatingAreaResponse>> call = service.syncUpAreas(gson.toJson(operatingAreas));

                call.enqueue(new Callback<List<OperatingAreaResponse>>() {
                    @Override
                    public void onResponse(Call<List<OperatingAreaResponse>> call, Response<List<OperatingAreaResponse>> response) {
                        if(response.isSuccessful()){
                            List<OperatingAreaResponse> opAreaRes = response.body();
                            updateOpAreaSyncStatus(opAreaRes);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<OperatingAreaResponse>> call, Throwable t) {
                        Toast.makeText(LandingPage.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        };unsyncedAreas.execute();

        syncDownDrivers();
        syncDownVehicles();
        syncDownAreas();

    }

    public void updateDriverSyncStatus(List<DriverResponse> response){
        @SuppressLint("StaticFieldLeak") DatabaseApiCalls.updateTransporterSyncStatus updateStatus = new DatabaseApiCalls.updateTransporterSyncStatus(LandingPage.this){
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //Now after updating the new driver id everywhere, run the remainder two syncing table functions.
                executeOtherSyncFunctions();
            }
        };updateStatus.execute(response);
    }

    public void updateVehicleSyncStatus(List<VehicleResponse> response){
        @SuppressLint("StaticFieldLeak") DatabaseApiCalls.updateVehicleSyncStatus updateVehStatus = new DatabaseApiCalls.updateVehicleSyncStatus(LandingPage.this){
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        };updateVehStatus.execute(response);
    }

    public void updateOpAreaSyncStatus(List<OperatingAreaResponse> response){
        @SuppressLint("StaticFieldLeak") DatabaseApiCalls.updateOpAreaSyncStatus updateAreaStatus = new DatabaseApiCalls.updateOpAreaSyncStatus(LandingPage.this){
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        };updateAreaStatus.execute(response);
    }

    public void syncDownDrivers(){

        RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
        Call<List<DriverSync>> call = service.syncDownDrivers(transport_details.get(SessionManager.KEY_LAST_SYNC_DOWN_DRIVER));
        call.enqueue(new Callback<List<DriverSync>>() {
            @Override
            public void onResponse(Call<List<DriverSync>> call, Response<List<DriverSync>> response) {
                if(response.isSuccessful()){
                    List<DriverSync> first = response.body();
                    List<Drivers> driveResponse = new ArrayList<>();

                    for(DriverSync z: first){
                        driveResponse.add(new Drivers(z.getDriver_id(),
                                z.getFirst_name(),
                                z.getLast_name(),
                                z.getPhone_number(),
                                z.getNo_of_vehicles(),
                                z.getTraining_date(),
                                z.getDriver_state(),
                                z.getDriver_lga(),
                                z.getDriver_ward(),
                                z.getDriver_village(),
                                z.getManager_id(),
                                z.getTemplate(),
                                z.getReg_date(),
                                z.getSync_status()));
                        sessionM.SET_LAST_SYNC_DOWN_DRIVER(z.getLast_sync_time());
                    }

                    @SuppressLint("StaticFieldLeak") DatabaseApiCalls.insertIntoDriverTable insert = new DatabaseApiCalls.insertIntoDriverTable(LandingPage.this){
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            initDriverRecycler();
                        }
                    }; insert.execute(driveResponse);
                }
            }

            @Override
            public void onFailure(Call<List<DriverSync>> call, Throwable t) {
                Toast.makeText(LandingPage.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void syncDownVehicles(){

        RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
        Call<List<VehicleSync>> call = service.syncDownVehicles(transport_details.get(SessionManager.KEY_LAST_SYNC_DOWN_VEHICLE));
        call.enqueue(new Callback<List<VehicleSync>>() {
            @Override
            public void onResponse(Call<List<VehicleSync>> call, Response<List<VehicleSync>> response) {
                if(response.isSuccessful()){
                    List<VehicleSync> first = response.body();
                    List<Vehicles> vehicleResponse = new ArrayList<>();

                    for(VehicleSync y: first){
                        vehicleResponse.add(new Vehicles(y.getVehicle_id(),
                                y.getVehicle_plate_no(),
                                y.getVehicle_type(),
                                y.getVehicle_condition(),
                                y.getLivestock_status(),
                                y.getVehicle_capacity(),
                                y.getVehicle_state(),
                                y.getVehicle_lga(),
                                y.getVehicle_ward(),
                                y.getVehicle_village(),
                                y.getOwner_id(),
                                y.getSync_status()));
                        sessionM.SET_LAST_SYNC_DOWN_VEHICLE(SessionManager.KEY_LAST_SYNC_DOWN_VEHICLE);
                    }

                    @SuppressLint("StaticFieldLeak") DatabaseApiCalls.insertIntoVehicleTable insert = new DatabaseApiCalls.insertIntoVehicleTable(LandingPage.this){
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                        }
                    }; insert.execute(vehicleResponse);
                }
            }

            @Override
            public void onFailure(Call<List<VehicleSync>> call, Throwable t) {
                Toast.makeText(LandingPage.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void syncDownAreas(){

        RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
        Call<List<OperatingAreaSync>> call = service.syncDownOperatingAreas(transport_details.get(SessionManager.KEY_LAST_SYNC_DOWN_AREA));
        call.enqueue(new Callback<List<OperatingAreaSync>>() {
            @Override
            public void onResponse(Call<List<OperatingAreaSync>> call, Response<List<OperatingAreaSync>> response) {
                if(response.isSuccessful()){
                    List<OperatingAreaSync> first = response.body();
                    List<OperatingAreas> opAreasResponse = new ArrayList<>();

                    for(OperatingAreaSync x: first){
                        opAreasResponse.add(new OperatingAreas(x.getOwner_id(),
                                x.getState_id(),
                                x.getVillage_id(),
                                x.getWard_id(),
                                x.getLga_id(),
                                x.getSync_status()));
                        sessionM.SET_LAST_SYNC_DOWN_AREA(x.getLast_sync_time());
                    }

                    @SuppressLint("StaticFieldLeak") DatabaseApiCalls.insertIntoAreaTable insert = new DatabaseApiCalls.insertIntoAreaTable(LandingPage.this){
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                        }
                    }; insert.execute(opAreasResponse);
                }
            }

            @Override
            public void onFailure(Call<List<OperatingAreaSync>> call, Throwable t) {
                Toast.makeText(LandingPage.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void initDriverRecycler(){
        @SuppressLint("StaticFieldLeak") DatabaseApiCalls.getAllTransporters getDrivers = new DatabaseApiCalls.getAllTransporters(LandingPage.this){
            @Override
            protected void onPostExecute(List<Drivers> drivers) {
                super.onPostExecute(drivers);
                adapter = new ViewTransporterAdapter(LandingPage.this, drivers, new ViewTransporterAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(Drivers drivers) {

                    }
                });

                RecyclerView.LayoutManager vLayoutManager = new LinearLayoutManager(getApplicationContext());
                rv_view_transporters.setLayoutManager(vLayoutManager);
                rv_view_transporters.setItemAnimator(new DefaultItemAnimator());
                rv_view_transporters.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                rv_view_transporters.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        };getDrivers.execute();
    }


}
