package com.bgenterprise.transporterapp.Network.ModelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OperatingAreaSyncDown {

    @SerializedName("owner_id")
    @Expose
    private String owner_id;

    @SerializedName("state_id")
    @Expose
    private String state_id;

    @SerializedName("village_id")
    @Expose
    private String village_id;

    @SerializedName("ward_id")
    @Expose
    private String ward_id;

    @SerializedName("lga_id")
    @Expose
    private String lga_id;

    @SerializedName("sync_status")
    @Expose
    private String sync_status;

    @SerializedName("last_sync_time")
    @Expose
    private String last_sync_time;

    public OperatingAreaSyncDown() {}

    public OperatingAreaSyncDown(String owner_id, String state_id, String village_id, String ward_id, String lga_id, String sync_status, String last_sync_time) {
        this.owner_id = owner_id;
        this.state_id = state_id;
        this.village_id = village_id;
        this.ward_id = ward_id;
        this.lga_id = lga_id;
        this.sync_status = sync_status;
        this.last_sync_time = last_sync_time;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getVillage_id() {
        return village_id;
    }

    public void setVillage_id(String village_id) {
        this.village_id = village_id;
    }

    public String getWard_id() {
        return ward_id;
    }

    public void setWard_id(String ward_id) {
        this.ward_id = ward_id;
    }

    public String getLga_id() {
        return lga_id;
    }

    public void setLga_id(String lga_id) {
        this.lga_id = lga_id;
    }

    public String getSync_status() {
        return sync_status;
    }

    public void setSync_status(String sync_status) {
        this.sync_status = sync_status;
    }

    public String getLast_sync_time() {
        return last_sync_time;
    }

    public void setLast_sync_time(String last_sync_time) {
        this.last_sync_time = last_sync_time;
    }
}
