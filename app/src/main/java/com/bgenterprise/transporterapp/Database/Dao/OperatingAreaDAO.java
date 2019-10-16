package com.bgenterprise.transporterapp.Database.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bgenterprise.transporterapp.Database.Tables.OperatingAreas;

import java.util.List;

@Dao
public interface OperatingAreaDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertOperatingArea(OperatingAreas operatingArea);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertAreaFromList(List<OperatingAreas> areas);

    @Update
    void UpdateOperatingArea(OperatingAreas operatingAreas);

    @Delete
    void DeleteOperatingArea(OperatingAreas operatingAreas);

    @Query("UPDATE operating_areas_table SET owner_id = :new_driver_id WHERE owner_id = :old_driver_id")
    void UpdateAreaDriverId(String new_driver_id, String old_driver_id);

    @Query("SELECT * FROM operating_areas_table WHERE sync_status = :status")
    List<OperatingAreas> getAllUnsyncedAreas(String status);

    @Query("UPDATE operating_areas_table SET sync_status = :status WHERE owner_id = :owner_id")
    void updateSyncStatus(String owner_id, String status);

    @Query("SELECT * FROM operating_areas_table WHERE owner_id = :driverID")
    List<OperatingAreas> getDriversAreas(String driverID);
}
