package com.bgenterprise.transporterapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context mCtx;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "Transporter Preferences";

    public static final String KEY_IMPORT_LOCATION = "import_location";
    public static final String KEY_DRIVER_DETAILS = "driver_details";
    public static final String KEY_VEHICLE_DETAILS = "vehicle_details";
    public static final String KEY_OPERATING_AREA_DETAILS = "operating_area_details";
    public static final String KEY_DRIVER_ID = "owner_id";
    public static final String KEY_DRIVER_TEMPLATE = "driver_template";
    public static final String KEY_VEHICLE_NO = "vehicle_no";
    public static final String KEY_TOTAL_VEHICLE = "total_vehicle";
    public static final String KEY_REG_TEMPLATE = "registered_template";
    public static final String KEY_LAST_SYNC_DOWN_DRIVER = "last_sync_down_driver";
    public static final String KEY_LAST_SYNC_DOWN_VEHICLE = "last_sync_down_vehicle";
    public static final String KEY_LAST_SYNC_DOWN_AREA = "last_sync_down_area";

    public SessionManager(Context context) {
        this.mCtx = context;
        prefs = mCtx.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = prefs.edit();
    }

    public void SET_DRIVER_DETAILS(String value){
        editor.putString(KEY_DRIVER_DETAILS, value);
        editor.commit();
    }

    public void SET_VEHICLE_DETAILS(String value){
        editor.putString(KEY_VEHICLE_DETAILS, value);
        editor.commit();
    }

    public void SET_OPERATING_AREA_DETAILS(String value){
        editor.putString(KEY_OPERATING_AREA_DETAILS, value);
        editor.commit();
    }

    public void SET_DRIVER_ID(String value){
        editor.putString(KEY_DRIVER_ID, value);
        editor.commit();
    }

    public void SET_DRIVER_TEMPLATE(String value){
        editor.putString(KEY_DRIVER_TEMPLATE, value);
        editor.commit();
    }

    public void SET_VEHICLE_NO(String value){
        editor.putString(KEY_VEHICLE_NO, value);
        editor.commit();
    }

    public void SET_TOTAL_VEHICLE(String value){
        editor.putString(KEY_TOTAL_VEHICLE, value);
        editor.commit();
    }

    public void SET_IMPORT_LOCATION(boolean status){
        editor.putBoolean(KEY_IMPORT_LOCATION, status);
        editor.commit();
    }

    public void SET_REG_TEMPLATE(String value){
        editor.putString(KEY_REG_TEMPLATE, value);
        editor.commit();
    }

    public void SET_LAST_SYNC_DOWN_DRIVER(String value){
        editor.putString(KEY_LAST_SYNC_DOWN_DRIVER, value);
        editor.commit();
    }

    public void SET_LAST_SYNC_DOWN_VEHICLE(String value){
        editor.putString(KEY_LAST_SYNC_DOWN_VEHICLE, value);
        editor.commit();
    }

    public void SET_LAST_SYNC_DOWN_AREA(String value){
        editor.putString(KEY_LAST_SYNC_DOWN_AREA, value);
        editor.commit();
    }

    public Boolean getImportStatus(){
        return prefs.getBoolean(KEY_IMPORT_LOCATION, false);
    }

    public HashMap<String, String> getTransporterDetails(){
        HashMap<String, String> transport = new HashMap<>();

        transport.put(KEY_DRIVER_DETAILS, prefs.getString(KEY_DRIVER_DETAILS, ""));
        transport.put(KEY_VEHICLE_DETAILS, prefs.getString(KEY_VEHICLE_DETAILS, ""));
        transport.put(KEY_OPERATING_AREA_DETAILS, prefs.getString(KEY_OPERATING_AREA_DETAILS, ""));
        transport.put(KEY_DRIVER_ID, prefs.getString(KEY_DRIVER_ID,""));
        transport.put(KEY_DRIVER_TEMPLATE, prefs.getString(KEY_DRIVER_TEMPLATE, ""));
        transport.put(KEY_VEHICLE_NO, prefs.getString(KEY_VEHICLE_NO,"0"));
        transport.put(KEY_TOTAL_VEHICLE, prefs.getString(KEY_TOTAL_VEHICLE, "0"));
        transport.put(KEY_REG_TEMPLATE, prefs.getString(KEY_REG_TEMPLATE, ""));
        transport.put(KEY_LAST_SYNC_DOWN_DRIVER, prefs.getString(KEY_LAST_SYNC_DOWN_DRIVER, "2019-10-04 18:00:00"));
        transport.put(KEY_LAST_SYNC_DOWN_AREA, prefs.getString(KEY_LAST_SYNC_DOWN_AREA, "2019-10-04 18:00:00"));
        transport.put(KEY_LAST_SYNC_DOWN_VEHICLE, prefs.getString(KEY_LAST_SYNC_DOWN_VEHICLE, "2019-10-04 18:00:00"));

        return transport;
    }

    public void CLEAR_REGISTRATION_SESSION(){
        editor.remove(KEY_DRIVER_DETAILS);
        editor.remove(KEY_VEHICLE_DETAILS);
        editor.remove(KEY_OPERATING_AREA_DETAILS);
        editor.remove(KEY_DRIVER_ID);
        editor.remove(KEY_DRIVER_TEMPLATE);
        editor.remove(KEY_VEHICLE_NO);
        editor.remove(KEY_TOTAL_VEHICLE);
        editor.commit();
    }

    public void CLEAR_VEHICLE_DETAILS(){
        editor.remove(KEY_VEHICLE_DETAILS);
        editor.remove(KEY_VEHICLE_NO);
        editor.remove(KEY_TOTAL_VEHICLE);
        editor.commit();
    }
}
