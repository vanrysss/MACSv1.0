package com.VanLesh.macsv10.macs;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by samvanryssegem on 3/4/14.
 */
public class Vehicle {

    private String type;
    private String vehicleclass;

    private double mCg; // distance of vehicle center of gravity from anchor
    private double mHg; // height of center of gravity from soil
    private double mWv; // weight of vehicle
    private double mTrackL; //track length
    private double mTrackW; //track width
    private double mBladeW; //blade width
    private double TrackA = mTrackL*mTrackW;

    private static final String JSON_TYPE ="type";
    private static final String JSON_CLASS="class";
    private static final String JSON_CG ="center of gravity";
    private static final String JSON_HG ="height CG";
    private static final String JSON_WEIGHT ="weight";
    private static final String JSON_TRACKLENGTH ="track length";
    private static final String JSON_TRACKWIDTH="track width";
    private static final String JSON_BLADEWIDTH="blade width";

    public Vehicle(){

    }

    public Vehicle(JSONObject json) throws JSONException {

        if(json.has(JSON_TYPE))
             type=json.getString(JSON_TYPE);
        if (json.has(JSON_CLASS))
            vehicleclass=json.getString(JSON_CLASS);
        if (json.has(JSON_CG))
             mCg =json.getDouble(JSON_CG);
        if (json.has(JSON_HG))
             mHg=json.getDouble(JSON_HG);
        if (json.has(JSON_WEIGHT))
             mWv=json.getDouble(JSON_WEIGHT);
        if (json.has(JSON_TRACKLENGTH))
            mTrackL=json.getDouble(JSON_TRACKLENGTH);
        if (json.has(JSON_TRACKWIDTH))
            mTrackW=json.getDouble(JSON_TRACKWIDTH);
        if (json.has(JSON_BLADEWIDTH))
            mBladeW=json.getDouble(JSON_BLADEWIDTH);

    }
    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();

        json.put(JSON_TYPE,type);
        json.put(JSON_CLASS,vehicleclass);
        json.put(JSON_CG,mCg);
        json.put(JSON_HG,mHg);
        json.put(JSON_WEIGHT,mWv);
        json.put(JSON_TRACKLENGTH,mTrackL);
        json.put(JSON_TRACKWIDTH,mTrackW);
        json.put(JSON_BLADEWIDTH,mBladeW);

        return json;

    }

    public double getTrackA() {
        return mTrackA;
    }

    private double mTrackA = mTrackL*mTrackW; //track area

    public String getVehicleType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getCg() {
        return mCg;
    }

    public void setCg(double cg) {
        mCg = cg;
    }

    public double getHg() {
        return mHg;
    }

    public void setHg(double hg) {
        mHg = hg;
    }

    public double getWv() {
        return mWv;
    }

    public void setWv(Double wv) {
        mWv = wv;
    }

    public double getTrackL() {
        return mTrackL;
    }

    public void setTrackL(double trackL) {
        mTrackL = trackL;
    }

    public double getTrackW() {
        return mTrackW;
    }

    public void setTrackW(double trackW) {
        mTrackW = trackW;
    }

    public double getBladeW() {
        return mBladeW;
    }

    public void setBladeW(double bladeW) {
        mBladeW = bladeW;
    }


    public String getVehicleClass() {
        return vehicleclass;
    }

    public void setVehicleClass(String vehicleclass) {
        this.vehicleclass = vehicleclass;
    }
}