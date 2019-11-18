package com.bgenterprise.transporterapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.babbangona.bg_face.LuxandActivity;
import com.babbangona.bg_face.LuxandInfo;
import com.bgenterprise.transporterapp.Database.DatabaseApiCalls;
import com.bgenterprise.transporterapp.Database.PopulateLocation;
import com.bgenterprise.transporterapp.Database.Tables.Drivers;
import com.bgenterprise.transporterapp.Database.Tables.HSF;
import com.bgenterprise.transporterapp.Database.Tables.OperatingAreas;
import com.bgenterprise.transporterapp.Database.Tables.Payments;
import com.bgenterprise.transporterapp.Database.Tables.Vehicles;
import com.bgenterprise.transporterapp.Database.TransporterDatabase;
import com.bgenterprise.transporterapp.InputPages.AddTransporter;
import com.bgenterprise.transporterapp.Network.ModelClasses.DriverResponse;
import com.bgenterprise.transporterapp.Network.ModelClasses.DriverSyncDown;
import com.bgenterprise.transporterapp.Network.ModelClasses.HSFSyncDown;
import com.bgenterprise.transporterapp.Network.ModelClasses.OperatingAreaResponse;
import com.bgenterprise.transporterapp.Network.ModelClasses.OperatingAreaSyncDown;
import com.bgenterprise.transporterapp.Network.ModelClasses.PaymentSyncDown;
import com.bgenterprise.transporterapp.Network.ModelClasses.VehicleResponse;
import com.bgenterprise.transporterapp.Network.ModelClasses.VehicleSyncDown;
import com.bgenterprise.transporterapp.Network.RetrofitApiCalls;
import com.bgenterprise.transporterapp.Network.RetrofitClient;
import com.bgenterprise.transporterapp.Onboarding.OnBoardingActivity;
import com.bgenterprise.transporterapp.RecyclerAdapters.ViewTransporterAdapter;
import com.bgenterprise.transporterapp.TransporterDetails.ProfileActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main2Activity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    @BindView(R.id.btn_add_transporter)
    MaterialButton btnAddTransporter;

    @BindView(R.id.rv_view_transporters)
    RecyclerView rv_view_transporters;

    @BindView(R.id.mtv_copyright)
    MaterialTextView mtv_copyright;

    @BindView(R.id.sv_transporters)
    SearchView sv_transporters;

    @BindView(R.id.shimmer_layout)
    ShimmerFrameLayout shimmer_layout;

    String[] appPermissions = {
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_NETWORK_STATE
    };

    SessionManager sessionM;
    HashMap<String, String> transport_details;
    ViewTransporterAdapter adapter;
    String staff_id;
    ProgressDialog pdSync;
    TransporterDatabase transportdb;
    private static final int PERMISSIONS_REQUEST_CODE = 1240;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        ButterKnife.bind(this);
        transportdb = TransporterDatabase.getInstance(Main2Activity.this);
        sessionM = new SessionManager(Main2Activity.this);
        pdSync = new ProgressDialog(Main2Activity.this);
        pdSync.setTitle("Loading");
        pdSync.setMessage("Synchronizing local database");
        pdSync.setCancelable(true);

        try {
            Intent intent = getIntent();
            Bundle b = intent.getExtras();
            staff_id = (String) b.get("staff_id");
            sessionM.SET_STAFF_ID(staff_id);
        } catch (Exception e) {
            Log.d("HERE", "" + staff_id);
        }

        importLocations();
        checkAndRequestPermissions();
        sessionM.CLEAR_REGISTRATION_SESSION();
        transport_details = sessionM.getTransporterDetails();
        initDriverRecycler();
        mtv_copyright.setText("© Transporter App v" + BuildConfig.VERSION_NAME);
        //confirmPhoneDate();
        sv_transporters.setOnQueryTextListener(this);
        redirectToOnboard();

    }

    @OnClick(R.id.btn_add_transporter)
    public void addTransport(){
        new MaterialAlertDialogBuilder(Main2Activity.this)
                .setTitle("Facial Recognition")
                .setMessage("Do you want to take the facial template of this driver ?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    sessionM.SET_FACIAL_REG_CHOICE(true);
                    Intent LuxandIntent = new Intent(Main2Activity.this, LuxandActivity.class);
                    startActivityForResult(LuxandIntent, 519);

                })
                .setNegativeButton("No", (dialogInterface, i) -> {
                    sessionM.SET_FACIAL_REG_CHOICE(false);
                    startActivity(new Intent(Main2Activity.this, AddTransporter.class));
                })
                .show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 519 && data != null && data.getIntExtra("RESULT", 0) == 1){
            //Face detected
            String faceTemplate = new LuxandInfo(this).getTemplate();
            sessionM.SET_REG_TEMPLATE(faceTemplate);
            startActivity(new Intent(Main2Activity.this, AddTransporter.class));
        }else{
            //NO Face was captured.
            Toast.makeText(Main2Activity.this, "Unable to capture facial template", Toast.LENGTH_LONG).show();
        }
    }

    public void importLocations(){
        if(!sessionM.getImportStatus()){
            @SuppressLint("StaticFieldLeak") PopulateLocation populateLocation = new PopulateLocation(Main2Activity.this){
                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    sessionM.SET_IMPORT_LOCATION(true);
                    Toast.makeText(Main2Activity.this, "Location data successfully inserted", Toast.LENGTH_LONG).show();
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
            case R.id.app_info:
                showAppInfo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void executeDriverSyncFunctions(){
        pdSync.show();
        @SuppressLint("StaticFieldLeak") DatabaseApiCalls.getAllUnsyncedTransporters unsyncedDriversApi = new DatabaseApiCalls.getAllUnsyncedTransporters(Main2Activity.this){
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
                        Toast.makeText(Main2Activity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        };unsyncedDriversApi.execute();
    }

    public void executeOtherSyncFunctions(){
        @SuppressLint("StaticFieldLeak") DatabaseApiCalls.getAllUnsyncedVehicles unsyncedVehicles = new DatabaseApiCalls.getAllUnsyncedVehicles(Main2Activity.this){
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
                        Toast.makeText(Main2Activity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        };unsyncedVehicles.execute();

        @SuppressLint("StaticFieldLeak") DatabaseApiCalls.getAllUnsyncedAreas unsyncedAreas = new DatabaseApiCalls.getAllUnsyncedAreas(Main2Activity.this){
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
                        Toast.makeText(Main2Activity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        };unsyncedAreas.execute();

        syncDownDrivers();
        syncDownVehicles();
        syncDownAreas();
        syncDownHSF();
        syncDownPayments();
        pdSync.dismiss();
    }

    public void updateDriverSyncStatus(List<DriverResponse> response){
        @SuppressLint("StaticFieldLeak") DatabaseApiCalls.updateTransporterSyncStatus updateStatus = new DatabaseApiCalls.updateTransporterSyncStatus(Main2Activity.this){
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //Now after updating the new driver id everywhere, run the remainder two syncing table functions.
                executeOtherSyncFunctions();
            }
        };updateStatus.execute(response);
    }

    public void updateVehicleSyncStatus(List<VehicleResponse> response){
        @SuppressLint("StaticFieldLeak") DatabaseApiCalls.updateVehicleSyncStatus updateVehStatus = new DatabaseApiCalls.updateVehicleSyncStatus(Main2Activity.this){
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        };updateVehStatus.execute(response);
    }

    public void updateOpAreaSyncStatus(List<OperatingAreaResponse> response){
        @SuppressLint("StaticFieldLeak") DatabaseApiCalls.updateOpAreaSyncStatus updateAreaStatus = new DatabaseApiCalls.updateOpAreaSyncStatus(Main2Activity.this){
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        };updateAreaStatus.execute(response);
    }

    public void syncDownDrivers(){

        RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
        Call<List<DriverSyncDown>> call = service.syncDownDrivers(transport_details.get(SessionManager.KEY_LAST_SYNC_DOWN_DRIVER));
        call.enqueue(new Callback<List<DriverSyncDown>>() {
            @Override
            public void onResponse(Call<List<DriverSyncDown>> call, Response<List<DriverSyncDown>> response) {
                if(response.isSuccessful()){
                    List<DriverSyncDown> first = response.body();
                    List<Drivers> driveResponse = new ArrayList<>();

                    for(DriverSyncDown z: first){
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
                                z.getPayment_option(),
                                z.getBg_card(),
                                z.getAccount_number(),
                                z.getAccount_name(),
                                z.getBank_name(),
                                z.getManager_id(),
                                z.getTemplate(),
                                z.getReg_date(),
                                z.getStaff_id(),
                                z.getSync_status()));
                        sessionM.SET_LAST_SYNC_DOWN_DRIVER(z.getLast_sync_time());
                    }

                    @SuppressLint("StaticFieldLeak") DatabaseApiCalls.insertIntoDriverTable insert = new DatabaseApiCalls.insertIntoDriverTable(Main2Activity.this){
                        @Override
                        protected void onPreExecute() {
                            //Initialize the shimmer
                            super.onPreExecute();
                            rv_view_transporters.setVisibility(View.GONE);
                            shimmer_layout.setVisibility(View.VISIBLE);
                            shimmer_layout.startShimmer();
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            //End the Shimmer
                            super.onPostExecute(aVoid);
                            shimmer_layout.stopShimmer();
                            shimmer_layout.setVisibility(View.GONE);
                            rv_view_transporters.setVisibility(View.VISIBLE);
                            initDriverRecycler();
                        }
                    }; insert.execute(driveResponse);
                }
            }

            @Override
            public void onFailure(Call<List<DriverSyncDown>> call, Throwable t) {
                Toast.makeText(Main2Activity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void syncDownVehicles(){

        RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
        Call<List<VehicleSyncDown>> call = service.syncDownVehicles(transport_details.get(SessionManager.KEY_LAST_SYNC_DOWN_VEHICLE));
        call.enqueue(new Callback<List<VehicleSyncDown>>() {
            @Override
            public void onResponse(Call<List<VehicleSyncDown>> call, Response<List<VehicleSyncDown>> response) {
                if(response.isSuccessful()){
                    List<VehicleSyncDown> first = response.body();
                    List<Vehicles> vehicleResponse = new ArrayList<>();

                    for(VehicleSyncDown y: first){
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
                        sessionM.SET_LAST_SYNC_DOWN_VEHICLE(y.getLast_sync_time());
                    }

                    @SuppressLint("StaticFieldLeak") DatabaseApiCalls.insertIntoVehicleTable insert = new DatabaseApiCalls.insertIntoVehicleTable(Main2Activity.this){
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                        }
                    }; insert.execute(vehicleResponse);
                }
            }

            @Override
            public void onFailure(Call<List<VehicleSyncDown>> call, Throwable t) {
                Toast.makeText(Main2Activity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void syncDownAreas(){

        RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
        Call<List<OperatingAreaSyncDown>> call = service.syncDownOperatingAreas(transport_details.get(SessionManager.KEY_LAST_SYNC_DOWN_AREA));
        call.enqueue(new Callback<List<OperatingAreaSyncDown>>() {
            @Override
            public void onResponse(Call<List<OperatingAreaSyncDown>> call, Response<List<OperatingAreaSyncDown>> response) {
                if(response.isSuccessful()){
                    List<OperatingAreaSyncDown> first = response.body();
                    List<OperatingAreas> opAreasResponse = new ArrayList<>();

                    for(OperatingAreaSyncDown x: first){
                        opAreasResponse.add(new OperatingAreas(x.getOwner_id(),
                                x.getState_id(),
                                x.getVillage_id(),
                                x.getWard_id(),
                                x.getLga_id(),
                                x.getSync_status()));
                        sessionM.SET_LAST_SYNC_DOWN_AREA(x.getLast_sync_time());
                    }

                    @SuppressLint("StaticFieldLeak") DatabaseApiCalls.insertIntoAreaTable insert = new DatabaseApiCalls.insertIntoAreaTable(Main2Activity.this){
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                        }
                    }; insert.execute(opAreasResponse);
                }
            }

            @Override
            public void onFailure(Call<List<OperatingAreaSyncDown>> call, Throwable t) {
                Toast.makeText(Main2Activity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void syncDownHSF(){
        //TODO --> Remember to add php function to check that there are no alphabets or symbols in the payment amount columns.
        RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
        Call<List<HSFSyncDown>> call = service.syncDownHSF(transport_details.get(SessionManager.KEY_LAST_SYNC_DOWN_HSF));
        call.enqueue(new Callback<List<HSFSyncDown>>() {
            @Override
            public void onResponse(Call<List<HSFSyncDown>> call, Response<List<HSFSyncDown>> response) {
                if(response.isSuccessful()){
                    List<HSFSyncDown> another = response.body();
                    List<HSF> hsfResponse = new ArrayList<>();

                    for(HSFSyncDown h: another){
                        hsfResponse.add(new HSF(h.getHsf_id(),
                                h.getField_id(),
                                h.getBags_transported(),
                                h.getPrice_per_bag(),
                                h.getTransporter_id(),
                                h.getCc_id(),
                                h.getDate_processed()));
                        sessionM.SET_LAST_SYNC_DOWN_HSF(h.getLast_sync_time());
                    }

                    AppExecutors.getInstance().diskIO().execute(() -> {
                        transportdb.getHsfDao().InsertHSFList(hsfResponse);
                    });
                }
            }

            @Override
            public void onFailure(Call<List<HSFSyncDown>> call, Throwable t) {
                Toast.makeText(Main2Activity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void syncDownPayments(){

        RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
        Call<List<PaymentSyncDown>> call = service.syncDownPayments(transport_details.get(SessionManager.KEY_LAST_SYNC_DOWN_PAYMENT));
        call.enqueue(new Callback<List<PaymentSyncDown>>() {
            @Override
            public void onResponse(Call<List<PaymentSyncDown>> call, Response<List<PaymentSyncDown>> response) {
                if(response.isSuccessful()){
                    List<PaymentSyncDown> pay = response.body();
                    List<Payments> payResponse = new ArrayList<>();

                    for(PaymentSyncDown p: pay){
                        payResponse.add(new Payments(p.getPayment_id(),
                                p.getTransporter_id(),
                                p.getAmount_paid(),
                                p.getMode_of_payment(),
                                p.getPayment_date()));
                        sessionM.SET_LAST_SYNC_DOWN_PAYMENT(p.getLast_sync_time());
                    }

                    AppExecutors.getInstance().diskIO().execute(() -> {
                        transportdb.getPaymentsDao().InsertPaymentsList(payResponse);
                    });
                }
            }

            @Override
            public void onFailure(Call<List<PaymentSyncDown>> call, Throwable t) {
                Toast.makeText(Main2Activity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void initDriverRecycler(){
        @SuppressLint("StaticFieldLeak") DatabaseApiCalls.getAllTransporters getDrivers = new DatabaseApiCalls.getAllTransporters(Main2Activity.this){
            @Override
            protected void onPostExecute(List<Drivers> drivers) {
                super.onPostExecute(drivers);
                adapter = new ViewTransporterAdapter(Main2Activity.this, drivers, drivers1 -> {
                    sessionM.SET_TRANSPORTER_ID(drivers1.getDriver_id());
                    startActivity(new Intent(Main2Activity.this, ProfileActivity.class));
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

    public boolean checkAndRequestPermissions(){

        //Check which permissions are granted
        List<String> listPermissionsNeeded = new ArrayList<>();
        for(String perm : appPermissions){
            if(ContextCompat.checkSelfPermission(Main2Activity.this, perm) != PackageManager.PERMISSION_GRANTED){
                listPermissionsNeeded.add(perm);
            }
        }

        //Ask for non-granted permissions
        if(!listPermissionsNeeded.isEmpty()){
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    PERMISSIONS_REQUEST_CODE);
            return false;
        }

        //All permissions granted.
        return true;
    }

    public void confirmPhoneDate(){
        //TODO --> Check Phone date if it's earlier than date of app development.
        Calendar today = Calendar.getInstance();
        Calendar devDate = Calendar.getInstance();
        devDate.set(2019, 10, 20);
        if(!devDate.before(today)){
            new MaterialAlertDialogBuilder(this)
                    .setCancelable(false)
                    .setTitle("Incorrect Phone Date")
                    .setIcon(R.drawable.ic_exclamation_mark)
                    .setMessage("Kindly adjust phone's date to use the Transporter App")
                    .setPositiveButton("Okay", (dialogInterface, i) -> {
                        startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
                    }).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //confirmPhoneDate();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        try{
            adapter.getFilter().filter(newText.toLowerCase());
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public void redirectToOnboard(){
        //The purpose of this function is to redirect to the onboarding activity for first time users.
        if(!sessionM.getOnboardStatus()){
            startActivity(new Intent(Main2Activity.this, OnBoardingActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void showAppInfo(){
        transport_details = sessionM.getTransporterDetails();
        //Display all sync times and any other important info about the Transporter App in an alert dialog.
        LinearLayoutCompat infoLayout = new LinearLayoutCompat(Main2Activity.this);
        infoLayout.setOrientation(LinearLayoutCompat.VERTICAL);

        int paddingDp = 20;
        float density = getResources().getDisplayMetrics().density;
        int paddingPixel = (int)(paddingDp * density);

        final MaterialTextView mtvHeadline = new MaterialTextView(Main2Activity.this);
        mtvHeadline.setText("Last sync times");
        mtvHeadline.setPadding(paddingPixel, 0, 0, 15);
        mtvHeadline.setTypeface(null, Typeface.BOLD);
        infoLayout.addView(mtvHeadline);

        final MaterialTextView mtvDriverT = new MaterialTextView(Main2Activity.this);
        mtvDriverT.setText("Drivers: " + transport_details.get(SessionManager.KEY_LAST_SYNC_DOWN_DRIVER));
        mtvDriverT.setPadding(paddingPixel, 0, 0, 5);
        mtvDriverT.setTypeface(null, Typeface.ITALIC);
        infoLayout.addView(mtvDriverT);

        final MaterialTextView mtvVehicleT = new MaterialTextView(Main2Activity.this);
        mtvVehicleT.setText("Vehicles: " + transport_details.get(SessionManager.KEY_LAST_SYNC_DOWN_VEHICLE));
        mtvVehicleT.setPadding(paddingPixel, 0, 0, 5);
        mtvVehicleT.setTypeface(null, Typeface.ITALIC);
        infoLayout.addView(mtvVehicleT);

        final MaterialTextView mtvAreaT = new MaterialTextView(Main2Activity.this);
        mtvAreaT.setText("Operating Areas: " + transport_details.get(SessionManager.KEY_LAST_SYNC_DOWN_AREA));
        mtvAreaT.setPadding(paddingPixel, 0, 0, 5);
        mtvAreaT.setTypeface(null, Typeface.ITALIC);
        infoLayout.addView(mtvAreaT);

        final MaterialTextView mtvPaymentT = new MaterialTextView(Main2Activity.this);
        mtvPaymentT.setText("Payments: " + transport_details.get(SessionManager.KEY_LAST_SYNC_DOWN_PAYMENT));
        mtvPaymentT.setPadding(paddingPixel, 0, 0, 5);
        mtvPaymentT.setTypeface(null, Typeface.ITALIC);
        infoLayout.addView(mtvPaymentT);

        final MaterialTextView mtvHSFT = new MaterialTextView(Main2Activity.this);
        mtvHSFT.setText("HSF: " + transport_details.get(SessionManager.KEY_LAST_SYNC_DOWN_HSF));
        mtvHSFT.setPadding(paddingPixel, 0, 0, 5);
        mtvHSFT.setTypeface(null, Typeface.ITALIC);
        infoLayout.addView(mtvHSFT);

        final MaterialTextView mtvVersion = new MaterialTextView(Main2Activity.this);
        mtvVersion.setText("App Version: " + BuildConfig.VERSION_NAME);
        mtvVersion.setPadding(paddingPixel, 30, 0, 5);
        mtvVersion.setTypeface(null, Typeface.BOLD_ITALIC);
        infoLayout.addView(mtvVersion);

        new MaterialAlertDialogBuilder(Main2Activity.this)
                .setIcon(R.drawable.ic_info_outline_bgblue_24dp)
                .setTitle("App Information")
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    //Do Nothing
                })
                .setView(infoLayout)
                .show();
    }

}
