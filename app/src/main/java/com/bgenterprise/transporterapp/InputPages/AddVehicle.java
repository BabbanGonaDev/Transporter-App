package com.bgenterprise.transporterapp.InputPages;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.bgenterprise.transporterapp.Database.Tables.Drivers;
import com.bgenterprise.transporterapp.Database.Tables.Vehicles;
import com.bgenterprise.transporterapp.Database.TransporterDatabase;
import com.bgenterprise.transporterapp.Main2Activity;
import com.bgenterprise.transporterapp.R;
import com.bgenterprise.transporterapp.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddVehicle extends AppCompatActivity {
    @BindView(R.id.btn_next) MaterialButton btn_next;
    @BindView(R.id.btn_another_vehicle) MaterialButton btn_another_vehicle;
    @BindView(R.id.btn_cancel) MaterialButton btn_cancel;
    @BindView(R.id.input_vehicle_type) TextInputLayout input_vehicle_type;
    @BindView(R.id.atv_vehicle_type) AutoCompleteTextView atv_vehicle_type;
    @BindView(R.id.input_vehicle_condition) TextInputLayout input_vehicle_condition;
    @BindView(R.id.atv_vehicle_condition) AutoCompleteTextView atv_vehicle_condition;
    @BindView(R.id.input_carry_livestock) TextInputLayout input_carry_livestock;
    @BindView(R.id.atv_carry_livestock) AutoCompleteTextView atv_carry_livestock;
    @BindView(R.id.input_capacity) TextInputLayout input_capacity;
    @BindView(R.id.atv_capacity) AutoCompleteTextView atv_capacity;
    @BindView(R.id.input_plate_number) TextInputLayout input_plate_number;
    @BindView(R.id.text_plate_number) TextInputEditText edit_plate_number;
    @BindView(R.id.isTransporterAddress) MaterialCheckBox chkboxIsTransporterAddress;
    @BindView(R.id.input_vehicle_state) TextInputLayout input_vehicle_state;
    @BindView(R.id.atv_vehicle_state) AutoCompleteTextView atv_vehicle_state;
    @BindView(R.id.input_vehicle_lga) TextInputLayout input_vehicle_lga;
    @BindView(R.id.atv_vehicle_lga) AutoCompleteTextView atv_vehicle_lga;
    @BindView(R.id.input_vehicle_ward) TextInputLayout input_vehicle_ward;
    @BindView(R.id.atv_vehicle_ward) AutoCompleteTextView atv_vehicle_ward;
    @BindView(R.id.input_vehicle_village) TextInputLayout input_vehicle_village;
    @BindView(R.id.edit_vehicle_village) TextInputEditText edit_vehicle_village;


    TransporterDatabase transportdb;
    SessionManager sessionM;
    HashMap<String, String> transport_details;
    int current_vehicle_no, total_vehicle_no;
    List<Vehicles> vehicles, temp;
    String vehicle_id, vehicle_owner_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        ButterKnife.bind(this);
        sessionM = new SessionManager(AddVehicle.this);
        transport_details = sessionM.getTransporterDetails();
        transportdb = TransporterDatabase.getInstance(AddVehicle.this);
        customizeActionBar();
        getNumberOfVehicles();
        //TODO ---> WHat's the format for vehicle id. (using staffId_datetime newly added 17-10-19) ?
        vehicle_id = transport_details.get(SessionManager.KEY_STAFF_ID) + "_" + new SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault()).format(new Date());
        vehicle_owner_id = transport_details.get(SessionManager.KEY_DRIVER_ID);
        disableButton(btn_next);
        initVehicleType();
        initLivestockDropdown();
        initVehicleCapacity();
        initVehicleCondition();
        initStateAdapter();

        atv_vehicle_state.setOnItemClickListener((adapterView, view, i, l) -> {
            atv_vehicle_lga.setText("");
            atv_vehicle_ward.setText("");
            initLGAdapter();
        });

        atv_vehicle_lga.setOnItemClickListener((adapterView, view, i, l) -> {
            atv_vehicle_ward.setText("");
            initWardsAdapter();
        });

    }

    @OnClick(R.id.btn_next)
    public void moveToNextActivity(){
        startActivity(new Intent(AddVehicle.this, AddOperatingAreas.class));
    }

    @OnClick(R.id.btn_another_vehicle)
    public void addAnother(){
        if(!checkEmptyInputs()){
            addNewVehicleToArrayList();
            if(current_vehicle_no <= total_vehicle_no){
                finish();
                startActivity(getIntent());
            }else{
                enableButton(btn_next);
                disableButton(btn_another_vehicle);
                clearAllInputs();
                displayFinishVehiclesRegDialog();
                Toast.makeText(AddVehicle.this, "You have finished all vehicles.", Toast.LENGTH_LONG).show();
            }
        }

    }

    @OnClick(R.id.btn_cancel)
    public void cancelAll(){
        new MaterialAlertDialogBuilder(AddVehicle.this)
                .setIcon(R.drawable.ic_exclamation_mark)
                .setTitle("Delete Registration ?")
                .setMessage("You are about to delete all registrations for this transporter. Do you want to go ahead ?")
                .setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sessionM.CLEAR_REGISTRATION_SESSION();
                        startActivity(new Intent(AddVehicle.this, Main2Activity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    @OnClick(R.id.isTransporterAddress)
    public void isTransporterAddressChkBox(MaterialCheckBox mChkBox){
        Gson gson = new Gson();
        List<Drivers> driver = gson.fromJson(transport_details.get(SessionManager.KEY_DRIVER_DETAILS), new TypeToken<List<Drivers>>(){}.getType());

        if(mChkBox.isChecked()){
            atv_vehicle_state.setText(driver.get(0).getDriver_state());
            atv_vehicle_lga.setText(driver.get(0).getDriver_lga());
            atv_vehicle_ward.setText(driver.get(0).getDriver_ward());
            edit_vehicle_village.setText(driver.get(0).getDriver_village());
        }else{
            //Empty inputs.
            atv_vehicle_state.setText("");
            atv_vehicle_lga.setText("");
            atv_vehicle_ward.setText("");
            edit_vehicle_village.setText("");
        }
    }

    public void customizeActionBar(){
        try {
            getSupportActionBar().setTitle("Add " + transport_details.get(SessionManager.KEY_VEHICLE_NO) +
                    " of " + transport_details.get(SessionManager.KEY_TOTAL_VEHICLE) +
                    " Vehicles");
        }catch (NullPointerException e){
            e.printStackTrace();
            Log.d("CHECK", "Unable to customize Actionbar");
        }

    }

    public boolean checkEmptyInputs(){
        if(atv_vehicle_type.getText().toString().equals("")){
            atv_vehicle_type.setError("This input is needed");
            return true;
        }
        if(atv_capacity.getText().toString().equals("")){
            atv_capacity.setError("This input is needed");
            return true;
        }
        if(atv_vehicle_lga.getText().toString().equals("")){
            atv_vehicle_lga.setError("This input is needed");
            return true;
        }
        if(atv_vehicle_ward.getText().toString().equals("")){
            atv_vehicle_ward.setError("This input is needed");
            return true;
        }
        if(edit_vehicle_village.getText().toString().equals("")){
            edit_vehicle_village.setError("This input is needed");
            return true;
        }
        return false;
    }

    public List<Vehicles> convertInputsToModel(){
        vehicles = new ArrayList<>();
        vehicles.add(new Vehicles(vehicle_id,
                edit_plate_number.getText().toString(),
                atv_vehicle_type.getText().toString(),
                "",
                "",
                atv_capacity.getText().toString(),
                atv_vehicle_state.getText().toString(),
                atv_vehicle_lga.getText().toString(),
                atv_vehicle_ward.getText().toString(),
                edit_vehicle_village.getText().toString(),
                vehicle_owner_id,
                "no"));
        return vehicles;
    }

    public void addNewVehicleToArrayList(){
        Gson gson = new Gson();
        Type vehicleType = new TypeToken<List<Vehicles>>(){}.getType();
        temp = new ArrayList<>();
        if(transport_details.get(SessionManager.KEY_VEHICLE_DETAILS) != ""){
            temp = gson.fromJson(transport_details.get(SessionManager.KEY_VEHICLE_DETAILS), vehicleType);
        }
        temp.addAll(convertInputsToModel());

        current_vehicle_no++;
        //Toast.makeText(getApplicationContext(), "New Number" + current_vehicle_no, Toast.LENGTH_LONG).show();
        sessionM.SET_VEHICLE_NO(String.valueOf(current_vehicle_no));
        sessionM.SET_VEHICLE_DETAILS(gson.toJson(temp));
    }

    public void enableButton(MaterialButton button){
        button.setEnabled(true);
    }

    public void disableButton(MaterialButton button){
        button.setEnabled(false);
    }

    public void clearAllInputs(){
        atv_vehicle_type.getText().clear();
        atv_vehicle_condition.getText().clear();
        atv_carry_livestock.getText().clear();
        atv_capacity.getText().clear();
        edit_plate_number.getText().clear();
        atv_vehicle_state.getText().clear();
        atv_vehicle_lga.getText().clear();
        atv_vehicle_ward.getText().clear();
        edit_vehicle_village.getText().clear();
    }

    public void getNumberOfVehicles(){
        try{
            current_vehicle_no = Integer.parseInt(transport_details.get(SessionManager.KEY_VEHICLE_NO));
            total_vehicle_no = Integer.parseInt(transport_details.get(SessionManager.KEY_TOTAL_VEHICLE));
        }catch(NumberFormatException e){
            e.printStackTrace();
        }
    }

    public void displayFinishVehiclesRegDialog(){
        //This function displays an alert informing the usere that they've successfully finished entering vehicles.
        new MaterialAlertDialogBuilder(AddVehicle.this)
                .setIcon(R.drawable.ic_success_checked)
                .setTitle("Success")
                .setCancelable(false)
                .setMessage("You've successfully registered transporter's vehicles. Click 'Next' to continue.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    public void initVehicleType(){
        String[] vehicleType = new String[]{"Big Truck", "Small Truck", "Motorcycle", "Bus", "Car"};

        ArrayAdapter<String> vehicleAdapter =
                new ArrayAdapter<>(AddVehicle.this,
                        R.layout.dropdown_menu_popup_item,
                        vehicleType);
        atv_vehicle_type.setAdapter(vehicleAdapter);
    }

    public void initLivestockDropdown(){
        String[] livestock = new String[]{"Yes", "No"};

        ArrayAdapter<String> livestockAdapter =
                new ArrayAdapter<>(AddVehicle.this,
                        R.layout.dropdown_menu_popup_item,
                        livestock);
        atv_carry_livestock.setAdapter(livestockAdapter);
    }

    public void initVehicleCapacity(){
        String[] vehicleCapacity = new String[]{"Less than 10 Bags", "10-20 Bags", "20-30 Bags", "30-40 Bags", "40-50 Bags", "More than 50 Bags"};

        ArrayAdapter<String> capacityAdapter =
                new ArrayAdapter<>(AddVehicle.this,
                        R.layout.dropdown_menu_popup_item,
                        vehicleCapacity);
        atv_capacity.setAdapter(capacityAdapter);
    }

    public void initVehicleCondition(){
        String[] condition = new String[]{"Very Good", "Good", "Fair", "Bad", "Very Bad"};

        ArrayAdapter<String> conditionAdapter =
                new ArrayAdapter<>(AddVehicle.this,
                        R.layout.dropdown_menu_popup_item,
                        condition);
        atv_vehicle_condition.setAdapter(conditionAdapter);
    }

    public void initStateAdapter(){

        @SuppressLint("StaticFieldLeak") getStates getS = new getStates(AddVehicle.this){
            @Override
            protected void onPostExecute(List<String> strings) {
                super.onPostExecute(strings);
                ArrayAdapter<String> stateAdapter =
                        new ArrayAdapter<>(getApplicationContext(),
                                R.layout.dropdown_menu_popup_item,
                                strings);
                atv_vehicle_state.setAdapter(stateAdapter);
            }
        };getS.execute();

    }

    public void initLGAdapter(){
        @SuppressLint("StaticFieldLeak") getLGAs getLga = new getLGAs(AddVehicle.this){
            @Override
            protected void onPostExecute(List<String> strings) {
                super.onPostExecute(strings);
                ArrayAdapter<String> lgaAdapter =
                        new ArrayAdapter<>(getApplicationContext(),
                                R.layout.dropdown_menu_popup_item,
                                strings);
                atv_vehicle_lga.setAdapter(lgaAdapter);
            }
        };getLga.execute(atv_vehicle_state.getText().toString());
    }

    public void initWardsAdapter(){
        @SuppressLint("StaticFieldLeak") getWards getWard = new getWards(AddVehicle.this){
            @Override
            protected void onPostExecute(List<String> strings) {
                super.onPostExecute(strings);
                ArrayAdapter<String> wardAdapter =
                        new ArrayAdapter<>(getApplicationContext(),
                                R.layout.dropdown_menu_popup_item,
                                strings);
                atv_vehicle_ward.setAdapter(wardAdapter);
            }
        } ;getWard.execute(atv_vehicle_lga.getText().toString());
    }

    @SuppressLint("StaticFieldLeak")
    public class getStates extends AsyncTask<Void, Void, List<String>> {
        Context context;

        public getStates(Context context) {
            this.context = context;
        }

        @Override
        protected List<String> doInBackground(Void... voids) {
            return transportdb.getLocationDao().getStates();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class getLGAs extends AsyncTask<String, Void, List<String>>{
        Context context;

        public getLGAs(Context context){
            this.context = context;
        }

        @Override
        protected List<String> doInBackground(String... params) {
            return transportdb.getLocationDao().getLGA(params[0]);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class getWards extends AsyncTask<String, Void, List<String>>{
        Context context;

        public getWards(Context context){
            this.context = context;
        }

        @Override
        protected List<String> doInBackground(String... params) {
            return transportdb.getLocationDao().getWard(params[0]);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
