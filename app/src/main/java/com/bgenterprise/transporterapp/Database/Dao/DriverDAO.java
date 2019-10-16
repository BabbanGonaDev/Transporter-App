package com.bgenterprise.transporterapp.Database.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bgenterprise.transporterapp.Database.Tables.Drivers;

import java.util.List;

@Dao
public interface DriverDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertDrivers(Drivers drivers);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertDriverFromList(List<Drivers> list);

    @Update
    void UpdateDrivers(Drivers drivers);

    @Delete
    void DeleteDrivers(Drivers drivers);

    @Query("SELECT * FROM drivers_table WHERE sync_status = :status")
    List<Drivers> getAllUnsyncedTransporters(String status);

    @Query("UPDATE drivers_table SET driver_id = :new_driver_id, sync_status = :sync_status WHERE driver_id = :old_driver_id")
    int updateSyncStatus(String new_driver_id, String old_driver_id, String sync_status);

    @Query("UPDATE drivers_table SET manager_id = :new_driver_id WHERE manager_id = :old_driver_id")
    void updateManagerID(String new_driver_id, String old_driver_id);

    @Query("SELECT * FROM drivers_table ORDER BY reg_date DESC")
    List<Drivers> getAllDrivers();

    @Query("SELECT * FROM drivers_table WHERE driver_id = :driver_id")
    Drivers getDriverDetails(String driver_id);
}
