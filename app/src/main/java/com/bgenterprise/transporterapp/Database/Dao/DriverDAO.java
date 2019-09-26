package com.bgenterprise.transporterapp.Database.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import com.bgenterprise.transporterapp.Database.Tables.Drivers;

@Dao
public interface DriverDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertDrivers(Drivers... drivers);

    @Update
    void UpdateDrivers(Drivers drivers);

    @Delete
    void DeleteDrivers(Drivers drivers);
}
