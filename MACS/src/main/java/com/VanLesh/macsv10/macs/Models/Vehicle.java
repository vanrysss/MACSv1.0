package com.VanLesh.macsv10.macs.Models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by samvanryssegem on 3/4/14.
 */
public class Vehicle {

    public static Double KN_TO_KG = 10.109776;
    private String type;
    private String vehicleclass;

    private double centerGrav; // distance of vehicle center of gravity from anchor
    private double centerGravHeight; // height of center of gravity from soil
    private double vehicleWidth; // weight of vehicle
    private double trackLength; //track length
    private double trackWidth; //track width
    private double bladeWidth; //blade width
    private double trackArea = trackLength * trackWidth;

    public boolean isimperial = false;

    public void convertToMetric() {
        if (isimperial) {
            double FEET_TO_METERS = 0.3048;
            trackLength = trackLength * FEET_TO_METERS;
            trackWidth = trackWidth * FEET_TO_METERS;
            mTrackA = mTrackA * FEET_TO_METERS;
            bladeWidth = bladeWidth * FEET_TO_METERS;
            centerGravHeight = centerGravHeight * FEET_TO_METERS;
            double LBS_TO_KG = 0.453592;
            vehicleWidth = vehicleWidth * LBS_TO_KG;
            centerGrav = centerGrav * FEET_TO_METERS;
        }
    }

    public double getTrackArea() {
        return mTrackA;
    }

    private double mTrackA = trackLength * trackWidth; //track area

    public String getVehicleType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getCg() {
        return centerGrav;
    }

    public void setCg(double cg) {
        centerGrav = cg;
    }

    public double getHg() {
        return centerGravHeight;
    }

    public void setHg(double hg) {
        centerGravHeight = hg;
    }

    public double getWv() {
        return vehicleWidth;
    }

    public void setWv(Double wv) {
        vehicleWidth = wv;
    }

    public double getTrackL() {
        return trackLength;
    }

    public void setTrackL(double trackL) {
        trackLength = trackL;
    }

    public double getTrackW() {
        return trackWidth;
    }

    public void setTrackW(double trackW) {
        trackWidth = trackW;
    }

    public double getBladeW() {
        return bladeWidth;
    }

    public void setBladeW(double bladeW) {
        bladeWidth = bladeW;
    }


    public String getVehicleClass() {
        return vehicleclass;
    }

    public void setVehicleClass(String vehicleclass) {
        this.vehicleclass = vehicleclass;
    }
}