package com.bgenterprise.transporterapp.InputPages;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.babbangona.bg_face.LuxandActivity;
import com.babbangona.bg_face.LuxandInfo;
import com.bgenterprise.transporterapp.Database.Tables.Drivers;
import com.bgenterprise.transporterapp.Database.Tables.OperatingAreas;
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
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddOperatingAreas extends AppCompatActivity {

@BindView(R.id.edit_operating_state) AutoCompleteTextView edit_operating_state;
@BindView(R.id.edit_operating_lga) AutoCompleteTextView edit_operating_lga;
@BindView(R.id.edit_operating_village) TextInputEditText edit_operating_village;
@BindView(R.id.edit_operating_ward) AutoCompleteTextView edit_operating_ward;
@BindView(R.id.input_operating_lga) TextInputLayout input_operating_lga;
@BindView(R.id.input_operating_state) TextInputLayout input_operating_state;
@BindView(R.id.input_operating_ward) TextInputLayout input_operating_ward;
@BindView(R.id.input_operating_village) TextInputLayout input_operating_village;
@BindView(R.id.chkbox_entire_state) MaterialCheckBox chkbox_entire_state;
@BindView(R.id.chkbox_entire_lga) MaterialCheckBox chkbox_entire_lga;
@BindView(R.id.chkbox_entire_ward) MaterialCheckBox chkbox_entire_ward;
@BindView(R.id.btn_cancel) MaterialButton btn_cancel;
@BindView(R.id.btn_add_area) MaterialButton btn_add_area;
@BindView(R.id.btn_finish_reg) MaterialButton btn_finish_reg;
@BindView(R.id.tv_operating_areas) MaterialTextView tv_operating_areas;

    SessionManager sessionM;
    HashMap<String, String> transport_details;
    List<OperatingAreas> area, temp, savedAreas;
    List<Vehicles> savedVehicles;
    List<Drivers> savedDrivers;
    String owner_id;
    TransporterDatabase transportdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_operating_areas);
        ButterKnife.bind(this);
        sessionM = new SessionManager(AddOperatingAreas.this);
        transport_details = sessionM.getTransporterDetails();
        transportdb = TransporterDatabase.getInstance(AddOperatingAreas.this);
        owner_id = transport_details.get(SessionManager.KEY_DRIVER_ID);
        initStateAdapter();
        displayCurrentAreasAdded();

        edit_operating_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                edit_operating_lga.setText("");
                edit_operating_ward.setText("");
                initLGAdapter();
            }
        });

        edit_operating_lga.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                edit_operating_ward.setText("");
                initWardsAdapter();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick({R.id.btn_cancel})
    public void cancelReg(){
        new MaterialAlertDialogBuilder(AddOperatingAreas.this)
                .setIcon(R.drawable.ic_exclamation_mark)
                .setTitle("Delete Registration ?")
                .setMessage("You are about to delete all registrations for this transporter. Do you want to go ahead ?")
                .setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sessionM.CLEAR_REGISTRATION_SESSION();
                        startActivity(new Intent(AddOperatingAreas.this, Main2Activity.class)
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

    @OnClick(R.id.btn_add_area)
    public void addNewArea(){
        if(!checkEmptyInputs()){
            addNewAreaToArrayList();
            finish();
            startActivity(getIntent());
        }

    }

    @OnClick(R.id.btn_finish_reg)
    public void finishRegistration(){

        if(transport_details.get(SessionManager.KEY_OPERATING_AREA_DETAILS).isEmpty()){
            Toast.makeText(AddOperatingAreas.this, "Kindly add an Operating Area", Toast.LENGTH_LONG).show();
        }else {
            new MaterialAlertDialogBuilder(AddOperatingAreas.this)
                    .setTitle("Submit All Entered Details ?")
                    .setMessage("Do you want to submit registration details of this transporter ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            beginLuxandActivity();
                        }
                    })
                    .setNegativeButton("No, Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
        }
    }

    @OnClick(R.id.chkbox_entire_state)
    public void operateEntireState(){
        if(chkbox_entire_state.isChecked()){
            disableInputLayout(input_operating_lga);
            disableInputLayout(input_operating_ward);
            disableInputLayout(input_operating_village);
        }else{
            enableInputLayout(input_operating_lga);
            enableInputLayout(input_operating_ward);
            enableInputLayout(input_operating_village);
        }
    }

    @OnClick(R.id.chkbox_entire_lga)
    public void operateEntireLGA(){
        if(!chkbox_entire_state.isChecked()){
            if(chkbox_entire_lga.isChecked()){
                disableInputLayout(input_operating_ward);
                disableInputLayout(input_operating_village);
            }else{
                enableInputLayout(input_operating_ward);
                enableInputLayout(input_operating_village);
            }
        }else{
            //Do Nothing.
        }

    }

    @OnClick(R.id.chkbox_entire_ward)
    public void operateEntireWard(){
        if(!chkbox_entire_lga.isChecked() && !chkbox_entire_state.isChecked()){
            if(chkbox_entire_ward.isChecked()){
                disableInputLayout(input_operating_village);
            }else{
                enableInputLayout(input_operating_village);
            }
        }else{
            //Do Nothing
        }
    }

    public void disableInputLayout(TextInputLayout inputL){
        inputL.getEditText().getText().clear();
        inputL.setBackgroundColor(Color.LTGRAY);
        inputL.setEnabled(false);
    }

    public void enableInputLayout(TextInputLayout inputL){
        inputL.setBackgroundColor(Color.WHITE);
        inputL.setEnabled(true);
    }

    public List<OperatingAreas>convertInputsToModel(){
        area = new ArrayList<>();
        area.add(new OperatingAreas(owner_id,
                edit_operating_state.getText().toString(),
                edit_operating_lga.getText().toString(),
                edit_operating_ward.getText().toString(),
                edit_operating_village.getText().toString(),
                "no"));

        return area;
    }

    public void addNewAreaToArrayList(){
        Gson gson = new Gson();
        Type areaType = new TypeToken<List<OperatingAreas>>(){}.getType();
        temp = new ArrayList<>();
        if(transport_details.get(SessionManager.KEY_OPERATING_AREA_DETAILS) != ""){
            temp = gson.fromJson(transport_details.get(SessionManager.KEY_OPERATING_AREA_DETAILS), areaType);
        }

        temp.addAll(convertInputsToModel());

        sessionM.SET_OPERATING_AREA_DETAILS(gson.toJson(temp));
    }

    public void displayCurrentAreasAdded(){
        //Here, we display all the areas in the shared preferences value in a Textview.

        Gson gson = new Gson();
        Type currentAreaType = new TypeToken<List<OperatingAreas>>(){}.getType();
        List<OperatingAreas> currentAreas = new ArrayList<>();
        String area_json = transport_details.get(SessionManager.KEY_OPERATING_AREA_DETAILS);

        if(!area_json.equals("")) {
            currentAreas = gson.fromJson(area_json, currentAreaType);

            int i = 1;
            for (OperatingAreas opArea: currentAreas) {

                if (opArea.getLga_id().equals("") && opArea.getWard_id().equals("") && opArea.getVillage_id().equals("")){

                    tv_operating_areas.append(i + ". " + opArea.getState_id() + "\n");

                } else if (opArea.getWard_id().equals("") && opArea.getVillage_id().equals("")) {

                    tv_operating_areas.append(i + ". " + opArea.getState_id() + ", " + opArea.getLga_id() + "\n");

                } else if (opArea.getVillage_id().equals("")) {

                    tv_operating_areas.append(i + ". " + opArea.getState_id() + ", " + opArea.getLga_id() + ", " + opArea.getWard_id() + "\n");

                } else {

                    tv_operating_areas.append(i + ". " + opArea.getState_id() + ", " + opArea.getLga_id() + ", " + opArea.getWard_id() + ", " + opArea.getVillage_id() + "\n");

                }
                i++;

            }
        }else{
            tv_operating_areas.setText("Add Operating Areas to view them here.");
        }
    }

    public void initStateAdapter(){

        @SuppressLint("StaticFieldLeak") getStates getS = new getStates(AddOperatingAreas.this){
            @Override
            protected void onPostExecute(List<String> strings) {
                super.onPostExecute(strings);
                ArrayAdapter<String> stateAdapter =
                        new ArrayAdapter<>(getApplicationContext(),
                                R.layout.dropdown_menu_popup_item,
                                strings);
                edit_operating_state.setAdapter(stateAdapter);
            }
        };getS.execute();

    }

    public void initLGAdapter(){
        @SuppressLint("StaticFieldLeak") getLGAs getLga = new getLGAs(AddOperatingAreas.this){
            @Override
            protected void onPostExecute(List<String> strings) {
                super.onPostExecute(strings);
                ArrayAdapter<String> lgaAdapter =
                        new ArrayAdapter<>(getApplicationContext(),
                                R.layout.dropdown_menu_popup_item,
                                strings);
                edit_operating_lga.setAdapter(lgaAdapter);
            }
        };getLga.execute(edit_operating_state.getText().toString());
    }

    public void initWardsAdapter(){
        @SuppressLint("StaticFieldLeak") getWards getWard = new getWards(AddOperatingAreas.this){
            @Override
            protected void onPostExecute(List<String> strings) {
                super.onPostExecute(strings);
                ArrayAdapter<String> wardAdapter =
                        new ArrayAdapter<>(getApplicationContext(),
                                R.layout.dropdown_menu_popup_item,
                                strings);
                edit_operating_ward.setAdapter(wardAdapter);
            }
        } ;getWard.execute(edit_operating_lga.getText().toString());
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

    public void beginLuxandActivity(){
        new LuxandInfo(this).putTemplate(transport_details.get(SessionManager.KEY_REG_TEMPLATE));
        Intent LuxandIntent = new Intent(this, LuxandActivity.class);
        startActivityForResult(LuxandIntent, 419);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 419 && data != null && data.getIntExtra("RESULT", 0) == 1){
            //Face authenticated
            Toast.makeText(AddOperatingAreas.this, "Saving Values", Toast.LENGTH_LONG).show();
            saveAllValues();
        }else{
            //NO Face was captured.
            Toast.makeText(AddOperatingAreas.this, "Unable to confirm facial template", Toast.LENGTH_LONG).show();
        }
    }

    public void saveAllValues(){
        Gson gson = new Gson();

        savedAreas = new ArrayList<>();
        savedVehicles = new ArrayList<>();
        savedDrivers = new ArrayList<>();

        savedAreas = gson.fromJson(transport_details.get(SessionManager.KEY_OPERATING_AREA_DETAILS), new TypeToken<List<OperatingAreas>>(){}.getType());
        savedVehicles = gson.fromJson(transport_details.get(SessionManager.KEY_VEHICLE_DETAILS), new TypeToken<List<Vehicles>>(){}.getType());
        savedDrivers = gson.fromJson(transport_details.get(SessionManager.KEY_DRIVER_DETAILS), new TypeToken<List<Drivers>>(){}.getType());

        @SuppressLint("StaticFieldLeak") insertIntoAreaTable insert = new insertIntoAreaTable(AddOperatingAreas.this){
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        };insert.execute(savedAreas);

        @SuppressLint("StaticFieldLeak") insertIntoDriverTable insert1 = new insertIntoDriverTable(AddOperatingAreas.this){
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        };insert1.execute(savedDrivers);

        @SuppressLint("StaticFieldLeak") insertIntoVehicleTable insert2 = new insertIntoVehicleTable(AddOperatingAreas.this){
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        };insert2.execute(savedVehicles);

        displayConfirmationDialog();
    }

    @SuppressLint("StaticFieldLeak")
    public class insertIntoAreaTable extends AsyncTask<List<OperatingAreas>, Void, Void>{
        Context context;
        public insertIntoAreaTable(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(List<OperatingAreas>... lists) {
            try{
                transportdb.getOperatingAreaDao().InsertAreaFromList(lists[0]);
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class insertIntoVehicleTable extends AsyncTask<List<Vehicles>, Void, Void>{
        Context mCtx;

        public insertIntoVehicleTable(Context mCtx) {
            this.mCtx = mCtx;
        }

        @Override
        protected Void doInBackground(List<Vehicles>... lists) {
            try{
                transportdb.getVehicleDao().InsertVehiclesFromList(lists[0]);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class insertIntoDriverTable extends AsyncTask<List<Drivers>, Void, Void>{
        Context c;

        public insertIntoDriverTable(Context c) {
            this.c = c;
        }

        @Override
        protected Void doInBackground(List<Drivers>... lists) {
            try{
                transportdb.getDriverDao().InsertDriverFromList(lists[0]);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    public void displayConfirmationDialog(){
        new MaterialAlertDialogBuilder(AddOperatingAreas.this)
                .setIcon(R.drawable.ic_success_checked)
                .setCancelable(false)
                .setMessage(savedDrivers.get(0).getFirst_name() + " " + savedDrivers.get(0).getLast_name() + " has been successfully registered as a transporter. ")
                .setPositiveButton("Thank You", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(AddOperatingAreas.this, Main2Activity.class));
                    }
                }).show();
    }

    public boolean checkEmptyInputs(){

        if(chkbox_entire_state.isChecked()){

            if(edit_operating_state.getText().toString().equals("")){
                edit_operating_state.setError("This input is needed");
                return true;
            }

        }else if(chkbox_entire_lga.isChecked()){

            if(edit_operating_state.getText().toString().equals("")){
                edit_operating_state.setError("This input is needed");
                return true;
            }
            if(edit_operating_lga.getText().toString().equals("")) {
                edit_operating_lga.setError("This input is needed");
                return true;
            }

        }else if(chkbox_entire_ward.isChecked()){

            if(edit_operating_state.getText().toString().equals("")){
                edit_operating_state.setError("This input is needed");
                return true;
            }
            if(edit_operating_lga.getText().toString().equals("")) {
                edit_operating_lga.setError("This input is needed");
                return true;
            }
            if(edit_operating_ward.getText().toString().equals("")) {
                edit_operating_ward.setError("This input is needed");
                return true;
            }

        }else if(!chkbox_entire_state.isChecked() && !chkbox_entire_lga.isChecked() && !chkbox_entire_ward.isChecked()){
            if(edit_operating_state.getText().toString().equals("")){
                edit_operating_state.setError("This input is needed");
                return true;
            }
            if(edit_operating_lga.getText().toString().equals("")) {
                edit_operating_lga.setError("This input is needed");
                return true;
            }
            if(edit_operating_ward.getText().toString().equals("")) {
                edit_operating_ward.setError("This input is needed");
                return true;
            }
            if(edit_operating_village.getText().toString().equals("")){
                edit_operating_village.setError("This input is needed");
                return true;
            }
        }

        return false;
    }


}
