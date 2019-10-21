package com.bgenterprise.transporterapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

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

@Database(entities = {Drivers.class, Vehicles.class, OperatingAreas.class, Location.class, HSF.class, Payments.class}, version = 1, exportSchema = false)
public abstract class TransporterDatabase extends RoomDatabase {
    private static TransporterDatabase INSTANCE;

    //Table entity classes.
    public abstract VehicleDAO getVehicleDao();
    public abstract DriverDAO getDriverDao();
    public abstract OperatingAreaDAO getOperatingAreaDao();
    public abstract LocationDAO getLocationDao();
    public abstract HSFDAO getHsfDao();
    public abstract PaymentsDAO getPaymentsDao();

    //TODO---> Don't forget to add migrations. And the fix to reset the shared-pref of the vehicles.

    //Initialization of database instance.
    public static TransporterDatabase getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    TransporterDatabase.class,
                    "transporter-db")
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance(){
        INSTANCE = null;
    }
}
