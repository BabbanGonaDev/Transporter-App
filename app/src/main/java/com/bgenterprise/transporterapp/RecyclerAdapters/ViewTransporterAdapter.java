package com.bgenterprise.transporterapp.RecyclerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bgenterprise.transporterapp.Database.Tables.Drivers;
import com.bgenterprise.transporterapp.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewTransporterAdapter extends RecyclerView.Adapter<ViewTransporterAdapter.ViewHolder> implements Filterable {
    List<Drivers> driversList;
    List<Drivers> mFilteredList;
    private final OnItemClickListener listener;
    private Context mCtx;

    public ViewTransporterAdapter(Context mCtx, List<Drivers> driversList, OnItemClickListener listener) {
        this.driversList = driversList;
        this.mFilteredList = driversList;
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
        Drivers filteredDriver = mFilteredList.get(position);

        holder.mtv_transporter_name.setText(filteredDriver.getFirst_name() + " " + filteredDriver.getLast_name());
        holder.mtv_transporter_id.setText(filteredDriver.getDriver_id());
        holder.mtv_vehicle_number.setText("Vehicles: " + filteredDriver.getNo_of_vehicles());
        holder.mtv_reg_date.setText("Reg. Date: " + filteredDriver.getReg_date());
    }

    public interface OnItemClickListener{
        void onClick(Drivers drivers);
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter(){

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if(charString.isEmpty()){
                    mFilteredList = driversList;
                }else{
                    List<Drivers> filteredList = new ArrayList<>();
                    for(Drivers drive: driversList){
                        if(drive.getFirst_name().toLowerCase().contains(charString) || drive.getLast_name().toLowerCase().contains(charString) || drive.getPhone_number().toLowerCase().contains(charString)){
                            filteredList.add(drive);
                        }
                    }
                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (List<Drivers>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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

            itemView.setOnClickListener(view -> {
                listener.onClick(mFilteredList.get(getLayoutPosition()));
            });
        }
    }
}
