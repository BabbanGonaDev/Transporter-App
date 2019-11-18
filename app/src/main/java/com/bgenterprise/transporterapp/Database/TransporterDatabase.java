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

@Database(entities = {Drivers.class, Vehicles.class, OperatingAreas.class, Location.class, HSF.class, Payments.class}, version = 3, exportSchema = false)
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
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
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

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //Move current table to a temp table.
            database.execSQL("ALTER TABLE drivers_table RENAME TO drivers_temp");

            //Create table with new structure
            database.execSQL("CREATE TABLE drivers_table (driver_id TEXT NOT NULL, " +
                    "first_name TEXT, " +
                    "last_name TEXT, " +
                    "phone_number TEXT, " +
                    "no_of_vehicles TEXT, " +
                    "training_date TEXT, " +
                    "driver_state TEXT, " +
                    "driver_lga TEXT, " +
                    "driver_ward TEXT, " +
                    "driver_village TEXT, " +
                    "payment_option TEXT, " +
                    "bg_card TEXT, " +
                    "account_number TEXT, " +
                    "account_name TEXT, " +
                    "bank_name TEXT, " +
                    "manager_id TEXT, " +
                    "template TEXT, " +
                    "reg_date TEXT, " +
                    "sync_status TEXT, " +
                    "PRIMARY KEY(driver_id))");

            //Copy content from temp table into the new table.
            database.execSQL("INSERT INTO drivers_table (driver_id, first_name, last_name, phone_number, no_of_vehicles, training_date, driver_state, driver_lga, driver_ward, driver_village, manager_id, template, reg_date, sync_status) " +
                    "SELECT driver_id, first_name, last_name, phone_number, no_of_vehicles, training_date, driver_state, driver_lga, driver_ward, driver_village, manager_id, template, reg_date, sync_status FROM drivers_temp");

            //Drop temp table.
            database.execSQL("DROP TABLE drivers_temp");
        }
    };

    public static void destroyInstance(){
        INSTANCE = null;
    }


}
