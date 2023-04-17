package com.conamobile.sosapp.util;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.conamobile.sosapp.ui.VApp;

public class DeviceAdminUtil {
    private static final String TAG = DeviceAdminUtil.class.getSimpleName();


    public static boolean checkisDeviceAdminEnabled() {
        if (VApp.devicePolicyManager != null && VApp.mAdminName != null) {
            if (VApp.devicePolicyManager.isAdminActive(VApp.mAdminName)) {
                MyLogger.l("@@@", "Permision is enabled");
                return true;
            } else {
                MyLogger.l(TAG, "No admin permision");
            }
        } else {
            MyLogger.l(TAG, "device managet is null");
        }

        return false;
    }

    public static void openDeviceManagerEnableAction(Activity activity, int requestCode) {
        if (!checkisDeviceAdminEnabled()) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, VApp.mAdminName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "We need this permission to lock your phone.");
            activity.startActivityForResult(intent, requestCode);
        } else {
            //Not safe,, May vary depends on ROM
            try {
                final Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.DeviceAdminAdd"));
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, VApp.mAdminName);
                activity.startActivity(intent);
            } catch (Exception e) {
                MyLogger.e(e);
            }
        }
    }

    public static void removeAdminAndUninstall(Context context)
    {
        if(VApp.devicePolicyManager != null && VApp.mAdminName != null)
        {
            if (VApp.devicePolicyManager.isAdminActive(VApp.mAdminName))
            {
                VApp.devicePolicyManager.removeActiveAdmin(VApp.mAdminName);
            }
        }

        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
        //intentionally dont direclty uninstall
        /*String packageName = context.getPackageName();
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        context.startActivity(intent);*/
    }
}
