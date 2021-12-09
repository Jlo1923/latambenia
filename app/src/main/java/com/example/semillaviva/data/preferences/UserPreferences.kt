package com.example.semillaviva.data.preferences

import android.content.Context
import android.content.SharedPreferences

class UserPreferences constructor(context: Context) {

    companion object {
        const val PREFERENCE_NAME = "lumenInnovations.semillaviva"
        var IS_LOGGED = "logged"
        var USER_ID = "id"
        var USER_HORA = "hora"
        var USER_IMAGEN = "imagen"
        var USER_PHONE = "phone"
        var USER_NOMBRE = "nombre"
        var USER_UBICACION = "ubicacion"
        var USER_CEDULA = "cedula"
        var USER_TOKEN = "token"
    }

    private var preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun save(KEY_NAME: String, value: Int) {
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putInt(KEY_NAME, value)
        editor.apply()
    }

    fun save(KEY_NAME: String, value: String) {
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putString(KEY_NAME, value)
        editor.apply()
    }

    fun save(KEY_NAME: String, value: Long) {
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putLong(KEY_NAME, value)
        editor.apply()
    }

    fun save(KEY_NAME: String, status: Boolean) {
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putBoolean(KEY_NAME, status)
        editor.apply()
    }

    fun getValueString(KEY_NAME: String): String? {
        return preferences.getString(KEY_NAME, null)
    }

    fun getValueInt(KEY_NAME: String): Int {
        return preferences.getInt(KEY_NAME, 0)
    }

    fun getValueBoolean(KEY_NAME: String): Boolean {
        return preferences.getBoolean(KEY_NAME, true)
    }
    fun getValueLong(KEY_NAME: String): Long {
        return preferences.getLong(KEY_NAME, 0)
    }

    fun clearSharedPreference() {
        val editor: SharedPreferences.Editor = preferences.edit()
        //sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        editor.clear()
        editor.apply()
    }

    fun removeValue(KEY_NAME: String) {
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.remove(KEY_NAME)
        editor.apply()
    }
}