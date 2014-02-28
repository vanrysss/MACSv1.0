package com.VanLesh.macsv10.macs;

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

    public boolean doemail;

    //

    private int beta; //angle of slope
    private int sigma; //anchor angle
    private int D_b; //blade embedment
    private int H_a; //height of anchor attachment
    private int delta; //mystery number
    private int theta; //angle on guyline
    private double Kp; //another mystery

    private int La; //Setback distance of anchor from soil
    private int Ha; //height of Anchor


    public Calculation(){
        mId = UUID.randomUUID();
        mDate = new Date();
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
    public double Pp(Vehicle v, Soil s, double Kp){

        return .5*s.getunitW()*Math.pow(D_b,2)*v.getBladeW()*Kp + 2*s.getC()*v.getBladeW()* Math.sqrt(Kp);
    }

    public double anchor_capacity(double A1,double A2, double Pp, int trackA, int weight, int cprime){

        return(((cprime*trackA*A1)/A2) + ((Pp*A1)/A2) + (weight/A2));

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
