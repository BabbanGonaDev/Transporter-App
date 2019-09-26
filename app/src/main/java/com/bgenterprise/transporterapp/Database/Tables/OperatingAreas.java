package com.bgenterprise.transporterapp.Database.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity (tableName = "operating_areas_table", primaryKeys = {"driver_id", "village_id", "ward_id", "lga_id"})
public class OperatingAreas {

    @NonNull
    private String driver_id;
    @NonNull
    private String village_id;
    @NonNull
    private String ward_id;
    @NonNull
    private String lga_id;

    public OperatingAreas(@NonNull String driver_id, @NonNull String village_id, @NonNull String ward_id, @NonNull String lga_id) {
        this.driver_id = driver_id;
        this.village_id = village_id;
        this.ward_id = ward_id;
        this.lga_id = lga_id;
    }

    @NonNull
    public String getDriver_id() {
        return driver_id;
    }

    @NonNull
    public String getVillage_id() {
        return village_id;
    }

    @NonNull
    public String getWard_id() {
        return ward_id;
    }

    @NonNull
    public String getLga_id() {
        return lga_id;
    }
}
