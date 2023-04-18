package com.conamobile.sosapp.pref

import android.content.Context

class SharedPreferences(context: Context) {
    private val pref = context.getSharedPreferences("key", Context.MODE_PRIVATE)

    fun isLogin(isLogin: Boolean) {
        val editor = pref.edit()
        editor.putBoolean("isLogin", isLogin)
        editor.apply()
    }

    fun isLogin(): Boolean {
        return pref.getBoolean("isLogin", false)
    }

    fun isName(isName: String) {
        val editor = pref.edit()
        editor.putString("isName", isName)
        editor.apply()
    }

    fun isName(): String? {
        return pref.getString("isName", null)
    }

    fun isPhone(isPhone: String) {
        val editor = pref.edit()
        editor.putString("isPhone", isPhone)
        editor.apply()
    }

    fun isPhone(): String? {
        return pref.getString("isPhone", null)
    }

    fun isHelpNumber(isHelpNumber: String) {
        val editor = pref.edit()
        editor.putString("isHelpNumber", isHelpNumber)
        editor.apply()
    }

    fun isHelpNumber(): String? {
        return pref.getString("isHelpNumber", null)
    }

    fun isDate(isDate: String) {
        val editor = pref.edit()
        editor.putString("isDate", isDate)
        editor.apply()
    }

    fun isDate(): String? {
        return pref.getString("isDate", null)
    }
}