package com.VanLesh.macsv10.macs;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by samvanryssegem on 2/27/14.
 *Handles singletons of Calculations. Let's us define an array of calcs to store in memory.
 */

public class CalculationLab {

    private static CalculationLab sCalculationLab;
    private Context mAppcontext;
    private ArrayList<Calculation> mCalculations;

    private CalculationLab(Context appcontext){
        mAppcontext = appcontext;
        mCalculations = new ArrayList<Calculation>();
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

    public Calculation getCalculation(UUID id){
        for(Calculation c : mCalculations){
            if(c.getId().equals(id))
                return c;
        }
        return null;
    }
}
