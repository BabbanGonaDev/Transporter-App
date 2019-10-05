package com.bgenterprise.transporterapp.RecyclerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bgenterprise.transporterapp.Database.Tables.Drivers;
import com.bgenterprise.transporterapp.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewTransporterAdapter extends RecyclerView.Adapter<ViewTransporterAdapter.ViewHolder> {
    List<Drivers> driversList;
    private final OnItemClickListener listener;
    private Context mCtx;

    public ViewTransporterAdapter(Context mCtx, List<Drivers> driversList, OnItemClickListener listener) {
        this.driversList = driversList;
        this.listener = listener;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_transporter_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Drivers drivers = driversList.get(position);
        holder.mtv_transporter_name.setText(drivers.getFirst_name() + " " + drivers.getLast_name());
        holder.mtv_transporter_id.setText(drivers.getDriver_id());
        holder.mtv_vehicle_number.setText("Vehicles: " + drivers.getNo_of_vehicles());
        holder.mtv_reg_date.setText("Reg. Date: " + drivers.getReg_date());
    }

    public interface OnItemClickListener{
        void onClick(Drivers drivers);
    }

    @Override
    public int getItemCount() {
        return driversList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.mtv_transporter_name)
        MaterialTextView mtv_transporter_name;

        @BindView(R.id.mtv_transporter_id)
        MaterialTextView mtv_transporter_id;

        @BindView(R.id.mtv_vehicle_number)
        MaterialTextView mtv_vehicle_number;

        @BindView(R.id.mtv_reg_date)
        MaterialTextView mtv_reg_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(driversList.get(getLayoutPosition()));
                }
            });
        }
    }
}
