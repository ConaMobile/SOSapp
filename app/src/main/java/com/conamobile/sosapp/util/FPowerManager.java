package com.conamobile.sosapp.util;

import android.content.Context;
import android.os.PowerManager;

public class FPowerManager {

    private static final String TAG = FPowerManager.class.getSimpleName();

    private static PowerManager pm;
    private static FPowerManager fPowerManager;

    public static FPowerManager instance(Context context) {
        if (pm == null) {
            pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

            MyLogger.l(TAG, "power manager is initialized");
        }

        if (fPowerManager == null) {
            fPowerManager = new FPowerManager();
        }

        return fPowerManager;
    }

    public boolean isScreenOn() {
        boolean isScreenOn;
        isScreenOn = pm.isInteractive();

        return isScreenOn;
    }
}
