package com.bgenterprise.transporterapp.Network;

import com.bgenterprise.transporterapp.Network.ModelClasses.DriverResponse;
import com.bgenterprise.transporterapp.Network.ModelClasses.DriverSyncDown;
import com.bgenterprise.transporterapp.Network.ModelClasses.HSFSyncDown;
import com.bgenterprise.transporterapp.Network.ModelClasses.OperatingAreaResponse;
import com.bgenterprise.transporterapp.Network.ModelClasses.OperatingAreaSyncDown;
import com.bgenterprise.transporterapp.Network.ModelClasses.PaymentSyncDown;
import com.bgenterprise.transporterapp.Network.ModelClasses.VehicleResponse;
import com.bgenterprise.transporterapp.Network.ModelClasses.VehicleSyncDown;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitApiCalls {

    @GET("sync_down_drivers.php")
    Call<List<DriverSyncDown>> syncDownDrivers(@Query("last_sync_down_driver") String last_sync_down_time);

    @GET("sync_down_vehicles.php")
    Call<List<VehicleSyncDown>> syncDownVehicles(@Query("last_sync_down_vehicle") String last_sync_down_time);

    @GET("sync_down_operating_areas.php")
    Call<List<OperatingAreaSyncDown>> syncDownOperatingAreas(@Query("last_sync_down_area") String last_sync_down_time);

    @GET("sync_down_hsf.php")
    Call<List<HSFSyncDown>> syncDownHSF(@Query("last_sync_down_hsf") String last_sync_down_time);

    @GET("sync_down_payments.php")
    Call<List<PaymentSyncDown>> syncDownPayments(@Query("last_sync_down_payment") String last_sync_down_time);

    @FormUrlEncoded
    @POST("sync_up_drivers.php")
    Call<List<DriverResponse>> syncUpDrivers(@Field("driver_list") String driver_list);

    @FormUrlEncoded
    @POST("sync_up_vehicles.php")
    Call<List<VehicleResponse>> syncUpVehicles(@Field("vehicle_list") String vehicle_list);

    @FormUrlEncoded
    @POST("sync_up_operating_areas.php")
    Call<List<OperatingAreaResponse>> syncUpAreas(@Field("areas_list") String areas_list);

}
