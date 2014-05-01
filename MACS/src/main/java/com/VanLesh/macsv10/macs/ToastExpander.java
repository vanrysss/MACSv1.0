package com.VanLesh.macsv10.macs;

import android.util.Log;
import android.widget.Toast;


/*
ToastExpander class taken from Github. This class lets us show toasts for longer than the
default value of 3.5 seconds.
Web Address for the code::
https://github.com/quiqueqs/Toast-Expander
 */
public class ToastExpander {

    public static final String TAG = "ToastExpander";

    public static void showFor(final Toast aToast, final long durationInMilliseconds) {

        aToast.setDuration(Toast.LENGTH_SHORT);

        Thread t = new Thread() {
            long timeElapsed = 0l;

            public void run() {
                try {
                    while (timeElapsed <= durationInMilliseconds) {
                        long start = System.currentTimeMillis();
                        aToast.show();
                        sleep(1750);
                        timeElapsed += System.currentTimeMillis() - start;
                    }
                } catch (InterruptedException e) {
                    Log.e(TAG, e.toString());
                }
            }
        };
        t.start();
    }
}
