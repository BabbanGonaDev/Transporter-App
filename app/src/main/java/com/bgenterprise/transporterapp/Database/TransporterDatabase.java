package com.bgenterprise.transporterapp.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.bgenterprise.transporterapp.Database.Dao.DriverDAO;
import com.bgenterprise.transporterapp.Database.Dao.HSFDAO;
import com.bgenterprise.transporterapp.Database.Dao.LocationDAO;
import com.bgenterprise.transporterapp.Database.Dao.OperatingAreaDAO;
import com.bgenterprise.transporterapp.Database.Dao.PaymentsDAO;
import com.bgenterprise.transporterapp.Database.Dao.VehicleDAO;
import com.bgenterprise.transporterapp.Database.Tables.Drivers;
import com.bgenterprise.transporterapp.Database.Tables.HSF;
import com.bgenterprise.transporterapp.Database.Tables.Location;
import com.bgenterprise.transporterapp.Database.Tables.OperatingAreas;
import com.bgenterprise.transporterapp.Database.Tables.Payments;
import com.bgenterprise.transporterapp.Database.Tables.Vehicles;

@Database(entities = {Drivers.class, Vehicles.class, OperatingAreas.class, Location.class, HSF.class, Payments.class}, version = 2, exportSchema = false)
public abstract class TransporterDatabase extends RoomDatabase {
    private static TransporterDatabase INSTANCE;

    //Table entity classes.
    public abstract VehicleDAO getVehicleDao();
    public abstract DriverDAO getDriverDao();
    public abstract OperatingAreaDAO getOperatingAreaDao();
    public abstract LocationDAO getLocationDao();
    public abstract HSFDAO getHsfDao();
    public abstract PaymentsDAO getPaymentsDao();

    //Initialization of database instance.
    public static TransporterDatabase getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    TransporterDatabase.class,
                    "transporter-db")
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }
        return INSTANCE;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //Create HSF table.
            database.execSQL("CREATE TABLE hsf_table (hsf_id TEXT NOT NULL, " +
                    "field_id TEXT, " +
                    "bags_transported TEXT, " +
                    "price_per_bag TEXT, " +
                    "transporter_id TEXT, " +
                    "cc_id TEXT, " +
                    "date_processed TEXT, " +
                    "PRIMARY KEY(hsf_id))");


            //Create payments table.
            database.execSQL("CREATE TABLE payments_table (payment_id TEXT NOT NULL, " +
                    "transporter_id TEXT, " +
                    "amount_paid TEXT, " +
                    "mode_of_payment TEXT, " +
                    "payment_date TEXT, " +
                    "PRIMARY KEY(payment_id))");
        }
    };

    public static void destroyInstance(){
        INSTANCE = null;
    }


}
