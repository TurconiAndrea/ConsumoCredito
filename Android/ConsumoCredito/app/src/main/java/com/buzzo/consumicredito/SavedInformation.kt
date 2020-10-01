package com.buzzo.consumicredito

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

class SavedInformation
    ( context: Context ) {

    private val PREF_NAME = "com.buzzo.ConsumoCredito.SavedInformation"
    private val FIRST_RUN_KEY = "FIRST_RUN"
    private val AUTH_KEY = "AUTH_KEY"
    private var sharedPreferences = context.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE)

    fun isFirstRun(): Boolean {
        return sharedPreferences.getBoolean(FIRST_RUN_KEY, true)
    }

    fun setFirstRunDone() {
        sharedPreferences.edit().putBoolean(FIRST_RUN_KEY, false).apply()
    }

    fun getUserAuthKey(): String? {
        return sharedPreferences.getString(AUTH_KEY, null)
    }

    fun setUserAuthKey(authKey: String) {
        sharedPreferences.edit().putString(AUTH_KEY, authKey).apply()
    }

    fun removeUserAuthKey() {
        sharedPreferences.edit().remove(AUTH_KEY).apply()
    }


}