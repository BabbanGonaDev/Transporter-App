package com.bgenterprise.transporterapp.Database.Tables;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "location_table")
public class Location {

    @PrimaryKey
    private int id;
    private String state;
    private String lga;
    private String ward;

    public Location(int id, String state, String lga, String ward) {
        this.id = id;
        this.state = state;
        this.lga = lga;
        this.ward = ward;
    }

    public int getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getLga() {
        return lga;
    }

    public String getWard() {
        return ward;
    }
}
