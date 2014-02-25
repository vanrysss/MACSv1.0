package com.VanLesh.macsv10.macs;

/**

 * Created by samvanryssegem on 2/25/14.
 * Simple class that objectifies a vehicle
 */
public class Vehicle {

    private String type;
    private String vehicleclass;

    private int mCg; // distance of vehicle center of gravity from soil
    private int mHg; // height of center of gravity from soil
    private int mWv; // weight of vehicle
    private int mTrackL; //track length
    private int mTrackW; //track width

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

    private int mBladeW; //blade width


}
