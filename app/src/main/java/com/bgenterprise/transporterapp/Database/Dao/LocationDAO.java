package com.bgenterprise.transporterapp.Database.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bgenterprise.transporterapp.Database.Tables.Location;

import java.util.List;

@Dao
public interface LocationDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void InsertLocation(List<Location> locationList);

    @Update
    void UpdateLocation(Location location);

    @Delete
    void DeleteLocation(Location location);

    @Query("SELECT DISTINCT state FROM location_table")
    List<String> getStates();

    @Query("SELECT DISTINCT lga FROM location_table WHERE state = :passedState")
    List<String> getLGA(String passedState);

    @Query("SELECT DISTINCT ward FROM location_table WHERE lga = :passedLGA")
    List<String> getWard(String passedLGA);
}
