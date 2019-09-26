package com.bgenterprise.transporterapp.Database.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import com.bgenterprise.transporterapp.Database.Tables.Vehicles;

@Dao
public interface VehicleDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void InsertVehicles(Vehicles... vehicles);

    @Update
    void UpdateVehicles(Vehicles... vehicle);

    @Delete
    void DeleteVehicles(Vehicles vehicles);
}
