package com.VanLesh.macsv10.macs;

import android.support.v4.app.Fragment;

import java.util.UUID;

public abstract class CalculationActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        //return new CalculationFragment();
        UUID calculationId = (UUID)getIntent()
                .getSerializableExtra(CalculationFragment.EXTRA_CALCULATION_ID);
            //    .getSerializableExtra(CalculationFragment.EXTRA_VEHICLE_ID);

        return CalculationFragment.newInstance(calculationId);

    }
}