package com.VanLesh.macsv10.macs;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by samvanryssegem on 3/4/14.
 */
// the Soils class just holds info on a soil type. Makes sense to make it a subclass since it's
// only used by the program
public class Soil {



    private String mname; //hey lets uniquely name all of our soil types
    private double munitW; //this is a force aka weight/volume
    private int mfrictA; // the friction angle
    private double mC; //cohesion of soil

    private static final String JSON_NAME="name";
    private static final String JSON_UNITW="unit weight";
    private static final String JSON_FRICTIONANGLE = "friction angle";
    private static final String JSON_COHESION="cohesion";

    public Soil(){

    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_NAME, mname);
        json.put(JSON_UNITW,munitW);
        json.put(JSON_FRICTIONANGLE,mfrictA);
        json.put(JSON_COHESION,mC);
        return json;

    }

    public Soil(JSONObject json) throws JSONException {
        if (json.has(JSON_NAME))
            mname =json.getString(JSON_NAME);
        if (json.has(JSON_UNITW))
            munitW =json.getDouble(JSON_UNITW);
        if (json.has(JSON_FRICTIONANGLE))
            mfrictA = json.getInt(JSON_FRICTIONANGLE);
        if (json.has(JSON_COHESION))
            mC = json.getDouble(JSON_COHESION);
    }
     public int getfrictA() {
        return mfrictA;
    }

    public void setfrictA(int mfrictA) {
        this.mfrictA = mfrictA;
    }


    public String getName() {
        return mname;
    }

    public void setName(String mname) {
        this.mname = mname;
    }

    public double getC() {
        return mC;
    }

    public void setC(double c) {
        mC = c;
    }

    public double getunitW() {
        return munitW;
    }

    public void setunitW(double munitW) {
        this.munitW = munitW;
    }


}