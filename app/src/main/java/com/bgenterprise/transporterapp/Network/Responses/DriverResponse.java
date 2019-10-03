package com.bgenterprise.transporterapp.Network.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverResponse {

    @SerializedName("old_driver_id")
    @Expose
    private String old_driver_id;

    @SerializedName("new_driver_id")
    @Expose
    private String new_driver_id;

    @SerializedName("sync_status")
    @Expose
    private String sync_status;

    public DriverResponse() {
    }

    public DriverResponse(String old_driver_id, String new_driver_id, String sync_status) {
        this.old_driver_id = old_driver_id;
        this.new_driver_id = new_driver_id;
        this.sync_status = sync_status;
    }

    public String getOld_driver_id() {
        return old_driver_id;
    }

    public void setOld_driver_id(String old_driver_id) {
        this.old_driver_id = old_driver_id;
    }

    public String getNew_driver_id() {
        return new_driver_id;
    }

    public void setNew_driver_id(String new_driver_id) {
        this.new_driver_id = new_driver_id;
    }

    public String getSync_status() {
        return sync_status;
    }

    public void setSync_status(String sync_status) {
        this.sync_status = sync_status;
    }
}
