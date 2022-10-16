package com.kiryl.mathworkout.helicopter.sprites

import android.content.Context
import com.kiryl.mathworkout.helicopter.mechanics.GameData

class FPS(context: Context) {
    val gameData: GameData = GameData(context)

    companion object{
        const val DEFAULT_FPS = 60
    }

    fun getFPS(): Int {
        return gameData.getInt(GameData.FPS_PREF_KEY,60)
    }

    fun setFPS(value: Int){
        println(value)
        gameData.setInt(GameData.FPS_PREF_KEY,value)
    }
}