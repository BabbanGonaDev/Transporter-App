package com.bgenterprise.transporterapp.TransporterDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bgenterprise.transporterapp.AppExecutors;
import com.bgenterprise.transporterapp.Database.Tables.Drivers;
import com.bgenterprise.transporterapp.Database.Tables.OperatingAreas;
import com.bgenterprise.transporterapp.Database.Tables.Vehicles;
import com.bgenterprise.transporterapp.Database.TransporterDatabase;
import com.bgenterprise.transporterapp.InputPages.PaymentOptionFrag;
import com.bgenterprise.transporterapp.Main2Activity;
import com.bgenterprise.transporterapp.R;
import com.bgenterprise.transporterapp.SessionManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.textview.MaterialTextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar xToolbar;

    @BindView(R.id.appbar)
    AppBarLayout appbar;

    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.edit_payment_icon)
    ImageButton edit_payment_icon;

    @BindView(R.id.mtv_payment_option)
    MaterialTextView mtv_payment_option;

    @BindView(R.id.mtv_driver_name)
    MaterialTextView mtv_driver_name;

    @BindView(R.id.mtv_driver_id)
    MaterialTextView mtv_driver_id;

    @BindView(R.id.mtv_driver_phone)
    MaterialTextView mtv_driver_phone;

    @BindView(R.id.mtv_driver_vehicle)
    MaterialTextView mtv_driver_vehicles;

    @BindView(R.id.mtv_driver_state)
    MaterialTextView mtv_driver_state;

    @BindView(R.id.mtv_driver_lga)
    MaterialTextView mtv_driver_lga;

    @BindView(R.id.mtv_driver_ward)
    MaterialTextView mtv_driver_ward;

    @BindView(R.id.mtv_vehicles)
    MaterialTextView mtv_vehicles;

    @BindView(R.id.mtv_areas)
    MaterialTextView mtv_areas;

    @BindView(R.id.mtv_bags_transported)
    MaterialTextView mtv_bags_transported;

    @BindView(R.id.mtv_amount_earned)
    MaterialTextView mtv_amount_earned;

    @BindView(R.id.mtv_amount_paid)
    MaterialTextView mtv_amount_paid;

    @BindView(R.id.mtv_balance_headline)
    MaterialTextView mtv_balance_headline;

    @BindView(R.id.mtv_pending_balance)
    MaterialTextView mtv_pending_balance;

    @BindView(R.id.mtv_last_payment_time)
    MaterialTextView mtv_last_payment_time;

    @BindView(R.id.mtv_hsf_processed)
    MaterialTextView mtv_hsf_processed;

    String driver_id;
    int bags_transported, amount_earned, amount_paid, pending_balance, hsf_processed;
    TransporterDatabase transportdb;
    Drivers driver;
    List<OperatingAreas> driverAreas;
    List<Vehicles> driverVehicles;
    SessionManager sessionM;
    HashMap<String, String> transport_details;
    DecimalFormat formatter = new DecimalFormat("#,###,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        transportdb = TransporterDatabase.getInstance(ProfileActivity.this);
        sessionM = new SessionManager(ProfileActivity.this);
        sessionM.CLEAR_PAYMENT_DETAILS();
        transport_details = sessionM.getTransporterDetails();

        try {
            driver_id = transport_details.get(SessionManager.KEY_TRANSPORTER_ID);
        } catch (Exception e) {
            e.printStackTrace();
            startActivity(new Intent(this, Main2Activity.class));
        }

        collapsingToolbarTitle();
        setSupportActionBar(xToolbar);
        getDriverDetails(driver_id);
        getVehiclesDetails(driver_id);
        getAreaDetails(driver_id);
        getPaymentDetails(driver_id);
        initUIDisplay();

    }

    public void collapsingToolbarTitle() {
        //Use this function to display the title in the collapsing toolbar, ONLY when the toolbar has been collapsed.
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(driver_id);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
    }

    public void initUIDisplay() {
        try {
            SimpleDateFormat sdfSource = new SimpleDateFormat(
                    "yyyy-MM-dd hh:mm:ss");
            // parse the string into Date object
            Date srcDate = sdfSource.parse(transport_details.get(SessionManager.KEY_LAST_SYNC_DOWN_HSF));

            mtv_driver_id.setText("Driver ID: " + driver.getDriver_id());
            mtv_driver_name.setText("Driver Name: " + driver.getFirst_name() + " " + driver.getLast_name());
            mtv_driver_phone.setText(driver.getPhone_number());
            mtv_driver_vehicles.setText(driver.getNo_of_vehicles() + " Vehicle(s)");
            mtv_driver_state.setText("State: " + driver.getDriver_state());
            mtv_driver_lga.setText("LGA: " + driver.getDriver_lga());
            mtv_driver_ward.setText("Ward: " + driver.getDriver_ward());
            mtv_bags_transported.setText(String.valueOf(bags_transported) + " Bag(s)");
            mtv_amount_earned.setText("NGN " + formatter.format(amount_earned));
            mtv_amount_paid.setText("NGN " + formatter.format(amount_paid));
            mtv_pending_balance.setText("NGN " + formatter.format(pending_balance));
            mtv_last_payment_time.setText("(As at: " + new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(srcDate)+ ")");
            mtv_hsf_processed.setText(String.valueOf(hsf_processed) + " HSF(s)");
            mtv_vehicles.setText("");
            mtv_areas.setText("");
            mtv_payment_option.setText("");

            displayVehicles();
            displayOperatingAreas();
            displayPaymentOption();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_call)
    public void CallTransporter() {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + driver.getPhone_number())));
    }

    @OnClick(R.id.btn_view_hsf)
    public void viewHSF(){
        sessionM.SET_TRANSPORTER_ID(driver_id);
        startActivity(new Intent(ProfileActivity.this, ViewTransporterHSF.class));
    }

    @OnClick(R.id.btn_view_payments)
    public void viewPayments(){
        sessionM.SET_TRANSPORTER_ID(driver_id);
        startActivity(new Intent(ProfileActivity.this, ViewTransporterPayments.class));
    }

    @OnClick(R.id.edit_payment_icon)
    public void editPaymentOption(){
        Bundle bundle = new Bundle();
        bundle.putString("driver_id", driver_id);
        FragmentManager fm = getSupportFragmentManager();
        PaymentOptionFrag optionFrag = new PaymentOptionFrag();
        optionFrag.setArguments(bundle);
        optionFrag.show(fm, "PaymentOptionFrag");
    }

    public void getDriverDetails(String driverID){

        AppExecutors.getInstance().diskIO().execute(() -> {
            try{
                driver = transportdb.getDriverDao().getDriverDetails(driverID);
                runOnUiThread(() -> initUIDisplay());
            }catch (Exception e){
                runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Unable to get driver details", Toast.LENGTH_LONG).show());
            }

        });

    }

    public void getVehiclesDetails(String driverID){

        AppExecutors.getInstance().diskIO().execute(() -> {
            try{
                driverVehicles = transportdb.getVehicleDao().getDriversVehicles(driverID);
                runOnUiThread(() -> initUIDisplay());
            }catch (Exception e){
                runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Unable to get vehicle details", Toast.LENGTH_LONG).show());
            }
        });

    }

    public void getAreaDetails(String driverID){

        AppExecutors.getInstance().diskIO().execute(() -> {
            try{
                driverAreas = transportdb.getOperatingAreaDao().getDriversAreas(driverID);
                runOnUiThread(() -> initUIDisplay());
            }catch (Exception e){
                runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Unable to get operating areas details", Toast.LENGTH_LONG).show());
            }
        });
    }

    public void getPaymentDetails(String driverID){
        AppExecutors.getInstance().diskIO().execute(() -> {

            bags_transported = transportdb.getHsfDao().bagsTransported(driverID);
            amount_earned = transportdb.getHsfDao().amountEarned(driverID);
            amount_paid = transportdb.getPaymentsDao().amountPaid(driverID);
            hsf_processed = transportdb.getHsfDao().hsfProcessed(driverID);

            runOnUiThread(() -> {
                pending_balance = (amount_earned - amount_paid);
                initUIDisplay();
            });
        });
    }

    public void displayVehicles(){
        int i = 1;
        for (Vehicles vehicles: driverVehicles) {
            mtv_vehicles.append(i + ". Type: " + vehicles.getVehicle_type() + "\n" +
                    "    Plate No: " + vehicles.getVehicle_plate_no() + "\n" +
                    "    Capacity: " + vehicles.getVehicle_capacity() + "\n" + "\n");
            i++;

        }
    }

    public void displayOperatingAreas(){
        int j = 1;
        for (OperatingAreas opArea: driverAreas) {
            if (opArea.getLga_id().equals("") && opArea.getWard_id().equals("") && opArea.getVillage_id().equals("")) {
                mtv_areas.append(j + ". " + opArea.getState_id() + "\n");
            } else if (opArea.getWard_id().equals("") && opArea.getVillage_id().equals("")) {
                mtv_areas.append(j + ". " + opArea.getState_id() + ", " + opArea.getLga_id() + "\n");
            } else if (opArea.getVillage_id().equals("")) {
                mtv_areas.append(j + ". " + opArea.getState_id() + ", " + opArea.getLga_id() + ", " + opArea.getWard_id() + "\n");
            } else {
                mtv_areas.append(j + ". " + opArea.getState_id() + ", " + opArea.getLga_id() + ", " + opArea.getWard_id() + ", " + opArea.getVillage_id() + "\n");
            }
            j++;
        }
    }

    public void displayPaymentOption(){
        switch (driver.getPayment_option()){
            case "BG Prepaid Card":
                mtv_payment_option.append("Option: " + driver.getPayment_option() + "\n");
                mtv_payment_option.append("Card Number: " + driver.getBg_card() + "\n");
                edit_payment_icon.setVisibility(View.GONE);
                break;
            case "Bank Account":
                mtv_payment_option.append("Option: " + driver.getPayment_option() + "\n");
                mtv_payment_option.append("Bank Name: " + driver.getBank_name() + "\n");
                mtv_payment_option.append("Account Number: " + driver.getAccount_number() + "\n");
                mtv_payment_option.append("Account Name: " + driver.getAccount_name() + "\n");
                edit_payment_icon.setVisibility(View.GONE);
                break;
            case "Cash":
                mtv_payment_option.append("Option: " + driver.getPayment_option() + "\n");
                edit_payment_icon.setVisibility(View.GONE);
                break;
            default:
                mtv_payment_option.append("N/A");
                edit_payment_icon.setVisibility(View.VISIBLE);
        }
    }
}
