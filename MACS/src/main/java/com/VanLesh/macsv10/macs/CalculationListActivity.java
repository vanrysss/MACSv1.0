package com.VanLesh.macsv10.macs;

import android.support.v4.app.Fragment;


/**
 * Created by samvanryssegem on 2/27/14.
 */
public class CalculationListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new CalculationListFragment();
    }
}
