package com.kiryl.mathworkout.services

import android.content.Context
import androidx.room.Room
import com.kiryl.mathworkout.math.viewmodel.general.TaskGeneration
import com.kiryl.mathworkout.services.data.pref.AppData
import com.kiryl.mathworkout.services.data.room.game.GameStats
import com.kiryl.mathworkout.services.data.room.game.GameStatsAppDataBase

class Analytics(private var context: Context) {

    private val appData = AppData(context)
    private val difficulty = appData.getDifficulty()

    private fun getList(): List<GameStats> {
        val db = Room.databaseBuilder(context, GameStatsAppDataBase::class.java, "database-name").build()
        val game = db.gameStatsDao()
        return game.getAll()
    }

    fun analyze(): Boolean {
        fixDamagedDifficulty()
        val statsList = getLast10Rounds()

        if (statsList.isNotEmpty() && getList().size %10 == 0){
            var correctAnswers = 0
            var totalAnswers = 0

            for (element in statsList){
                correctAnswers += element.correctAnswer
                totalAnswers += element.totalAnswers
            }

            val result = correctAnswers.toFloat()/totalAnswers.toFloat()

            if (result > 0.7f){
                higherDifficulty()
                return true
            }

            if (result < 0.3f){
                lowerDifficulty()
                return false
            }

        }
        return false
    }

    private fun fixDamagedDifficulty(){
        if (difficulty != TaskGeneration.DIFFICULTY_HARD && difficulty != TaskGeneration.DIFFICULTY_NORMAL && difficulty != TaskGeneration.DIFFICULTY_EASY && difficulty != TaskGeneration.DIFFICULTY_BABY )
        appData.setDifficulty(TaskGeneration.DIFFICULTY_EASY)
    }


    private fun higherDifficulty() {
        if (difficulty== TaskGeneration.DIFFICULTY_BABY){
            appData.setDifficulty(TaskGeneration.DIFFICULTY_EASY)
        }
        if (difficulty == TaskGeneration.DIFFICULTY_EASY){
            appData.setDifficulty(TaskGeneration.DIFFICULTY_NORMAL)
        }
        if (difficulty == TaskGeneration.DIFFICULTY_NORMAL){
            appData.setDifficulty(TaskGeneration.DIFFICULTY_HARD)
        }
    }

    private fun lowerDifficulty() {
        if (difficulty == TaskGeneration.DIFFICULTY_EASY){
            appData.setDifficulty(TaskGeneration.DIFFICULTY_BABY)
        }
        if (difficulty == TaskGeneration.DIFFICULTY_NORMAL){
            appData.setDifficulty(TaskGeneration.DIFFICULTY_EASY)
        }
        if (difficulty == TaskGeneration.DIFFICULTY_HARD){
            appData.setDifficulty(TaskGeneration.DIFFICULTY_NORMAL)
        }
    }

    private fun getLast10Rounds(): List<GameStats> {
        val list = getList()
        var lastTenList = list
        if (list.isNotEmpty() && list.size > 15){
            lastTenList = list.takeLast(10)
        }
        return lastTenList
    }

}