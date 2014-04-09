package com.VanLesh.macsv10.macs;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by samvanryssegem on 3/12/14.
 */
public class VehicleLab {

    private static final String TAG = "VehicleLab";
    private static final String FILENAME = "vehicles.json";

    private static VehicleLab sVehicleLab;
    private Context mAppcontext;
    private ArrayList<Vehicle> mVehicles;
    private VehicleJSONSerializer mSerializer;

    private Context mAppContext;

    private VehicleLab(Context appcontext){
        mAppcontext = appcontext;
        mSerializer = new VehicleJSONSerializer(mAppcontext, FILENAME);

        try {
            mVehicles = mSerializer.loadVehicles();
        }catch (Exception e){
            mVehicles = new ArrayList<Vehicle>();
/*
            Vehicle ex1 = new Vehicle();
            ex1.setVehicleClass("Bulldozer");
            ex1.setType("Caterpillar D8");
            ex1.setWv(355* Vehicle.KN_TO_KG);
            ex1.setBladeW(3.255);
            ex1.setCg(3.51);
            ex1.setTrackL(3.255);
            ex1.setTrackW(0.62);

            Vehicle ex2 = new Vehicle();
            ex2.setVehicleClass("Excavator");
            ex2.setType("Caterpillar 320C");
            ex2.setWv(214 * Vehicle.KN_TO_KG);
            ex2.setBladeW(1.55);
            ex2.setCg(7.75);
            ex2.setTrackL(3.72);
            ex2.setTrackW(0.62);

            Vehicle newbie = new Vehicle();
            newbie.setType("new");

            mVehicles.add(ex1);
            mVehicles.add(ex2);
            mVehicles.add(newbie);

            */
            Log.e(TAG, "Error loading vehicles:", e);

        }
    }

    public static VehicleLab get(Context c){
        if(sVehicleLab == null){
            sVehicleLab = new VehicleLab(c.getApplicationContext());
        }
        return sVehicleLab;
    }

    public ArrayList<Vehicle> getVehicles(){
        return mVehicles;
    }

    public void addVehicle(Vehicle c){
        mVehicles.add(c);
    }

    public void deleteVehicle(Vehicle c){
        mVehicles.remove(c);
    }

    public Vehicle getVehicle(String title){
        for(Vehicle c : mVehicles){
            if(c.getVehicleType().equals(title))
                return c;
        }
        return null;
    }

    public boolean saveVehicles(){
        try {
            mSerializer.saveVehicles(mVehicles);
            Log.d(TAG,"vehicles saved");
            return true;
        }catch (Exception e){
            Log.e(TAG,"Error saving",e);
            return false;
        }
    }


}

