package com.bgenterprise.transporterapp.RecyclerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bgenterprise.transporterapp.Database.Tables.Payments;
import com.bgenterprise.transporterapp.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewPaymentAdapter extends RecyclerView.Adapter<ViewPaymentAdapter.ViewHolder> {

    private List<Payments> paymentsList;
    private Context mCtx;
    private final OnItemClickListener listener;

    public ViewPaymentAdapter(Context mCtx, List<Payments> paymentsList, OnItemClickListener listener) {
        this.paymentsList = paymentsList;
        this.mCtx = mCtx;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_payment_card, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       final Payments payments = paymentsList.get(position);

       holder.mtv_payment_amount.setText("Amount paid: " + payments.getAmount_paid());
       holder.mtv_payment_date.setText("Date of Payment: " + payments.getPayment_date());
       holder.mtv_payment_mode.setText("Mode of Payment: " + payments.getMode_of_payment());
    }

    @Override
    public int getItemCount() {
        return paymentsList.size();
    }

    public interface OnItemClickListener{
        void onClick(Payments paymentsList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.mtv_payment_amount)
        MaterialTextView mtv_payment_amount;

        @BindView(R.id.mtv_payment_date)
        MaterialTextView mtv_payment_date;

        @BindView(R.id.mtv_payment_mode)
        MaterialTextView mtv_payment_mode;

        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(view1 -> {
                //Do Nothing.
            });
        }
    }
}
