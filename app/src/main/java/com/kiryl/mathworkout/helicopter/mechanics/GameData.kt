package com.kiryl.mathworkout.helicopter.mechanics

import android.content.Context


class GameData(context: Context) {

    companion object{
        const val SHARED_PREFS_NAME = "helicopter_game_shared_prefs"
        const val COINS_PREF_KEY = "coins_pref_key"
        const val TIME_PLAYED_PREF_KEY = "time_played_pref_key"

        const val PURCHASED_BLUE_HELICOPTER_PREF_KEY = "purchased_blue_helicopter_pref_key"
        const val PURCHASED_WHITE_HELICOPTER_PREF_KEY = "purchased_white_helicopter_pref_key"
        const val PURCHASED_GREY_HELICOPTER_PREF_KEY = "purchased_grey_helicopter_pref_key"
        const val PURCHASED_GREEN_HELICOPTER_PREF_KEY = "purchased_green_helicopter_pref_key"
        const val PURCHASED_RED_HELICOPTER_PREF_KEY = "purchased_red_helicopter_pref_key"

        const val SELECTED_HELICOPTER_PREF_KEY = "selected_helicopter_pref_key"
        const val REVIVE_TOKENS_PREF_KEY = "revive_tokens_pref_key"

        const val FPS_PREF_KEY = "fps_pref_key"
    }

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun setInt(key:String,value:Int){
        editor.apply {
            putInt(key, value)
        }.apply()
    }

    fun getInt(key: String,default: Int? = 0):Int {
        return sharedPreferences.getInt(key,default!!)
    }

    fun setBoolean(key:String,value:Boolean){
        editor.apply {
            putBoolean(key, value)
        }.apply()
    }

    fun getBoolean(key: String):Boolean {
        return sharedPreferences.getBoolean(key, false)
    }



}