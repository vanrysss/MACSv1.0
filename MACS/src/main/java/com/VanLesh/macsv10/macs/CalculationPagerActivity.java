package com.VanLesh.macsv10.macs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by samvanryssegem on 2/27/14.
 */
public class CalculationPagerActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private ArrayList<Calculation> mCalculations;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        mCalculations = CalculationLab.get(this).getCalculations();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Calculation calc = mCalculations.get(position);
                return CalculationFragment.newInstance(calc.getId());
            }

            @Override
            public int getCount() {
                return mCalculations.size();
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            public void onPageScrollStateChanged(int state) {}

            public void onPageScrolled(int pos, float posOffset, int posOffsetPixels){}

            public void onPageSelected(int pos){
                Calculation calc = mCalculations.get(pos);
                if(calc.getTitle()!= null){
                    setTitle(calc.getTitle());
                }
            }
        });

        UUID crimeId = (UUID)getIntent()
                .getSerializableExtra(CalculationFragment.EXTRA_CALCULATION_ID);
        for(int i=0; i < mCalculations.size(); i++){
            if(mCalculations.get(i).getId().equals(crimeId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }
}