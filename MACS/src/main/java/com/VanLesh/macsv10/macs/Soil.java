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
    private int munitW; //this is a force aka weight/volume
    private int mfrictA; // the friction angle
    private int mC; //cohesion of soil

    private static final String JSON_NAME="name";
    private static final String JSON_UNITW="unit weight";
    private static final String JSON_FRICTIONANGLE = "friction angle";
    private static final String JSON_COHESION="cohesion";





    public Soil(){
        mname = "defualt";
        munitW =0;
        mfrictA =0;
        mC=0;
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
        mname =json.getString(JSON_NAME);
        munitW =json.getInt(JSON_UNITW);
        mfrictA = json.getInt(JSON_FRICTIONANGLE);
        mC = json.getInt(JSON_COHESION);
    }
     public int getfrictA() {
        return mfrictA;
    }

    public void setfrictA(int mfrictA) {
        this.mfrictA = mfrictA;
    }



    public int getC() {
        return mC;
    }

    public void setC(int c) {
        mC = c;
    }

    public int getunitW() {
        return munitW;
    }

    public void setunitW(int munitW) {
        this.munitW = munitW;
    }


}