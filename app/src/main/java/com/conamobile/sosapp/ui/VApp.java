package com.conamobile.sosapp.ui;

import android.app.Application;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.conamobile.sosapp.BuildConfig;

public class VApp extends Application {
    public static DevicePolicyManager devicePolicyManager;
    public static ComponentName mAdminName;

    public static boolean appDebug = BuildConfig.DEBUG;


    @Override
    public void onCreate() {
        super.onCreate();

        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mAdminName = new ComponentName(this, VAdmin.class);
    }

    public static class VAdmin extends DeviceAdminReceiver {
        public VAdmin() {
            super();
        }

        @Override
        public void onEnabled(Context context, Intent intent) {
            super.onEnabled(context, intent);
        }

        @Override
        public void onDisabled(Context context, Intent intent) {
            super.onDisabled(context, intent);
        }
    }
}
