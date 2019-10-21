package com.bgenterprise.transporterapp.Network.ModelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OperatingAreaResponse {

    @SerializedName("owner_id")
    @Expose
    public String owner_id;

    @SerializedName("sync_status")
    @Expose
    public String sync_status;

    public OperatingAreaResponse() {
    }

    public OperatingAreaResponse(String owner_id, String sync_status) {
        this.owner_id = owner_id;
        this.sync_status = sync_status;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {

        this.owner_id = owner_id;
    }

    public String getSync_status() {
        return sync_status;
    }

    public void setSync_status(String sync_status) {
        this.sync_status = sync_status;
    }
}
