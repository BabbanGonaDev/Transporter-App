package com.bgenterprise.transporterapp.Database.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity (tableName = "operating_areas_table", primaryKeys = {"owner_id", "village_id", "ward_id", "lga_id"})
public class OperatingAreas {

    @NonNull
    private String owner_id;
    private String state_id;
    @NonNull
    private String village_id;
    @NonNull
    private String ward_id;
    @NonNull
    private String lga_id;
    private String sync_status;

    //Required empty constructor
    @Ignore
    public OperatingAreas() {
    }

    public OperatingAreas(@NonNull String owner_id, String state_id, @NonNull String lga_id, @NonNull String ward_id, @NonNull String village_id, String sync_status) {
        this.owner_id = owner_id;
        this.state_id = state_id;
        this.lga_id = lga_id;
        this.ward_id = ward_id;
        this.village_id = village_id;
        this.sync_status = sync_status;
    }

    @NonNull
    public String getOwner_id() {
        return owner_id;
    }

    public String getState_id() {
        return state_id;
    }

    @NonNull
    public String getLga_id() {
        return lga_id;
    }

    @NonNull
    public String getWard_id() {
        return ward_id;
    }

    @NonNull
    public String getVillage_id() {
        return village_id;
    }

    public String getSync_status() {
        return sync_status;
    }

    public void setOwner_id(@NonNull String driver_id) {
        this.owner_id = owner_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public void setVillage_id(@NonNull String village_id) {
        this.village_id = village_id;
    }

    public void setWard_id(@NonNull String ward_id) {
        this.ward_id = ward_id;
    }

    public void setLga_id(@NonNull String lga_id) {
        this.lga_id = lga_id;
    }

    public void setSync_status(String sync_status) {
        this.sync_status = sync_status;
    }
}
