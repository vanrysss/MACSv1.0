package com.VanLesh.macsv10.macs;

import android.support.v4.app.Fragment;

import java.util.UUID;

public abstract class CalculationActivity extends SingleFragmentActivity {

/*    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }

    }
*/
    @Override
    protected Fragment createFragment(){
        //return new CalculationFragment();
        UUID calculationId = (UUID)getIntent()
                .getSerializableExtra(CalculationFragment.EXTRA_CALCULATION_ID);

        return CalculationFragment.newInstance(calculationId);

    }
}