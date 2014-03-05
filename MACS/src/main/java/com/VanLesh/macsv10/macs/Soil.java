package com.VanLesh.macsv10.macs;

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