package com.VanLesh.macsv10.macs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by samvanryssegem on 2/27/14.
 */
public class CalculationPagerActivity extends FragmentActivity  implements CalculationFragment.Callbacks{

    private ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        final ArrayList<Calculation> calculations = CalculationLab.get(this).getCalculations();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                UUID calcId = calculations.get(position).getId();
                return CalculationFragment.newInstance(calcId);
            }

            @Override
            public int getCount() {
                return calculations.size();
            }
        });

        UUID crimeId = (UUID)getIntent()
                .getSerializableExtra(CalculationFragment.EXTRA_CALCULATION_ID);
        for(int i=0; i < calculations.size(); i++){
            if(calculations.get(i).getId().equals(crimeId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }

    public void onCalculationUpdated(Calculation crime) {
        // do nothing
    }

    public void onUnitsToggled(View v){

    }
}