package com.bgenterprise.transporterapp.Database;

import android.content.Context;
import android.os.AsyncTask;

import com.bgenterprise.transporterapp.Database.Tables.Location;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PopulateLocation extends AsyncTask<Void, Void, Void> {

    Context mCtx;
    private TransporterDatabase transportdb;

    public PopulateLocation(Context mCtx) {
        this.mCtx = mCtx;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        transportdb = TransporterDatabase.getInstance(mCtx);
        transportdb.getLocationDao().InsertLocation(readLocationFromCSV());
        return null;
    }

    private List<Location> readLocationFromCSV(){
        List<Location> locations = new ArrayList<>();
        String[] content = null;
        try{
            InputStream inputS = mCtx.getAssets().open("location.csv");
            CSVReader csvReader = new CSVReader(new InputStreamReader(inputS));
            int iteration = 0;
            while((content = csvReader.readNext()) != null){
                if(iteration == 0){
                    iteration++;
                }else{
                    locations.add(new Location(Integer.parseInt(content[0]),
                            content[1],
                            content[2],
                            content[3]));
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return locations;
    }
}
