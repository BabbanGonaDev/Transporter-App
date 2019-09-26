package com.bgenterprise.transporterapp.Database.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "drivers_table")
public class Drivers {

    @NonNull
    @PrimaryKey
    private String driver_id;
    private String first_name;
    private String last_name;
    private String phone_number;
    private String no_of_vehicles;
    private String training_date;
    private String driver_lga;
    private String driver_ward;
    private String driver_village;
    private String manager_id;
    private String template;
    private String reg_date;

    public Drivers(@NonNull String driver_id, String first_name, String last_name, String phone_number, String no_of_vehicles, String training_date, String driver_lga, String driver_ward, String driver_village, String manager_id, String template, String reg_date) {
        this.driver_id = driver_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_number = phone_number;
        this.no_of_vehicles = no_of_vehicles;
        this.training_date = training_date;
        this.driver_lga = driver_lga;
        this.driver_ward = driver_ward;
        this.driver_village = driver_village;
        this.manager_id = manager_id;
        this.template = template;
        this.reg_date = reg_date;
    }

    @NonNull
    public String getDriver_id() {
        return driver_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getNo_of_vehicles() {
        return no_of_vehicles;
    }

    public String getTraining_date() {
        return training_date;
    }

    public String getDriver_lga() {
        return driver_lga;
    }

    public String getDriver_ward() {
        return driver_ward;
    }

    public String getDriver_village() {
        return driver_village;
    }

    public String getManager_id() {
        return manager_id;
    }

    public String getTemplate() {
        return template;
    }

    public String getReg_date() {
        return reg_date;
    }
}

