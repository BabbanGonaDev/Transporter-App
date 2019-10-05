package com.bgenterprise.transporterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.btnAccessControl)
    MaterialButton btnAccessControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_transporter_truck_white);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.btnAccessControl)
    public void redirect(){
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setComponent(new ComponentName("com.babbangona.accesscontrol", "com.babbangona.accesscontrol.MainActivity"));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Install Access Control and try again", Toast.LENGTH_SHORT).show();
        }
    }
}
