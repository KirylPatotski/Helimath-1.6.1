package com.kiryl.mathworkout.services.data.pref


import android.content.Context
import android.content.SharedPreferences


class GameIDPref(private val context: Context) {
    private var editor: SharedPreferences.Editor
    private var pref: SharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    init {
        editor = pref.edit()
    }

    companion object {
        private const val SHARED_PREFS_NAME = "shared_pref_database_id_key"
        private const val ID_KEY = "id_key"
    }


    fun update() {
        val gameID = pref.getInt(ID_KEY,0)
        editor.apply {
            putInt(ID_KEY, gameID + 1)
        }
    }

    fun getID(): Int {
        return pref.getInt(ID_KEY,0)
    }
}

