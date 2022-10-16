package com.kiryl.mathworkout.ui.statistic

import android.content.Context
import androidx.room.Room
import com.github.mikephil.charting.data.BarEntry
import com.kiryl.mathworkout.services.data.pref.AppData
import com.kiryl.mathworkout.services.data.room.game.GameStats
import com.kiryl.mathworkout.services.data.room.game.GameStatsAppDataBase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChartData(private val context: Context) {

    private var barArrayList: ArrayList<BarEntry> = ArrayList()
    private var appData = AppData(context)

    private fun getList(): List<GameStats> {

        val db = Room.databaseBuilder(context, GameStatsAppDataBase::class.java, "database-name").build()

        val game = db.gameStatsDao()
        return game.getAll()
    }


    fun getCorrectAnswers(): ArrayList<BarEntry> {
        val last: Int? = appData.getLastElement()
        var list = getList()

        try {if (last != null) list = list.takeLast(last) }catch (e:ArrayIndexOutOfBoundsException){e.printStackTrace()}


        if (list.isNotEmpty()) {
            for (element in list) {
                barArrayList.add(BarEntry(element.id.toFloat(), element.correctAnswer.toFloat()))
            }
        }

        return barArrayList
    }

//    TODO
//    fun getStatsByDay(): ArrayList<BarEntry> {
//        val list = getList()
//
//        val datesAsStringList = ArrayList<String>()
//
//        val dayStats = ArrayList<GameStats>()

//
//        get array with all dates
//        for (element in list) {
//            val date = formatDate(element.date.toLong())
//            if (!datesAsStringList.contains(date)) datesAsStringList.add(date)
//        }

//        return barArrayList
//    }

    fun formatDate(systemTime: Long): String {
        val date = Date(systemTime)
        val format = SimpleDateFormat("yyyy.MM.dd")
        return format.format(date)
    }

    fun getSingleStatistic(id: Int): GameStats? {
        val list = getList()
        for (element in list){
            if (element.id == id){
                return element
            }
        }
        return null
    }

}
