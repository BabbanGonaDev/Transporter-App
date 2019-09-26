package com.bgenterprise.transporterapp.Database.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.bgenterprise.transporterapp.Database.Tables.OperatingAreas;

@Dao
public interface OperatingAreaDAO {

    @Insert
    void InsertOperatingArea(OperatingAreas operatingArea);

    @Update
    void UpdateOperatingArea(OperatingAreas operatingAreas);

    @Delete
    void DeleteOperatingArea(OperatingAreas operatingAreas);
}
