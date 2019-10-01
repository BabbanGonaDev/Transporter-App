package com.bgenterprise.transporterapp.Database.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import com.bgenterprise.transporterapp.Database.Tables.OperatingAreas;

import java.util.List;

@Dao
public interface OperatingAreaDAO {

    @Insert
    void InsertOperatingArea(OperatingAreas operatingArea);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertAreaFromList(List<OperatingAreas> areas);

    @Update
    void UpdateOperatingArea(OperatingAreas operatingAreas);

    @Delete
    void DeleteOperatingArea(OperatingAreas operatingAreas);
}
