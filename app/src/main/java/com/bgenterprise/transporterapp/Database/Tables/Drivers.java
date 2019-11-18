package com.bgenterprise.transporterapp.Database.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
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
    private String driver_state;
    private String driver_lga;
    private String driver_ward;
    private String driver_village;
    private String payment_option;
    private String bg_card;
    private String account_number;
    private String account_name;
    private String bank_name;
    private String manager_id;
    private String template;
    private String reg_date;
    private String staff_id;
    private String sync_status;

    //Required Empty Constructor
    @Ignore
    public Drivers() {
    }

    public Drivers(@NonNull String driver_id, String first_name, String last_name, String phone_number, String no_of_vehicles, String training_date, String driver_state, String driver_lga, String driver_ward, String driver_village, String payment_option, String bg_card, String account_number, String account_name, String bank_name, String manager_id, String template, String reg_date, String staff_id, String sync_status) {
        this.driver_id = driver_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_number = phone_number;
        this.no_of_vehicles = no_of_vehicles;
        this.training_date = training_date;
        this.driver_state = driver_state;
        this.driver_lga = driver_lga;
        this.driver_ward = driver_ward;
        this.driver_village = driver_village;
        this.payment_option = payment_option;
        this.bg_card = bg_card;
        this.account_number = account_number;
        this.account_name = account_name;
        this.bank_name = bank_name;
        this.manager_id = manager_id;
        this.template = template;
        this.reg_date = reg_date;
        this.staff_id = staff_id;
        this.sync_status = sync_status;
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

    public String getDriver_state() {
        return driver_state;
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

    public String getSync_status() {
        return sync_status;
    }

    public String getPayment_option() {
        return payment_option;
    }

    public void setPayment_option(String payment_option) {
        this.payment_option = payment_option;
    }

    public String getBg_card() {
        return bg_card;
    }

    public void setBg_card(String bg_card) {
        this.bg_card = bg_card;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public void setDriver_id(@NonNull String driver_id) {
        this.driver_id = driver_id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setNo_of_vehicles(String no_of_vehicles) {
        this.no_of_vehicles = no_of_vehicles;
    }

    public void setTraining_date(String training_date) {
        this.training_date = training_date;
    }

    public void setDriver_state(String driver_state) {
        this.driver_state = driver_state;
    }

    public void setDriver_lga(String driver_lga) {
        this.driver_lga = driver_lga;
    }

    public void setDriver_ward(String driver_ward) {
        this.driver_ward = driver_ward;
    }

    public void setDriver_village(String driver_village) {
        this.driver_village = driver_village;
    }

    public void setManager_id(String manager_id) {
        this.manager_id = manager_id;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public void setSync_status(String sync_status) {
        this.sync_status = sync_status;
    }
}

