package com.VanLesh.macsv10.macs.Models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by samvanryssegem on 3/4/14.
 */
// the Soils class just holds info on a soil type. Makes sense to make it a subclass since it's
// only used by the program
public class Soil {


    private static final String nameJSON = "name";
    private static final String unitWeightJSON = "unit weight";
    private static final String frictionAngleJSON = "friction angle";
    private static final String cohesionJSON = "cohesion";
    public final String name; //hey lets uniquely name all of our soil types
    public final double unitWeight; //this is a force aka weight/volume
    public final int frictionAngle; // the friction angle
    public final double cohesion; //cohesion of soil
    private final double KPa_To_Psf = 0.04788;
    private final double KGm3_To_Pf3 = 77.8555;
    public final boolean isimperial;

    public Soil(boolean isimperial, String name, double unitWeight, int frictionAngle, double cohesion) {
        this.isimperial = isimperial;
        this.name = name;
        this.unitWeight = unitWeight;
        this.frictionAngle = frictionAngle;
        this.cohesion = cohesion;
    }

    public Soil(JSONObject json) throws JSONException {

        if (json.has(nameJSON) && json.has(unitWeightJSON) && json.has(frictionAngleJSON) && json.has(cohesionJSON)) {
            this.name = json.getString(nameJSON);
            this.cohesion = json.getDouble(cohesionJSON);
            this.frictionAngle = json.getInt(frictionAngleJSON);
            this.unitWeight = json.getDouble(unitWeightJSON);
            this.isimperial = false;
        } else throw new JSONException("This soil does not have all fields set properly.");

    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(nameJSON, name);
        json.put(unitWeightJSON, unitWeight);
        json.put(frictionAngleJSON, frictionAngle);
        json.put(cohesionJSON, cohesion);
        return json;

    }

    public Soil(Soil metricSoil) {
        this.cohesion = metricSoil.cohesion * KPa_To_Psf;
        this.unitWeight = metricSoil.unitWeight / KGm3_To_Pf3;
        this.name = metricSoil.name;
        this.frictionAngle = metricSoil.frictionAngle;
        this.isimperial = true;

    }


}