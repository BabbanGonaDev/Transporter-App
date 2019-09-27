package com.bgenterprise.transporterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;

import com.bgenterprise.transporterapp.Database.Tables.OperatingAreas;
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

@BindView(R.id.edit_operating_state) TextInputEditText edit_operating_state;
@BindView(R.id.edit_operating_lga) AutoCompleteTextView edit_operating_lga;
@BindView(R.id.edit_operating_village) AutoCompleteTextView edit_operating_village;
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
    List<OperatingAreas> area, temp;
    String owner_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_operating_areas);
        ButterKnife.bind(this);
        sessionM = new SessionManager(AddOperatingAreas.this);
        transport_details = sessionM.getTransporterDetails();
        owner_id = "SAADFKGLDDDDE";
        displayCurrentAreasAdded();
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
                        sessionM.CLEAR_ALL_SESSION();
                        startActivity(new Intent(AddOperatingAreas.this, LandingPage.class)
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
        addNewAreaToArrayList();
        finish();
        startActivity(getIntent());
    }

    @OnClick(R.id.btn_finish_reg)
    public void finishRegistration(){
        //TODO ---> This is where we move to confirm page.
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
                edit_operating_village.getText().toString()));

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

}
