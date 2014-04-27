package com.VanLesh.macsv10.macs.SplashScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.VanLesh.macsv10.macs.CalculationListActivity;
import com.VanLesh.macsv10.macs.R;

/**
 * Created by samvanryssegem on 4/24/14.
 * Like the title suggests, this just handles the initial "loading" screen.
 * Displays the graphic for 3seconds then hands it off to our main activity, CalculationListActivity.
 */
public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int TIME_OUT = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //this will execute once the timer runs out
                //goes to the CalculationListActivity
                Intent i = new Intent(SplashScreen.this, CalculationListActivity.class);
                startActivity(i);

                finish();
            }
        }, TIME_OUT);
    }


}
