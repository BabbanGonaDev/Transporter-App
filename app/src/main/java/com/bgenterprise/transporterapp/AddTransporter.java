package com.bgenterprise.transporterapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SearchView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.bgenterprise.transporterapp.Database.Tables.Drivers;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddTransporter extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    @BindView(R.id.btn_next) MaterialButton btn_next;
    @BindView(R.id.input_first_name) TextInputLayout input_first_name;
    @BindView(R.id.edit_first_name) TextInputEditText edit_first_name;
    @BindView(R.id.input_last_name) TextInputLayout input_last_name;
    @BindView(R.id.edit_last_name) TextInputEditText edit_last_name;
    @BindView(R.id.input_phone_number) TextInputLayout input_phone_number;
    @BindView(R.id.edit_phone_number) TextInputEditText edit_phone_number;
    @BindView(R.id.input_no_of_vehicles) TextInputLayout input_no_of_vehicles;
    @BindView(R.id.edit_no_of_vehicles) TextInputEditText edit_no_of_vehicles;
    @BindView(R.id.input_lga) TextInputLayout input_lga;
    @BindView(R.id.edit_lga) AutoCompleteTextView edit_lga;
    @BindView(R.id.input_ward) TextInputLayout input_ward;
    @BindView(R.id.edit_ward) AutoCompleteTextView edit_ward;
    @BindView(R.id.input_village) TextInputLayout input_village;
    @BindView(R.id.edit_village) AutoCompleteTextView edit_village;
    @BindView(R.id.isDriver) MaterialCheckBox chkIsDriver;
    @BindView(R.id.btn_select_training_date) MaterialButton btn_select_training_date;
    @BindView(R.id.mtv_select_training_date) MaterialTextView mtv_select_training_date;
    @BindView(R.id.manager_layout) LinearLayoutCompat manager_layout;
    @BindView(R.id.manager_search) SearchView manager_search;

    DatePickerDialog datePickerDialog ;
    int Year, Month, Day;
    Calendar calendar;
    List<Drivers> drivers;
    SessionManager sessionM;
    String driver_id, driver_template, manager_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transporter);
        ButterKnife.bind(this);
        customizeActionBar();
        manager_layout.setVisibility(View.GONE);
        sessionM = new SessionManager(AddTransporter.this);
        driver_id = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        driver_template = "AAAAAxxxxxxeefwdqwwqewq";

        //TODO ---> Check whether the manager_id variable is empty, especially if the person acknowledges working for a transporter.
    }

    @OnClick(R.id.btn_select_training_date)
    public void training_date_picker(){
        datePickerDialog = DatePickerDialog.newInstance(AddTransporter.this, Year, Month, Day);
        datePickerDialog.setThemeDark(false);
        datePickerDialog.showYearPickerFirst(false);
        datePickerDialog.setMinDate(Calendar.getInstance());
        datePickerDialog.setTitle("Select Training Date");

        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

            }
        });

        datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mtv_select_training_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
    }

    @OnClick(R.id.btn_next)
    public void next_to_vehicle(){
        if(!checkEmptyInputs()){
            if(!chkIsDriver.isChecked()){
                /**Set manager_id as the driver_id if the driver is an independent.*/
                manager_id = driver_id;
            }

            try {
                /**Get the entered values into the model class.*/

                drivers = new ArrayList<>();
                drivers.add(new Drivers(driver_id,
                        edit_first_name.getText().toString(),
                        edit_last_name.getText().toString(),
                        edit_phone_number.getText().toString(),
                        edit_no_of_vehicles.getText().toString(),
                        mtv_select_training_date.getText().toString(),
                        edit_lga.getText().toString(),
                        edit_ward.getText().toString(),
                        edit_village.getText().toString(),
                        manager_id,
                        driver_template,
                        new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date())));

                //Convert model class to gson and store in shared preferences.
                Gson gson = new Gson();
                sessionM.SET_DRIVER_DETAILS(gson.toJson(drivers));
                sessionM.SET_DRIVER_ID(driver_id);
                sessionM.SET_DRIVER_TEMPLATE(driver_template);
                sessionM.SET_VEHICLE_NO("1");
                sessionM.SET_TOTAL_VEHICLE(edit_no_of_vehicles.getText().toString());
                redirectToNextPage();

            }catch (NullPointerException e){
                e.printStackTrace();
                Log.d("CHECK", "Unable to create object list");
            }

        }

    }

    @OnClick(R.id.isDriver)
    public void isDriverChkBox(MaterialCheckBox mCheckBox){
        if(mCheckBox.isChecked()){
            manager_layout.setVisibility(View.VISIBLE);
            edit_no_of_vehicles.setText("");
        }else{
            manager_layout.setVisibility(View.GONE);
        }
    }

    public void redirectToNextPage(){
        if(chkIsDriver.isChecked()){
            /** Redirect straight to Operating areas if the transporter is just a driver.*/

            startActivity(new Intent(AddTransporter.this, AddOperatingAreas.class));
        }else if(!chkIsDriver.isChecked()){
            /** Redirect to vehicles if the transporter is independent or manager.*/

            startActivity(new Intent(AddTransporter.this, AddVehicle.class));
        }else{
            //Do Nothing.
        }
    }

    public boolean checkEmptyInputs(){
        if(edit_first_name.getText().toString().equals("")){
            edit_first_name.setError("This input is needed");
            return true;
        }
        if(edit_last_name.getText().toString().equals("")){
            edit_last_name.setError("This input is needed");
            return true;
        }
        if(edit_phone_number.getText().toString().trim().length() != 11){
            edit_phone_number.setError("Phone Number must be 11 digits.");
            return true;
        }
        if(edit_lga.getText().toString().equals("")){
            edit_lga.setError("This input is needed");
            return true;
        }
        if(edit_ward.getText().toString().equals("")){
            edit_ward.setError("This input is needed");
            return true;
        }
        if(edit_no_of_vehicles.getText().toString().equals("") && !chkIsDriver.isChecked()){
            edit_no_of_vehicles.setError("This input is needed");
            return true;
        }
        if(mtv_select_training_date.getText().toString().equals("")){
            mtv_select_training_date.setError("Kindly Select a Training Date");
            return true;
        }
        if(edit_village.getText().toString().equals("")){
            edit_village.setError("This input is needed");
            return true;
        }
        return false;
    }

    public void customizeActionBar(){
        try {
            getSupportActionBar().setTitle("Register New Transporter");
        }catch (NullPointerException e){
            e.printStackTrace();
            Log.d("CHECK", "Unable to customize Actionbar");
        }
    }
}
