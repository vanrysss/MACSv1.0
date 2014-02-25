package com.VanLesh.macsv10.macs;

/**
 * Created by samvanryssegem on 2/25/14.
 * Lays out the parameters of the soils we're dealing with(sand, concrete, loam, etc)
 */
public class Soil {

    private String mname; //hey lets uniquely name all of our soil types
    private int munitW; //this is a force aka weight/volume
    private int mfrictA; // the friction angle
    private int mC; //cohesion factor of soil

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
