package com.bgenterprise.transporterapp.Network;

import com.bgenterprise.transporterapp.Database.Tables.Drivers;
import com.bgenterprise.transporterapp.Network.Responses.DriverResponse;
import com.bgenterprise.transporterapp.Network.Responses.OperatingAreaResponse;
import com.bgenterprise.transporterapp.Network.Responses.VehicleResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitApiCalls {

    @GET("sync_down_drivers.php")
    Call<List<Drivers>> syncDownDrivers(@Query("last_sync_down_time") String last_sync_down_time);

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
