package com.bgenterprise.transporterapp.Database.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bgenterprise.transporterapp.Database.Tables.Payments;

import java.util.List;

@Dao
public interface PaymentsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertPayments(Payments payments);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertPaymentsList(List<Payments> payments);

    @Update
    void UpdatePayments(Payments payments);

    @Delete
    void DeletePayments(Payments payments);

    @Query("SELECT SUM(amount_paid) FROM payments_table WHERE transporter_id = :driverID")
    int amountPaid(String driverID);

    @Query("SELECT * FROM payments_table WHERE transporter_id = :driverID")
    List<Payments> getTransporterPayments(String driverID);
}
