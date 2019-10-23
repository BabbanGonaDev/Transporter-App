package com.bgenterprise.transporterapp.RecyclerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bgenterprise.transporterapp.Database.Tables.HSF;
import com.bgenterprise.transporterapp.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewHSFAdapter extends RecyclerView.Adapter<ViewHSFAdapter.ViewHolder> implements Filterable {
    private List<HSF> hsfList;
    private List<HSF> mFilteredList;
    private Context context;
    private final OnItemClickListener listener;

    public ViewHSFAdapter(Context context, List<HSF> hsfList, OnItemClickListener listener) {
        this.hsfList = hsfList;
        this.mFilteredList = hsfList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_hsf_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final HSF hsf = mFilteredList.get(position);

        holder.mtv_hsf_id.setText("HSF ID: " + hsf.getHsf_id());
        holder.mtv_field_id.setText("Field ID: " + hsf.getField_id());
        holder.mtv_date_processed.setText("Date processed: " + hsf.getDate_processed());
        holder.mtv_number_of_bags.setText("Transported: " + hsf.getBags_transported() + " Bag(s)");
    }

    public interface OnItemClickListener{
        void onClick(HSF hsfList);
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if(charString.isEmpty()){
                    mFilteredList = hsfList;
                }else{
                    List<HSF> filteredList = new ArrayList<>();
                    for(HSF hsf: hsfList){
                        if(hsf.getHsf_id().toLowerCase().contains(charString) || hsf.getField_id().toLowerCase().contains(charString) || hsf.getCc_id().toLowerCase().contains(charString)){
                            filteredList.add(hsf);
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
                mFilteredList = (List<HSF>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.mtv_hsf_id)
        MaterialTextView mtv_hsf_id;

        @BindView(R.id.mtv_field_id)
        MaterialTextView mtv_field_id;

        @BindView(R.id.mtv_number_of_bags)
        MaterialTextView mtv_number_of_bags;

        @BindView(R.id.mtv_date_processed)
        MaterialTextView mtv_date_processed;

        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(view1 -> listener.onClick(mFilteredList.get(getLayoutPosition())));
        }
    }
}
