package com.VanLesh.macsv10.macs;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by samvanryssegem on 2/27/14.
 *Handles singletons of Calculations. Let's us define an array of calcs to store in memory.
 */

public class CalculationLab {

    private static final String TAG = "CalculationLab";
    private static final String FILENAME = "calculations.json";

    private static CalculationLab sCalculationLab;
    private ArrayList<Calculation> mCalculations;
    private CalculationJSONSerializer mSerializer;

    private Context mAppContext;

    private CalculationLab(Context appcontext){
        Context appcontext1 = appcontext;
        mSerializer = new CalculationJSONSerializer(appcontext1, FILENAME);

        try {
            mCalculations = mSerializer.loadCalculations();
        }catch (Exception e){
            mCalculations = new ArrayList<Calculation>();

            Log.e(TAG,"Error loading calcs:", e);

        }
    }

    public static CalculationLab get(Context c){
        if(sCalculationLab == null){
            sCalculationLab = new CalculationLab(c.getApplicationContext());
        }
        return sCalculationLab;
    }

    public ArrayList<Calculation> getCalculations(){
        return mCalculations;
    }

    public void addCalculation(Calculation c){
        mCalculations.add(c);
    }

    public void deleteCalculation(Calculation c){
        mCalculations.remove(c);
    }

    public Calculation getCalculation(UUID id){
        for(Calculation c : mCalculations){
            if(c.getId().equals(id))
                return c;
        }
        return null;
    }

    public boolean saveCalculations(){
        try {
            mSerializer.saveCalculations(mCalculations);
            Log.d(TAG,"calculations saved");
            return true;
        }catch (Exception e){
            Log.e(TAG,"Error saving",e);
            return false;
        }
    }


}
