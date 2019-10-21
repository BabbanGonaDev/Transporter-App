package com.bgenterprise.transporterapp.Database.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "hsf_table")
public class HSF {

    @NonNull
    @PrimaryKey
    private String hsf_id;
    private String field_id;
    private String bags_transported;
    private String price_per_bag;
    private String transporter_id;
    private String cc_id;
    private String date_processed;

    //Required Empty Constructor
    @Ignore
    public HSF() {
    }

    public HSF(@NonNull String hsf_id, String field_id, String bags_transported, String price_per_bag, String transporter_id, String cc_id, String date_processed) {
        this.hsf_id = hsf_id;
        this.field_id = field_id;
        this.bags_transported = bags_transported;
        this.price_per_bag = price_per_bag;
        this.transporter_id = transporter_id;
        this.cc_id = cc_id;
        this.date_processed = date_processed;
    }

    @NonNull
    public String getHsf_id() {
        return hsf_id;
    }

    public void setHsf_id(@NonNull String hsf_id) {
        this.hsf_id = hsf_id;
    }

    public String getField_id() {
        return field_id;
    }

    public void setField_id(String field_id) {
        this.field_id = field_id;
    }

    public String getBags_transported() {
        return bags_transported;
    }

    public void setBags_transported(String bags_transported) {
        this.bags_transported = bags_transported;
    }

    public String getPrice_per_bag() {
        return price_per_bag;
    }

    public void setPrice_per_bag(String price_per_bag) {
        this.price_per_bag = price_per_bag;
    }

    public String getTransporter_id() {
        return transporter_id;
    }

    public void setTransporter_id(String transporter_id) {
        this.transporter_id = transporter_id;
    }

    public String getCc_id() {
        return cc_id;
    }

    public void setCc_id(String cc_id) {
        this.cc_id = cc_id;
    }

    public String getDate_processed() {
        return date_processed;
    }

    public void setDate_processed(String date_processed) {
        this.date_processed = date_processed;
    }
}


