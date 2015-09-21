package com.VanLesh.macsv10.macs;

import android.content.Context;
import android.util.Log;

import com.VanLesh.macsv10.macs.Models.Soil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samvanryssegem on 3/12/14.
 */
public class SoilLab {
    private static final String TAG = "SoilLab";
    private static final String FILENAME = "soils.json";

    private static SoilLab sSoilLab;
    private List<Soil> soils;
    private final SoilJSONSerializer mSerializer;

    private Context mAppContext;

    private SoilLab(Context appcontext) {
        Context appcontext1 = appcontext;
        mSerializer = new SoilJSONSerializer(appcontext1, FILENAME);

        try {
            soils = mSerializer.loadSoils();
        } catch (Exception e) {
            soils = new ArrayList<Soil>();
            Log.e(TAG, "Error loading soils:", e);

        }
    }

    public static SoilLab get(Context c) {
        if (sSoilLab == null) {
            sSoilLab = new SoilLab(c.getApplicationContext());
        }
        return sSoilLab;
    }

    public ArrayList<Soil> getSoils() {
        return soils;
    }

    public void addSoil(Soil c) {
        soils.add(c);
    }

    public void deleteSoil(Soil c) {
        soils.remove(c);
    }

    public Soil getSoil(String title) {
        for (Soil c : soils) {
            if (c.getName().equals(title))
                return c;
        }
        return null;
    }

    public boolean saveSoils() {
        try {
            mSerializer.saveSoils(soils);
            Log.d(TAG, "soils saved");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving", e);
            return false;
        }
    }


}

