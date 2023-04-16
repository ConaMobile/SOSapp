package com.conamobile.sosapp.util;

import android.util.Log;

import com.conamobile.sosapp.ui.VApp;

public class MyLogger {
    public static void l(String from, String message) {
        if (VApp.appDebug) {
            Log.d(from, message);
        }
    }

    public static void e(Exception e) {
        if (VApp.appDebug) {
            e.printStackTrace();
        }
    }
}
