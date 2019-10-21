package com.bgenterprise.transporterapp.Database.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "payments_table")
public class Payments {

    @NonNull
    @PrimaryKey
    private String payment_id;
    private String transporter_id;
    private String amount_paid;
    private String mode_of_payment;
    private String payment_date;

    public Payments(@NonNull String payment_id, String transporter_id, String amount_paid, String mode_of_payment, String payment_date) {
        this.payment_id = payment_id;
        this.transporter_id = transporter_id;
        this.amount_paid = amount_paid;
        this.mode_of_payment = mode_of_payment;
        this.payment_date = payment_date;
    }

    @NonNull
    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(@NonNull String payment_id) {
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
}
