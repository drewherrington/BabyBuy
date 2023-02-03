package com.project.babybuy

import android.content.Context
import android.content.SharedPreferences

class PrefManager(context: Context) {
    private val pref: SharedPreferences
    private val editor: SharedPreferences.Editor


    fun createLoginSession() {
        editor.putBoolean(IS_LOGIN, true)
        editor.commit()
        editor.apply()
    }

    fun createLogoutSession() {
        editor.putBoolean(IS_LOGIN, false)
        editor.commit()
        editor.apply()
    }

    fun deletePrefData() {
        editor.clear()
        editor.commit()
        editor.apply()
    }



    fun getPrefValue(key: String?): String? {
        var returnValue: String? = ""
        try {
            returnValue = pref.getString(key, "")
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return returnValue
    }

    fun checkLogin(): Boolean {
        return if (pref.getBoolean(IS_LOGIN, false)) {
            true
        } else {
            false
        }
    }

    fun savePrefValue(key: String, value: String?) {
        editor.putString(key, value)
        editor.commit()
        editor.apply()
    }

    companion object {


        private const val PREF_NAME = "BabyBuy"
        private const val IS_LOGIN = "IsLoggedIn"


    }

    init {
        val PRIVATE_MODE = 0
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }
}