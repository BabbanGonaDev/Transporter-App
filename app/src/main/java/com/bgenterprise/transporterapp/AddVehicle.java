package com.bgenterprise.transporterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.bgenterprise.transporterapp.Database.Tables.Vehicles;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
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

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddVehicle extends AppCompatActivity {
    @BindView(R.id.btn_next) MaterialButton btn_next;
    @BindView(R.id.btn_another_vehicle) MaterialButton btn_another_vehicle;
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
    @BindView(R.id.input_vehicle_lga) TextInputLayout input_vehicle_lga;
    @BindView(R.id.atv_vehicle_lga) AutoCompleteTextView atv_vehicle_lga;
    @BindView(R.id.input_vehicle_ward) TextInputLayout input_vehicle_ward;
    @BindView(R.id.atv_vehicle_ward) AutoCompleteTextView atv_vehicle_ward;
    @BindView(R.id.input_vehicle_village) TextInputLayout input_vehicle_village;
    @BindView(R.id.atv_vehicle_village) AutoCompleteTextView atv_vehicle_village;


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
        customizeActionBar();
        current_vehicle_no = Integer.parseInt(transport_details.get(SessionManager.KEY_VEHICLE_NO));
        total_vehicle_no = Integer.parseInt(transport_details.get(SessionManager.KEY_TOTAL_VEHICLE));
        vehicle_id = "XXSSRRRR" + new SimpleDateFormat("HHmmss", Locale.getDefault()).format(new Date());
        vehicle_owner_id = "CC";
        disableButton(btn_next);

    }

    @OnClick(R.id.btn_next)
    public void moveToNextActivity(){
        startActivity(new Intent(AddVehicle.this, AddOperatingAreas.class));
    }

    @OnClick(R.id.btn_another_vehicle)
    public void addAnother(){
        addNewVehicleToArrayList();
        if(current_vehicle_no <= total_vehicle_no){
            finish();
            startActivity(getIntent());
        }else{
            enableButton(btn_next);
            disableButton(btn_another_vehicle);
            Toast.makeText(AddVehicle.this, "You have finished all vehicles.", Toast.LENGTH_LONG).show();
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
        if(atv_vehicle_condition.getText().toString().equals("")){
            atv_vehicle_condition.setError("This input is needed");
            return true;
        }
        if(atv_carry_livestock.getText().toString().equals("")){
            atv_carry_livestock.setError("This input is needed");
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
        if(atv_vehicle_village.getText().toString().equals("")){
            atv_vehicle_village.setError("This input is needed");
            return true;
        }
        return false;
    }

    public List<Vehicles> convertInputsToModel(){
        vehicles = new ArrayList<>();
        vehicles.add(new Vehicles(vehicle_id,
                edit_plate_number.getText().toString(),
                atv_vehicle_type.getText().toString(),
                atv_vehicle_condition.getText().toString(),
                atv_carry_livestock.getText().toString(),
                atv_capacity.getText().toString(),
                atv_vehicle_lga.getText().toString(),
                atv_vehicle_ward.getText().toString(),
                atv_vehicle_village.getText().toString(),
                vehicle_owner_id));
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
        Toast.makeText(getApplicationContext(), "New Number" + current_vehicle_no, Toast.LENGTH_LONG).show();
        sessionM.SET_VEHICLE_NO(String.valueOf(current_vehicle_no));
        sessionM.SET_VEHICLE_DETAILS(gson.toJson(temp));
    }

    public void enableButton(MaterialButton button){
        button.setEnabled(true);
    }

    public void disableButton(MaterialButton button){
        button.setEnabled(false);
    }


}
