package com.bgenterprise.transporterapp.Network.ModelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleSyncDown {

    @SerializedName("vehicle_id")
    @Expose
    private String vehicle_id;

    @SerializedName("vehicle_plate_no")
    @Expose
    private String vehicle_plate_no;

    @SerializedName("vehicle_type")
    @Expose
    private String vehicle_type;

    @SerializedName("vehicle_condition")
    @Expose
    private String vehicle_condition;

    @SerializedName("livestock_status")
    @Expose
    private String livestock_status;

    @SerializedName("vehicle_capacity")
    @Expose
    private String vehicle_capacity;

    @SerializedName("vehicle_state")
    @Expose
    private String vehicle_state;

    @SerializedName("vehicle_lga")
    @Expose
    private String vehicle_lga;

    @SerializedName("vehicle_ward")
    @Expose
    private String vehicle_ward;

    @SerializedName("vehicle_village")
    @Expose
    private String vehicle_village;

    @SerializedName("owner_id")
    @Expose
    private String owner_id;

    @SerializedName("sync_status")
    @Expose
    private String sync_status;

    @SerializedName("last_sync_time")
    @Expose
    private String last_sync_time;

    public VehicleSyncDown() {}

    public VehicleSyncDown(String vehicle_id, String vehicle_plate_no, String vehicle_type, String vehicle_condition, String livestock_status, String vehicle_capacity, String vehicle_state, String vehicle_lga, String vehicle_ward, String vehicle_village, String owner_id, String sync_status, String last_sync_time) {
        this.vehicle_id = vehicle_id;
        this.vehicle_plate_no = vehicle_plate_no;
        this.vehicle_type = vehicle_type;
        this.vehicle_condition = vehicle_condition;
        this.livestock_status = livestock_status;
        this.vehicle_capacity = vehicle_capacity;
        this.vehicle_state = vehicle_state;
        this.vehicle_lga = vehicle_lga;
        this.vehicle_ward = vehicle_ward;
        this.vehicle_village = vehicle_village;
        this.owner_id = owner_id;
        this.sync_status = sync_status;
        this.last_sync_time = last_sync_time;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getVehicle_plate_no() {
        return vehicle_plate_no;
    }

    public void setVehicle_plate_no(String vehicle_plate_no) {
        this.vehicle_plate_no = vehicle_plate_no;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getVehicle_condition() {
        return vehicle_condition;
    }

    public void setVehicle_condition(String vehicle_condition) {
        this.vehicle_condition = vehicle_condition;
    }

    public String getLivestock_status() {
        return livestock_status;
    }

    public void setLivestock_status(String livestock_status) {
        this.livestock_status = livestock_status;
    }

    public String getVehicle_capacity() {
        return vehicle_capacity;
    }

    public void setVehicle_capacity(String vehicle_capacity) {
        this.vehicle_capacity = vehicle_capacity;
    }

    public String getVehicle_state() {
        return vehicle_state;
    }

    public void setVehicle_state(String vehicle_state) {
        this.vehicle_state = vehicle_state;
    }

    public String getVehicle_lga() {
        return vehicle_lga;
    }

    public void setVehicle_lga(String vehicle_lga) {
        this.vehicle_lga = vehicle_lga;
    }

    public String getVehicle_ward() {
        return vehicle_ward;
    }

    public void setVehicle_ward(String vehicle_ward) {
        this.vehicle_ward = vehicle_ward;
    }

    public String getVehicle_village() {
        return vehicle_village;
    }

    public void setVehicle_village(String vehicle_village) {
        this.vehicle_village = vehicle_village;
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

    public String getLast_sync_time() {
        return last_sync_time;
    }

    public void setLast_sync_time(String last_sync_time) {
        this.last_sync_time = last_sync_time;
    }
}
