package com.VanLesh.macsv10.macs.Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by samvanryssegem on 2/24/14.
 * This class implements all of the logic behind Ben's algorithm.
 * We have some basic metadata for handling preferences and IDing each Calculation object
 * then all of the nitty gritty stuff.
 */
public class Calculation {

    //metadata
    private String mTitle;
    private String mEngineerName;
    private String mJobSite;
    private Date mDate;
    private UUID mId;


    private double latitude;
    private double longitude;


    // Measurements
    private int beta; //angle of slope
    private double D_b; //blade embedment
    private double delta;//figure provided by Ben
    private int theta; //angle on guyline
    private double La; //Setback distance of anchor from soil
    private double Ha; //height of Anchor


    public boolean isimperial = false;
    //Class objects that matter for calculation
    private Soil mSoil;
    private Vehicle mVehicle;


    public double getRollover() {
        return rollover;
    }

    public double getDrag() {
        return drag;
    }

    //final calculation values
    private double rollover;
    private double drag;
    private double Kp;
    private static final double KN_TO_KG = 101.971;
    private static final double KG_TO_KN = 0.00980665;
    private static final double KG_TO_LBS = 2.205;

    //JSON things
    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_DATE = "date";
    private static final String JSON_ENGINEER = "engineer";
    private static final String JSON_SITE = "jobsite";
    private static final String JSON_BETA = "β";
    private static final String JSON_Db = "Db";
    private static final String JSON_DELTA = "δ";
    private static final String JSON_THETA = "θ";
    private static final String JSON_Kp = "Kp";
    private static final String JSON_La = "La";
    private static final String JSON_Ha = "Ha";
    private static final String JSON_VEHICLE = "vehicle";
    private static final String JSON_SOIL = "soil";
    private static final String JSON_LONGITUDE = "longitude";
    private static final String JSON_LATITUDE = "latitude";

    public Calculation() {
        mId = UUID.randomUUID();
        mDate = new Date();
        mVehicle = new Vehicle();
        mSoil = new Soil();
        latitude = 0;
        longitude = 0;

    }

    public Calculation(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        if (json.has(JSON_TITLE))
            mTitle = json.getString(JSON_TITLE);

        if (json.has(JSON_VEHICLE))
            mVehicle = new Vehicle(json.getJSONObject(JSON_VEHICLE));

        if (json.has(JSON_SOIL))
            mSoil = new Soil(json.getJSONObject(JSON_SOIL));

        mDate = new Date(json.getLong(JSON_DATE));
        mEngineerName = json.getString(JSON_ENGINEER);
        mJobSite = json.getString(JSON_SITE);
        mId = UUID.fromString(json.getString(JSON_ID));
        beta = json.getInt(JSON_BETA);
        D_b = json.getDouble(JSON_Db);
        delta = json.getDouble(JSON_DELTA);
        theta = json.getInt(JSON_THETA);
        Kp = json.getDouble(JSON_Db);
        La = json.getDouble(JSON_La);
        Ha = json.getDouble(JSON_Ha);
        if (json.has(JSON_LATITUDE))
            latitude = json.getDouble(JSON_LATITUDE);
        if (json.has(JSON_LONGITUDE))
            longitude = json.getDouble(JSON_LONGITUDE);

    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_DATE, mDate.getTime());
        json.put(JSON_ENGINEER, mEngineerName);
        json.put(JSON_SITE, mJobSite);
        json.put(JSON_BETA, beta);
        json.put(JSON_Db, D_b);
        json.put(JSON_DELTA, delta);
        json.put(JSON_THETA, theta);
        json.put(JSON_Kp, Kp);
        json.put(JSON_La, La);
        json.put(JSON_Ha, Ha);
        if (latitude != 0)
            json.put(JSON_LATITUDE, latitude);
        if (longitude != 0)
            json.put(JSON_LONGITUDE, longitude);
        if (mVehicle != null)
            json.put(JSON_VEHICLE, mVehicle.toJSON());
        if (mSoil != null)
            json.put(JSON_SOIL, mSoil.toJSON());

        return json;

    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getBeta() {
        return beta;
    }

    public void setBeta(int beta) {
        this.beta = beta;
    }

    public double getD_b() {
        return D_b;
    }

    public void setD_b(double d_b) {
        D_b = d_b;
    }

    public int getTheta() {
        return theta;
    }

    public void setTheta(int theta) {
        this.theta = theta;
    }

    public Double getLa() {
        return La;
    }

    public void setLa(Double la) {
        La = la;
    }

    public Double getHa() {
        return Ha;
    }

