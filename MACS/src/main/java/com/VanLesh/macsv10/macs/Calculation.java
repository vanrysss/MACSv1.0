package com.VanLesh.macsv10.macs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by samvanryssegem on 2/24/14.
 * This class implements all of the logic behind Ben's algorithm.
 * We have some basic metadata for handling preferences and IDing each Calculation object
 * then all of the nitty gritty stuff.
 *
 */
public class Calculation {
    //metadata
    private String mTitle;
    private String mEngineerName;
    private String mJobSite;
    private Date mDate;
    private UUID mId;
    public boolean doemail = true;



    // Measurements
    private int beta; //angle of slope
    private int D_b; //blade embedment
    private int delta =1;
    private int theta; //angle on guyline
    private double Kp; //another mystery
    private int La; //Setback distance of anchor from soil
    private int Ha; //height of Anchor


    //Class objects that matter for calculation
    public Soil mSoil;
    public Vehicle mVehicle;

    //final calculation values
    private int rollover;
    private int drag;

    //JSON things
    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_DATE = "date";
    private static final String JSON_ENGINEER = "engineer";
    private static final String JSON_DOEMAIL = "do email";
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

    public Calculation(){
        mId = UUID.randomUUID();
        mDate = new Date();
        mVehicle = new Vehicle();
        mSoil = new Soil();

    }

    //TODO: fill out the rest pls
    public Calculation(JSONObject json) throws JSONException{
        mId = UUID.fromString(json.getString(JSON_ID));
        if (json.has(JSON_TITLE))
            mTitle = json.getString(JSON_TITLE);

        if(json.has(JSON_VEHICLE))
            mVehicle = new Vehicle(json.getJSONObject(JSON_VEHICLE));

        if (json.has(JSON_SOIL))
            mSoil = new Soil(json.getJSONObject(JSON_SOIL));

        mDate = new Date(json.getLong(JSON_DATE));
        mEngineerName = json.getString(JSON_ENGINEER);
        mJobSite = json.getString(JSON_SITE);
        mId = UUID.fromString(json.getString(JSON_ID));
        doemail = json.getBoolean(JSON_DOEMAIL);
        beta = json.getInt(JSON_BETA);
        D_b = json.getInt(JSON_Db);
        delta = json.getInt(JSON_DELTA);
        theta = json.getInt(JSON_THETA);
        Kp = json.getDouble(JSON_Db);
        La = json.getInt(JSON_La);
        Ha = json.getInt(JSON_Ha);

    }
    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_TITLE,mTitle);
        json.put(JSON_DATE,mDate.toString());
        json.put(JSON_ENGINEER,mEngineerName);
        json.put(JSON_SITE,mJobSite);
        json.put(JSON_BETA,beta);
        json.put(JSON_Db, D_b);
        json.put(JSON_DELTA,delta);
        json.put(JSON_THETA,theta);
        json.put(JSON_Kp,Kp);
        json.put(JSON_La,La);
        json.put(JSON_Ha,Ha);
        if (mVehicle != null)
            json.put(JSON_VEHICLE, mVehicle.toJSON());
        if (mSoil != null)
            json.put(JSON_SOIL, mSoil.toJSON());

        return json;

    }

    public void setId(UUID id) {
        mId = id;
    }

    public int getBeta() {
        return beta;
    }

    public void setBeta(int beta) {
        this.beta = beta;
    }

    public int getD_b() {
        return D_b;
    }

    public void setD_b(int d_b) {
        D_b = d_b;
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    public int getTheta() {
        return theta;
    }

    public void setTheta(int theta) {
        this.theta = theta;
    }

    public double getKp() {
        return Kp;
    }

    public void setKp(double kp) {
        Kp = kp;
    }

    public int getLa() {
        return La;
    }

    public void setLa(int la) {
        La = la;
    }

    public int getHa() {
        return Ha;
    }

    public void setHa(int ha) {
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

    public boolean isDoemail() {
        return doemail;
    }

    public void setDoemail(boolean doemail) {
        this.doemail = doemail;
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

    private double tsin(int param){
        return(Math.sin(Math.toRadians(param)));
    }

    private double tcos(int param){
        return(Math.cos(Math.toRadians(param)));
    }

    private double ttan(int param){
        return (Math.tan(Math.toRadians(param)));
    }

    public double Alpha1(){
        double out = (tsin(beta)+tcos(delta));
        double top = (tcos(beta)-ttan(delta)*tsin(beta));
        double bot = (tsin(beta) + ttan(delta)*tcos(beta));

        return (out *(top/bot));
    }

    public double Alpha2(){

        double out = (tsin(theta)+tcos(theta));
        double top = (tcos(beta)-ttan(delta)*tsin(beta));
        double bot = (tsin(beta) + ttan(delta)*tcos(beta));

        return (out *(top/bot));
    }

    //equation 8 in the publication
    public double Pp(Vehicle v, Soil s){
        if (theta - beta >= .333 * delta)
            Kp = 0;
        else if (theta - beta >0)
            Kp = .5 * Kp;
        else
            Kp = Kp;

        return .5*s.getunitW()*Math.pow(D_b,2)*v.getBladeW()*Kp + 2*s.getC()*v.getBladeW()* Math.sqrt(Kp);
    }

    public double anchor_capacity(double A1,double A2, double Pp){

        return(((getSoil().getC()*getVehicle().getTrackA()*A1)/A2) + ((Pp*A1)/A2) + (getVehicle().getWv()/A2));

    }

    //equation 15 in the publication
    public double tip_over_moment(Vehicle v, int Pp){

        double top =(v.getWv()*(v.getCg()*tcos(beta)-(D_b+ v.getHg())*tsin(beta))+Pp*((1/3)*D_b));
        double bot = tcos(theta-beta)*(D_b+Ha) + tsin(theta-beta)*La;

        return top/bot;

    }

    @Override
    //over ride default toString to return our title so the user sees something
    //useful when we display a list of calcs
    public String toString(){
        return mTitle;
    }




}
