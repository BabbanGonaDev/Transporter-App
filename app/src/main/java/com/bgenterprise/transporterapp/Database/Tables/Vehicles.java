package com.bgenterprise.transporterapp.Database.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vehicles_table")
public class Vehicles {

    @NonNull
    @PrimaryKey
    private String vehicle_id;
    private String vehicle_plate_no;
    private String vehicle_type;
    private String vehicle_condition;
    private String livestock_status;
    private String vehicle_capacity;
    private String vehicle_lga;
    private String vehicle_ward;
    private String vehicle_village;
    private String owner_id;

    public Vehicles(@NonNull String vehicle_id, String vehicle_plate_no, String vehicle_type, String vehicle_condition, String livestock_status, String vehicle_capacity, String vehicle_lga, String vehicle_ward, String vehicle_village, String owner_id) {
        this.vehicle_id = vehicle_id;
        this.vehicle_plate_no = vehicle_plate_no;
        this.vehicle_type = vehicle_type;
        this.vehicle_condition = vehicle_condition;
        this.livestock_status = livestock_status;
        this.vehicle_capacity = vehicle_capacity;
        this.vehicle_lga = vehicle_lga;
        this.vehicle_ward = vehicle_ward;
        this.vehicle_village = vehicle_village;
        this.owner_id = owner_id;
    }

    @NonNull
    public String getVehicle_id() {
        return vehicle_id;
    }

    public String getVehicle_plate_no() {
        return vehicle_plate_no;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public String getVehicle_condition() {
        return vehicle_condition;
    }

    public String getLivestock_status() {
        return livestock_status;
    }

    public String getVehicle_capacity() {
        return vehicle_capacity;
    }

    public String getVehicle_lga() {
        return vehicle_lga;
    }

    public String getVehicle_ward() {
        return vehicle_ward;
    }

    public String getVehicle_village() {
        return vehicle_village;
    }

    public String getOwner_id() {
        return owner_id;
    }
}
