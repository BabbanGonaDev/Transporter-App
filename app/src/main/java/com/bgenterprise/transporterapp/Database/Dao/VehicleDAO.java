package com.bgenterprise.transporterapp.Database.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bgenterprise.transporterapp.Database.Tables.Vehicles;

import java.util.List;

@Dao
public interface VehicleDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertVehicles(Vehicles vehicles);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertVehiclesFromList(List<Vehicles> vehicles);

    @Update
    void UpdateVehicles(Vehicles... vehicle);

    @Delete
    void DeleteVehicles(Vehicles vehicles);

    @Query("UPDATE vehicles_table SET owner_id = :new_driver_id WHERE owner_id = :old_driver_id")
    void UpdateNewOwnerid(String new_driver_id, String old_driver_id);

    @Query("SELECT * FROM vehicles_table WHERE sync_status = :status")
    List<Vehicles> getAllUnsyncedVehicles(String status);

    @Query("UPDATE vehicles_table SET sync_status = :status WHERE owner_id = :owner_id")
    void updateSyncStatus(String owner_id, String status);

    @Query("SELECT * FROM vehicles_table WHERE owner_id = :driver_id")
    List<Vehicles> getDriversVehicles(String driver_id);
}
