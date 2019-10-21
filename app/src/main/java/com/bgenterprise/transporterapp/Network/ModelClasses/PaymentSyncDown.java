package com.bgenterprise.transporterapp.Network.ModelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentSyncDown {

    @SerializedName("payment_id")
    @Expose
    private String payment_id;

    @SerializedName("transporter_id")
    @Expose
    private String transporter_id;

    @SerializedName("amount_paid")
    @Expose
    private String amount_paid;

    @SerializedName("mode_of_payment")
    @Expose
    private String mode_of_payment;

    @SerializedName("payment_date")
    @Expose
    private String payment_date;

    @SerializedName("last_sync_time")
    @Expose
    private String last_sync_time;

    public PaymentSyncDown() {
    }

    public PaymentSyncDown(String payment_id, String transporter_id, String amount_paid, String mode_of_payment, String payment_date, String last_sync_time) {
        this.payment_id = payment_id;
        this.transporter_id = transporter_id;
        this.amount_paid = amount_paid;
        this.mode_of_payment = mode_of_payment;
        this.payment_date = payment_date;
        this.last_sync_time = last_sync_time;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getTransporter_id() {
        return transporter_id;
    }

    public void setTransporter_id(String transporter_id) {
        this.transporter_id = transporter_id;
    }

    public String getAmount_paid() {
        return amount_paid;
    }

    public void setAmount_paid(String amount_paid) {
        this.amount_paid = amount_paid;
    }

    public String getMode_of_payment() {
        return mode_of_payment;
    }

    public void setMode_of_payment(String mode_of_payment) {
        this.mode_of_payment = mode_of_payment;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public String getLast_sync_time() {
        return last_sync_time;
    }

    public void setLast_sync_time(String last_sync_time) {
        this.last_sync_time = last_sync_time;
    }
}
