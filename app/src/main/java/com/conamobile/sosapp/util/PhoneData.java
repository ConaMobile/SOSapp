package com.conamobile.sosapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.conamobile.sosapp.constants.KeyConstant;

public class PhoneData {
    private static final String TAG = PhoneData.class.getSimpleName();

    public static void savePhoneData(Context context, String field, String values) {
        try {
            SharedPreferences sp = context.getSharedPreferences(KeyConstant.PHONE_DATA_STR, Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            edit.putString(field, values);
            MyLogger.l(TAG + " saving", field + " = " + values);
            edit.apply();
        } catch (Exception e) {
            MyLogger.e(e);
        }
    }

    public static String getPhoneData(Context context, String field, String defaultValue) {
        try {
            SharedPreferences sp = context.getSharedPreferences(KeyConstant.PHONE_DATA_STR, Context.MODE_PRIVATE);
            MyLogger.l(TAG + " reading", field + " " + sp.getString(field, defaultValue));
            return sp.getString(field, defaultValue);
        } catch (Exception e) {
            MyLogger.e(e);
            return "";
        }
    }

    public static void savePhoneData(Context context, String field, boolean values) {
        savePhoneData(context, field, String.valueOf(values));
    }


    public static boolean getPhoneData(Context context, String field, boolean defaultValue) {
        String value = getPhoneData(context, field, "");

        if (!TextUtils.isEmpty(value)) {
            try {
                defaultValue = Boolean.parseBoolean(value);
            } catch (Exception e) {
            }
        }

        return defaultValue;
    }
}
