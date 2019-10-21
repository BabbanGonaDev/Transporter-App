package com.bgenterprise.transporterapp.Network.ModelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HSFSyncDown {

    @SerializedName("hsf_id")
    @Expose
    private String hsf_id;

    @SerializedName("field_id")
    @Expose
    private String field_id;

    @SerializedName("bags_transported")
    @Expose
    private String bags_transported;

    @SerializedName("price_per_bag")
    @Expose
    private String price_per_bag;

    @SerializedName("transporter_id")
    @Expose
    private String transporter_id;

    @SerializedName("cc_id")
    @Expose
    private String cc_id;

    @SerializedName("date_processed")
    @Expose
    private String date_processed;

    @SerializedName("last_sync_time")
    @Expose
    private String last_sync_time;

    public HSFSyncDown() {
    }

    public HSFSyncDown(String hsf_id, String field_id, String bags_transported, String price_per_bag, String transporter_id, String cc_id, String date_processed, String last_sync_time) {
        this.hsf_id = hsf_id;
        this.field_id = field_id;
        this.bags_transported = bags_transported;
        this.price_per_bag = price_per_bag;
        this.transporter_id = transporter_id;
        this.cc_id = cc_id;
        this.date_processed = date_processed;
        this.last_sync_time = last_sync_time;
    }

    public String getHsf_id() {
        return hsf_id;
    }

    public void setHsf_id(String hsf_id) {
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

    public String getLast_sync_time() {
        return last_sync_time;
    }

    public void setLast_sync_time(String last_sync_time) {
        this.last_sync_time = last_sync_time;
    }
}
