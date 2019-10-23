package com.bgenterprise.transporterapp.TransporterDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.bgenterprise.transporterapp.AppExecutors;
import com.bgenterprise.transporterapp.Database.Tables.Payments;
import com.bgenterprise.transporterapp.Database.TransporterDatabase;
import com.bgenterprise.transporterapp.R;
import com.bgenterprise.transporterapp.RecyclerAdapters.ViewPaymentAdapter;
import com.bgenterprise.transporterapp.SessionManager;
import com.google.android.material.textview.MaterialTextView;

import java.util.HashMap;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewTransporterPayments extends AppCompatActivity {
    @BindView(R.id.mtv_payment_headline)
    MaterialTextView mtv_payment_headline;

    @BindView(R.id.rcv_payments)
    RecyclerView rcv_payments;

    SessionManager sessionM;
    HashMap<String, String> transport_details;
    TransporterDatabase transportdb;
    List<Payments> paymentsList;
    ViewPaymentAdapter paymentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transporter_payments);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionM = new SessionManager(ViewTransporterPayments.this);
        transport_details = sessionM.getTransporterDetails();
        transportdb = TransporterDatabase.getInstance(ViewTransporterPayments.this);
        initPaymentRecycler();
        mtv_payment_headline.setText(transport_details.get(SessionManager.KEY_TRANSPORTER_ID) + " Payments");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void initPaymentRecycler(){

        AppExecutors.getInstance().diskIO().execute(() -> {
            paymentsList = transportdb.getPaymentsDao().getTransporterPayments(transport_details.get(SessionManager.KEY_TRANSPORTER_ID));
            runOnUiThread(() -> {
                paymentAdapter = new ViewPaymentAdapter(getApplicationContext(), paymentsList, paymentsList -> {
                    //Do Nothing.
                });
                RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(getApplicationContext());
                rcv_payments.setLayoutManager(pLayoutManager);
                rcv_payments.setItemAnimator(new DefaultItemAnimator());
                rcv_payments.setAdapter(paymentAdapter);
                paymentAdapter.notifyDataSetChanged();
            });
        });
    }
}
