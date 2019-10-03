package com.bgenterprise.transporterapp.Database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.bgenterprise.transporterapp.Database.Tables.Drivers;
import com.bgenterprise.transporterapp.Database.Tables.OperatingAreas;
import com.bgenterprise.transporterapp.Database.Tables.Vehicles;
import com.bgenterprise.transporterapp.Network.Responses.DriverResponse;
import com.bgenterprise.transporterapp.Network.Responses.OperatingAreaResponse;
import com.bgenterprise.transporterapp.Network.Responses.VehicleResponse;

import java.util.List;

public class DatabaseApiCalls {

    public static class getAllUnsyncedTransporters extends AsyncTask<Void, Void, List<Drivers>>{
        @SuppressLint("StaticFieldLeak")
        Context mCtx;
        TransporterDatabase transportdb;

        public getAllUnsyncedTransporters(Context mCtx) {
            this.mCtx = mCtx;
        }

        @Override
        protected List<Drivers> doInBackground(Void... voids) {
            try{
                transportdb = TransporterDatabase.getInstance(mCtx);
                return transportdb.getDriverDao().getAllUnsyncedTransporters("no");
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class getAllUnsyncedVehicles extends AsyncTask<Void, Void, List<Vehicles>>{
        @SuppressLint("StaticFieldLeak")
        Context mCtx;
        TransporterDatabase transporterdb;

        public getAllUnsyncedVehicles(Context mCtx) {
            this.mCtx = mCtx;
        }

        @Override
        protected List<Vehicles> doInBackground(Void... voids) {
            try{
                transporterdb = TransporterDatabase.getInstance(mCtx);
                return transporterdb.getVehicleDao().getAllUnsyncedVehicles("no");
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class getAllUnsyncedAreas extends AsyncTask<Void, Void, List<OperatingAreas>>{
        @SuppressLint("StaticFieldLeak")
        Context mCtx;
        TransporterDatabase transporterdb;

        public getAllUnsyncedAreas(Context mCtx) {
            this.mCtx = mCtx;
        }

        @Override
        protected List<OperatingAreas> doInBackground(Void... voids) {
            try{
                transporterdb = TransporterDatabase.getInstance(mCtx);
                return transporterdb.getOperatingAreaDao().getAllUnsyncedAreas("no");
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class updateTransporterSyncStatus extends AsyncTask<List<DriverResponse>, Void, Void>{
        @SuppressLint("StaticFieldLeak")
        Context mCtx;
        TransporterDatabase transportdb;

        public updateTransporterSyncStatus(Context mCtx) {
            this.mCtx = mCtx;
        }

        @Override
        protected Void doInBackground(List<DriverResponse>... lists) {
            try{
                transportdb = TransporterDatabase.getInstance(mCtx);

                List<DriverResponse> syncRes = lists[0];

                /**
                 * Update the driver id on all the tables from the response of the driver syncing function.
                 * Hence in other words, the other syncing functions can not run until the driver guy has run and updated their owner ids.
                 **/
                for(DriverResponse res: syncRes){
                    //Changed this, so to iterate through list and update sync statuses. Also did the same round all.
                    transportdb.getDriverDao().updateSyncStatus(res.getNew_driver_id(), res.getOld_driver_id(), res.getSync_status());
                    transportdb.getVehicleDao().UpdateNewOwnerid(res.getNew_driver_id(), res.getOld_driver_id());
                    transportdb.getOperatingAreaDao().UpdateAreaDriverId(res.getNew_driver_id(), res.getOld_driver_id());

                }
                    /*//Changed this, so to iterate through list and update sync statuses. Also did the same round all.
                    transportdb.getDriverDao().updateSyncStatus(syncRes.get(0).getNew_driver_id(), syncRes.get(0).getOld_driver_id(), syncRes.get(0).getSync_status());
                    transportdb.getVehicleDao().UpdateNewOwnerid(syncRes.get(0).getNew_driver_id(), syncRes.get(0).getOld_driver_id());
                    transportdb.getOperatingAreaDao().UpdateAreaDriverId(syncRes.get(0).getNew_driver_id(), syncRes.get(0).getOld_driver_id());*/

            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class updateVehicleSyncStatus extends AsyncTask<List<VehicleResponse>, Void, Void>{
        @SuppressLint("StaticFieldLeak")
        Context mCtx;
        TransporterDatabase transporterdb;

        public updateVehicleSyncStatus(Context mCtx) {
            this.mCtx = mCtx;
        }

        @Override
        protected Void doInBackground(List<VehicleResponse>... lists) {
            try {
                transporterdb = TransporterDatabase.getInstance(mCtx);

                List<VehicleResponse> vehicleRes = lists[0];
                for(VehicleResponse res: vehicleRes){
                    transporterdb.getVehicleDao().updateSyncStatus(res.getOwner_id(), res.sync_status);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }


    public static class updateOpAreaSyncStatus extends AsyncTask<List<OperatingAreaResponse>, Void, Void>{
        Context mCtx;
        TransporterDatabase transporterdb;

        public updateOpAreaSyncStatus() {
        }

        public updateOpAreaSyncStatus(Context mCtx) {
            this.mCtx = mCtx;
        }

        @Override
        protected Void doInBackground(List<OperatingAreaResponse>... lists) {
            try{
                transporterdb = TransporterDatabase.getInstance(mCtx);

                List<OperatingAreaResponse> areaRes = lists[0];
                for(OperatingAreaResponse res: areaRes){
                    transporterdb.getOperatingAreaDao().updateSyncStatus(res.getOwner_id(), res.getSync_status());
                }

            }catch (Exception e){

            }

            return null;
        }
    }





}
