package com.kiryl.mathworkout.services.data.pref


import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import com.kiryl.mathworkout.R
import com.kiryl.mathworkout.math.viewmodel.general.TaskGeneration
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class AppData(val context: Context){
    private var editor:SharedPreferences.Editor
    private var pref:SharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    init {
        editor = pref.edit()
    }

    companion object {

        const val SHARED_PREFS_NAME = "shared_prefs"
        const val DIAMONDS_PREF_KEY = "diamonds_pref_key"
        const val DIFFICULTY_PREF_KEY = "difficulty_pref_key"
        const val HI_PREF_KEY = "hi_pref_key"
        const val TOTAL_DIAMONDS_PREF_KEY = "total_diamonds_pref_key"
        const val TOTAL_SCORE_PREF_KEY = "total_score_pref_key"

        //App Settings
        const val LAST_ELEMENS_TO_LOAD_IN_DIAGRAM_PREF_KEY = "last_elements_pref_key"
        const val SEND_NOTIFICATION_PREF_KEY = "send_notification_pref_key"
        const val DARK_MODE_PREF_KEY = "dark_mode_pref_key"
        const val VIBRATIONS_PREF_KEY = "vibrations_pref_key"
        const val SOUND_TURNED_ON_PREF_KEY  = "sound_turned_on_pref_key"
        const val LANGUAGE_PREF_KEY = "language_pref_key"
        const val FIRST_TIME_PREF_KEY = "first_time_pref_key"

        //Other Tools
        const val CONSENT_TO_NEWS_LETTER_PREF_KEY = "consent_to_news_letter_pref_key"


        //Streak
        const val STREAK_PREF_KEY = "streak_pref_key"
        const val LAST_USE_PREF_KEY = "last_use_pref_key"
        const val DAY_GOAL_PREF_KEY = "day_goal_pref_key"
        const val XP = "xp"
    }

    fun getXP(): Int {
        return pref.getInt(XP,0)
    }

    fun addXP(amount: Int){
        val xp = getXP() + amount
        editor.apply{
            putInt(XP,xp)
        }.apply()
    }

    fun getLastElement(): Int? {
        val index = pref.getInt(LAST_ELEMENS_TO_LOAD_IN_DIAGRAM_PREF_KEY,0)
        return if (index == 0) null
        else index
    }

    fun addDiamonds(diamonds: Int) {
        var tempDiamonds = pref.getInt(TOTAL_DIAMONDS_PREF_KEY, 0)
        tempDiamonds += diamonds
        val dia = pref.getInt(DIAMONDS_PREF_KEY, 0)
        editor.apply {
            putInt(DIAMONDS_PREF_KEY, diamonds + dia)
            if (diamonds > 0) {
                putInt(TOTAL_DIAMONDS_PREF_KEY, tempDiamonds)
            }
        }.apply()
    }

    fun addTotalScore(score: Int) {
        var tempScore = pref.getInt(TOTAL_SCORE_PREF_KEY, 0)
        tempScore += score
        editor.apply {
            putInt(TOTAL_SCORE_PREF_KEY, tempScore)
        }.apply()
    }

    fun getDiamonds(): Int {
        return pref.getInt(DIAMONDS_PREF_KEY, 0)
    }


    fun setDifficulty(difficulty: String) {
        editor.apply {
            putString(DIFFICULTY_PREF_KEY, difficulty)
        }.apply()
        println(difficulty)
    }

    fun getDifficulty(): String {
        val difficulty = pref.getString(DIFFICULTY_PREF_KEY, TaskGeneration.DIFFICULTY_EASY)
        println(difficulty)
        return difficulty.toString()
    }

    fun setAnyInt(key: String, value: Int){
        editor.apply {
            putInt(key, value)
        }.apply()
    }

    fun getAnyInt(key: String, defaultValue: Int? = 0): Int {
        return pref.getInt(key, defaultValue!!)
    }

    fun setAnyString(key: String, value: String){
        editor.apply {
            putString(key, value)
        }.apply()
        println(key + value)
    }

    fun getAnyString(key: String, defaultValue: String? = "" ): String? {
        return pref.getString(key, defaultValue)
    }




    fun streak():Boolean{
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val yesterday = dateFormat.format(calendar.time)
        calendar.add(Calendar.DATE,1)
        val today = dateFormat.format(calendar.time)

        var todayUsed = false
        var lastUse = pref.getString(LAST_USE_PREF_KEY,null)
        var streak = pref.getInt(STREAK_PREF_KEY,0)
        println("$lastUse $streak")

        when (lastUse) {
            null -> {
                lastUse = today
                streak = 1
            }
            today -> {
                todayUsed = true
            }
            yesterday -> {
                lastUse = today
                streak++
            }
            else -> {
                lastUse = null
                streak = 1
            }
        }
        println("$lastUse $streak")
        val editor = pref.edit()

        editor.apply{
           editor.putString(LAST_USE_PREF_KEY,lastUse)
           editor.putInt(STREAK_PREF_KEY,streak)
        }.apply()

        return todayUsed
    }

    fun getStreak(): Int{
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val yesterday = dateFormat.format(calendar.time)
        calendar.add(Calendar.DATE,1)
        val today = dateFormat.format(calendar.time)
        var lastUse = pref.getString(LAST_USE_PREF_KEY,null)
        var streak = pref.getInt(STREAK_PREF_KEY,0)
        println("$lastUse $streak")

        when (lastUse) {
            null -> {
                streak = 0
            }
            today -> {

            }
            yesterday -> {

            }
            else -> {
                if (getDiamonds() > 499) {
                    AlertDialog.Builder(context)
                        .setTitle("Pay 500💎 to restore streak?")
                        .setMessage("Otherwise your streak will be set to 0")
                        .setPositiveButton(context.getString(R.string.Yes)) { dialog, _ ->
                            dialog.dismiss()
                            lastUse = today.toString()
                            editor.apply {
                                editor.putString(LAST_USE_PREF_KEY, lastUse)
                                editor.putInt(STREAK_PREF_KEY, streak)
                            }.apply()
                            addDiamonds(-500)
                        }
                        .setNegativeButton(context.getString(R.string.cancel)) { _, _ -> }
                        .show()
                }
                return streak
                }
        }
        editor.apply{
            editor.putString(LAST_USE_PREF_KEY,lastUse)
            editor.putInt(STREAK_PREF_KEY,streak)
        }.apply()
        return streak
    }


    fun setAnyBoolean(key: String, boolean: Boolean) {
        editor.apply {
            putBoolean(key, boolean)
        }.apply()
        println("${key.uppercase(Locale.getDefault())} $boolean")
    }

    fun getAnyBoolean(key: String, defaultValue: Boolean? = true): Boolean {
        return pref.getBoolean(key, defaultValue!!)
    }

    fun getDayGoal(): Int {
        return pref.getInt(DAY_GOAL_PREF_KEY,30)
    }

    fun upDateGoal(value: Int){
        editor.apply {
            putInt(DAY_GOAL_PREF_KEY,value)
        }.apply()
    }
}

