package com.VanLesh.macsv10.macs;

import android.content.Context;
import android.util.Log;

import com.VanLesh.macsv10.macs.Models.Vehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samvanryssegem on 3/12/14.
 */
public class VehicleLab {

    private static final String TAG = "VehicleLab";
    private static final String FILENAME = "vehicles.json";

    private static VehicleLab sVehicleLab;
    private List<Vehicle> mVehicles;

    private Context mAppContext;

    private VehicleLab(Context appcontext) {
        Context appcontext1 = appcontext;

        try {
            mVehicles = mSerializer.loadVehicles();
        } catch (Exception e) {
            mVehicles = new ArrayList<Vehicle>();
            Log.e(TAG, "Error loading vehicles:", e);

        }
    }

    public static VehicleLab get(Context c) {
        if (sVehicleLab == null) {
            sVehicleLab = new VehicleLab(c.getApplicationContext());
        }
        return sVehicleLab;
    }

    public ArrayList<Vehicle> getVehicles() {
        return mVehicles;
    }

    public void addVehicle(Vehicle c) {
        mVehicles.add(c);
    }

    public void deleteVehicle(Vehicle c) {
        mVehicles.remove(c);
    }

    public Vehicle getVehicle(String title) {
        for (Vehicle c : mVehicles) {
            if (c.getVehicleType().equals(title))
                return c;
        }
        return null;
    }

    public boolean saveVehicles() {
        try {
            mSerializer.saveVehicles(mVehicles);
            Log.d(TAG, "vehicles saved");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving", e);
            return false;
        }
    }


}

