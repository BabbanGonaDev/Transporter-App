package com.bgenterprise.transporterapp.Database.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bgenterprise.transporterapp.Database.Tables.HSF;

import java.util.List;

@Dao
public interface HSFDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertHSF(HSF hsf);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertHSFList(List<HSF> hsf);

    @Update
    void UpdateHSF(HSF hsf);

    @Delete
    void DeleteHSF(HSF hsf);

    @Query("SELECT SUM(bags_transported * price_per_bag) FROM hsf_table WHERE transporter_id = :driverID")
    int amountEarned(String driverID);

    @Query("SELECT SUM(bags_transported) FROM hsf_table WHERE transporter_id = :driverID")
    int bagsTransported(String driverID);
}