    public void setHa(Double ha) {
        Ha = ha;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public UUID getId() {
        return mId;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setEngineerName(String engineerName) {
        mEngineerName = engineerName;
    }

    public void setJobSite(String jobSite) {
        mJobSite = jobSite;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getEngineerName() {
        return mEngineerName;
    }

    public String getJobSite() {
        return mJobSite;
    }

    public Soil getSoil() {
        return mSoil;
    }

    public void setSoil(Soil soil) {
        mSoil = soil;
    }

    public Vehicle getVehicle() {
        return mVehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        mVehicle = vehicle;
    }

    private double tsin(int param) {
        return (Math.sin(Math.toRadians(param)));
    }

    private double tcos(double param) {
        return (Math.cos(Math.toRadians(param)));
    }

    double Alpha1() {
        delta = (getSoil().getfrictA()) / 3;

        double top = (Math.cos(Math.toRadians(beta)) - Math.tan(Math.toRadians(delta)) * Math.sin(Math.toRadians(beta)));
        double bot = (Math.sin(Math.toRadians(beta)) + Math.tan(Math.toRadians(delta)) * Math.cos(Math.toRadians(beta)));

        return Math.sin(Math.toRadians(beta)) + Math.cos(Math.toRadians((beta)) * (top / bot));
    }

    double Alpha2() {
        delta = (getSoil().getfrictA()) / 3;
        double top = (Math.cos(Math.toRadians(beta)) - Math.tan(Math.toRadians(delta)) * Math.sin(Math.toRadians(beta)));
        double bot = (Math.sin(Math.toRadians(beta)) + Math.tan(Math.toRadians(delta)) * Math.cos(Math.toRadians(beta)));

        return Math.sin(Math.toRadians(beta)) + Math.sin(Math.toRadians(theta)) * (top / bot);
    }

    public void imperialconversion() {

        if (isimperial) {
            double FEET_TO_METERS = 0.3048;
            D_b = D_b * FEET_TO_METERS;
            La = La * FEET_TO_METERS;
            Ha = Ha * FEET_TO_METERS;
        }
    }

    //equation 8 in the publication
    double Pp() {

        return 0.5 * (getSoil().getunitW() * KG_TO_KN) * Math.pow(D_b, 2) * getVehicle().getBladeW() * Kp
                                                                                            + 2 * getSoil().getC() * getVehicle().getBladeW() * Math.sqrt(Kp);
    }

    //figure 7
    public double AnchorCapacity(boolean isimperial) {
        delta = (getSoil().getfrictA()) / 3;

        // in order to prevent division by zero. Ben knows about this.
        if (beta == 0 && theta == 0) {
            beta = 1;
            theta = 1;
        }

        double alph1 = Alpha1();
        double alph2 = Alpha2();
        Kp = Math.pow(Math.tan(45 * Math.toRadians(getSoil().getfrictA()) / 2), 2);
        if (theta - beta >= delta)
            Kp = 0;
        else if (theta - beta > 0)
            Kp = .5 * Kp;
        else
            Kp = Kp;

        double gamma = getSoil().getunitW() * KG_TO_KN;
        double Wb = getVehicle().getBladeW();
        double Nb = Kp * alph1;
        double c = getSoil().getC();
        double Nc = 2 * Math.sqrt(Kp) * alph1;
        double Nct = alph1 / alph2;
        double Nw = 1 / alph2;
        double At = getVehicle().getTrackA();
        double Wv = getVehicle().getWv() * KG_TO_KN;

        double prelim = 0.5 * gamma * Math.pow(D_b, 2) * Wb * Nb + (c * Wb * Nc) + (c * At * Nct) + (Wv * Nw);
        if (isimperial) {
            drag = prelim * KN_TO_KG * KG_TO_LBS;
            return drag;
        } else {
            drag = prelim * KN_TO_KG;
            return drag;
        }

    }

    //equation 15 in the publication
    public double MomentCalc(boolean isimperial) {

        double Wv = getVehicle().getWv() * KG_TO_KN;

        Vehicle v = getVehicle();
        double top = (Wv * (v.getCg() * tcos(beta) - (D_b + v.getHg()) * tsin(beta)) + Pp() * (D_b / 3));
        double bot = tcos(theta - beta) * (D_b + Ha) + tsin(theta - beta) * La;

        double prelim = (top / bot);
        if (isimperial) {
            rollover = prelim * KN_TO_KG * KG_TO_LBS;
            return rollover;
        } else
            rollover = prelim * KN_TO_KG;
        return rollover;


    }

    @Override
    //over ride default toString to return our title so the user sees something
    //useful when we display a list of calcs
    public String toString() {
        return mTitle;
    }


}
