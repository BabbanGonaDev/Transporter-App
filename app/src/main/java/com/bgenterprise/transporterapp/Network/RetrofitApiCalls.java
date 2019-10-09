package com.bgenterprise.transporterapp.Network;

import com.bgenterprise.transporterapp.Network.Responses.DriverResponse;
import com.bgenterprise.transporterapp.Network.Responses.DriverSync;
import com.bgenterprise.transporterapp.Network.Responses.OperatingAreaResponse;
import com.bgenterprise.transporterapp.Network.Responses.OperatingAreaSync;
import com.bgenterprise.transporterapp.Network.Responses.VehicleResponse;
import com.bgenterprise.transporterapp.Network.Responses.VehicleSync;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitApiCalls {

    @GET("sync_down_drivers.php")
    Call<List<DriverSync>> syncDownDrivers(@Query("last_sync_down_driver") String last_sync_down_time);

    @GET("sync_down_vehicles.php")
    Call<List<VehicleSync>> syncDownVehicles(@Query("last_sync_down_vehicle") String last_sync_down_time);

    @GET("sync_down_operating_areas.php")
    Call<List<OperatingAreaSync>> syncDownOperatingAreas(@Query("last_sync_down_area") String last_sync_down_time);

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
