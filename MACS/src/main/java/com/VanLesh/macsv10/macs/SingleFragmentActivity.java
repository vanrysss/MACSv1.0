package com.VanLesh.macsv10.macs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by samvanryssegem on 2/27/14.
 * sets view to be inflated from activity_fragment.xml then looks for a fragment in
 * FragmentManager, creates and adds if it doesnt exist
 */

public abstract class SingleFragmentActivity extends FragmentActivity {

    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
}
