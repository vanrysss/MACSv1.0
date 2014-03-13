package com.VanLesh.macsv10.macs;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by samvanryssegem on 3/4/14.
 */
public class Vehicle {

    private String type;
    private String vehicleclass;

    private int mCg; // distance of vehicle center of gravity from anchor
    private int mHg; // height of center of gravity from soil
    private int mWv; // weight of vehicle
    private int mTrackL; //track length
    private int mTrackW; //track width
    private int mBladeW; //blade width


    private static final String JSON_TYPE ="type";
    private static final String JSON_CLASS="class";
    private static final String JSON_CG ="center of gravity";
    private static final String JSON_HG ="height CG";
    private static final String JSON_WEIGHT ="weight";
    private static final String JSON_TRACKLENGTH ="track length";
    private static final String JSON_TRACKWIDTH="track width";

    public Vehicle(){

    }

    public Vehicle(JSONObject json) throws JSONException {

        type=json.getString(JSON_TYPE);
        vehicleclass=json.getString(JSON_CLASS);
        mCg =json.getInt(JSON_CG);
        mHg=json.getInt(JSON_HG);
        mWv=json.getInt(JSON_WEIGHT);
        mTrackL=json.getInt(JSON_TRACKLENGTH);
        mTrackW=json.getInt(JSON_TRACKWIDTH);


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

        return json;

    }

    public int getTrackA() {
        return mTrackA;
    }

    private int mTrackA = mTrackL*mTrackW; //track area

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCg() {
        return mCg;
    }

    public void setCg(int cg) {
        mCg = cg;
    }

    public int getHg() {
        return mHg;
    }

    public void setHg(int hg) {
        mHg = hg;
    }

    public int getWv() {
        return mWv;
    }

    public void setWv(int wv) {
        mWv = wv;
    }

    public int getTrackL() {
        return mTrackL;
    }

    public void setTrackL(int trackL) {
        mTrackL = trackL;
    }

    public int getTrackW() {
        return mTrackW;
    }

    public void setTrackW(int trackW) {
        mTrackW = trackW;
    }

    public int getBladeW() {
        return mBladeW;
    }

    public void setBladeW(int bladeW) {
        mBladeW = bladeW;
    }


    public String getVehicleclass() {
        return vehicleclass;
    }

    public void setVehicleclass(String vehicleclass) {
        this.vehicleclass = vehicleclass;
    }
}