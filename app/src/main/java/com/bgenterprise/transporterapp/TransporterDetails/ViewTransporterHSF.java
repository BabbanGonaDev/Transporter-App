package com.bgenterprise.transporterapp.TransporterDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bgenterprise.transporterapp.AppExecutors;
import com.bgenterprise.transporterapp.Database.Tables.HSF;
import com.bgenterprise.transporterapp.Database.TransporterDatabase;
import com.bgenterprise.transporterapp.R;
import com.bgenterprise.transporterapp.RecyclerAdapters.ViewHSFAdapter;
import com.bgenterprise.transporterapp.SessionManager;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewTransporterHSF extends AppCompatActivity{
    @BindView(R.id.rcView_hsf)
    RecyclerView hsf_recycler;

    @BindView(R.id.hsf_toolbar)
    Toolbar hsf_toolbar;

    SessionManager sessionM;
    HashMap<String, String> transport_details;
    ViewHSFAdapter hsfAdapter;
    TransporterDatabase transportdb;
    List<HSF> hsfList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transporter_hsf);
        ButterKnife.bind(this);
        setSupportActionBar(hsf_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionM = new SessionManager(ViewTransporterHSF.this);
        transport_details = sessionM.getTransporterDetails();
        transportdb = TransporterDatabase.getInstance(ViewTransporterHSF.this);
        initRecyclerAdapter();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem search = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                hsfAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
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

    public void initRecyclerAdapter(){

        AppExecutors.getInstance().diskIO().execute(() -> {
            hsfList = transportdb.getHsfDao().getTransporterHSF(transport_details.get(SessionManager.KEY_TRANSPORTER_ID));
            runOnUiThread(() -> {
                hsfAdapter = new ViewHSFAdapter(getApplicationContext(), hsfList, hsf -> {
                    //Do Nothing
                });
                RecyclerView.LayoutManager hLayoutManager = new LinearLayoutManager(this);
                hsf_recycler.setLayoutManager(hLayoutManager);
                hsf_recycler.setItemAnimator(new DefaultItemAnimator());
                hsf_recycler.setAdapter(hsfAdapter);
                hsfAdapter.notifyDataSetChanged();
            });
        });
    }

}
