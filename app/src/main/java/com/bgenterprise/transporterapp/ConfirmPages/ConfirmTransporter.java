package com.bgenterprise.transporterapp.ConfirmPages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.bgenterprise.transporterapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfirmTransporter extends AppCompatActivity {
@BindView(R.id.edit_confirm_fname) TextInputEditText edit_confirm_fname;
@BindView(R.id.edit_confirm_lname) TextInputEditText edit_confirm_lname;
@BindView(R.id.edit_confirm_phone_no) TextInputEditText edit_confirm_phone_no;
@BindView(R.id.edit_confirm_vehicles) TextInputEditText edit_confirm_vehicles;
@BindView(R.id.edit_confirm_location) TextInputEditText edit_confirm_location;
@BindView(R.id.layout_confirm_fname) TextInputLayout layout_confirm_fname;
@BindView(R.id.layout_confirm_lname) TextInputLayout layout_confirm_lname;
@BindView(R.id.layout_confirm_phone_no) TextInputLayout layout_confirm_phone_no;
@BindView(R.id.layout_confirm_vehicles) TextInputLayout layout_confirm_vehicles;
@BindView(R.id.layout_confirm_location) TextInputLayout layout_confirm_location;

boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_transporter);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Confirm Transporter Details");
        isEdit = false;
        lockDetails();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.edit_details:
                EditingFunction(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void EditingFunction(MenuItem editItem){
        if(!isEdit){
            editDetails();
            editItem.setIcon(R.drawable.ic_done_all_while_24dp);
            Toast.makeText(this, "Ready for Edit", Toast.LENGTH_LONG).show();
            isEdit = true;
        }else if(isEdit){
            editItem.setIcon(R.drawable.ic_edit_white_24dp);
            isEdit = false;
            lockDetails();
            Toast.makeText(this, "Changes have been saved", Toast.LENGTH_LONG).show();
        }

    }

    public void editDetails(){
        //TODO ---> Set the Inputs as editable.
        setEnabled(edit_confirm_fname, layout_confirm_fname);
        setEnabled(edit_confirm_lname, layout_confirm_lname);
        setEnabled(edit_confirm_phone_no, layout_confirm_phone_no);
        setEnabled(edit_confirm_location, layout_confirm_location);
        setEnabled(edit_confirm_vehicles, layout_confirm_vehicles);
    }

    public void setEnabled(TextInputEditText e, TextInputLayout f){
        e.setEnabled(true);
    }

    public void lockDetails(){
        //TODO ---> Set inputs back to locked.
        setDisabled(edit_confirm_fname, layout_confirm_fname);
        setDisabled(edit_confirm_lname, layout_confirm_lname);
        setDisabled(edit_confirm_location, layout_confirm_location);
        setDisabled(edit_confirm_phone_no, layout_confirm_phone_no);
        setDisabled(edit_confirm_vehicles, layout_confirm_vehicles);

    }

    public void setDisabled(TextInputEditText d, TextInputLayout g){
        g.setEnabled(false);
    }
}
