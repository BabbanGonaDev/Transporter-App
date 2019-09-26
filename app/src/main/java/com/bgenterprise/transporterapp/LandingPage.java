package com.bgenterprise.transporterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LandingPage extends AppCompatActivity {
    @BindView(R.id.btn_add_transporter)
    MaterialButton btnAddTransporter;

    SessionManager sessionM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        ButterKnife.bind(this);
        sessionM = new SessionManager(LandingPage.this);
        sessionM.CLEAR_ALL_SESSION();

        //TODO --> Check Phone date if it's earlier than date of app development.
    }

    @OnClick(R.id.btn_add_transporter)
    public void addTransport(){
        startActivity(new Intent(LandingPage.this, AddTransporter.class));
    }
}
