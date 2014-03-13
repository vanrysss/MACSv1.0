package com.VanLesh.macsv10.macs;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


/**
 * Created by samvanryssegem on 2/27/14.
 */
public class CalculationListActivity extends SingleFragmentActivity
        implements CalculationListFragment.Callbacks, CalculationFragment.Callbacks{

    @Override
    protected Fragment createFragment(){
        return new CalculationListFragment();
    }

    protected int getLayoutResId(){
        return R.layout.activity_masterdetail;
    }

    public void onCalculationSelected(Calculation calc){
        if (findViewById(R.id.detailFragmentContainer)==null){
            Intent i= new Intent(this,CalculationPagerActivity.class);
            i.putExtra(CalculationFragment.EXTRA_CALCULATION_ID,calc.getId());
            startActivityForResult(i,0);
        }else {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            Fragment oldDetail = fm.findFragmentById(R.id.detailFragmentContainer);
            Fragment newDetail = CalculationFragment.newInstance(calc.getId());

            if (oldDetail != null) ft.remove(oldDetail);


            ft.add(R.id.detailFragmentContainer, newDetail);
            ft.commit();
        }
    }

    public void onCalculationUpdated(Calculation calc){
        FragmentManager fm= getSupportFragmentManager();
        CalculationListFragment listFragment = (CalculationListFragment)
           fm.findFragmentById(R.id.fragmentContainer);
        listFragment.updateUI();


    }

    public void onVehicleUpdated(Vehicle v){
        FragmentManager fm = getSupportFragmentManager();
        CalculationListFragment listFragment = (CalculationListFragment)
            fm.findFragmentById(R.id.fragmentContainer);
        listFragment.updateUI();
    }

    public void onSoilUpdated(Soil s){
        FragmentManager fm = getSupportFragmentManager();
        CalculationListFragment listFragment = (CalculationListFragment)
                                                                                            fm.findFragmentById(R.id.fragmentContainer);
        listFragment.updateUI();
    }
}



