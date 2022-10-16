package com.kiryl.mathworkout.ui.statistic

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.kiryl.mathworkout.MainActivity
import com.kiryl.mathworkout.R
import com.kiryl.mathworkout.helicopter.ui.start.HelicopterStartFragment
import com.kiryl.mathworkout.services.data.pref.AppData
import com.kiryl.mathworkout.services.data.room.game.GameStats
import com.kiryl.mathworkout.ui.start.StartFragment
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*


class StatsFragment : Fragment() {

    private lateinit var appData : AppData
    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var barChart: BarChart
    private var scoreBarData = ArrayList<BarEntry>()
    private var gameStats = GameStats()

    private lateinit var idChip: Chip
    private lateinit var dateChip: Chip
    private lateinit var answerChip: Chip
    private lateinit var timeChip: Chip

    private lateinit var c1: Chip
    private lateinit var c2: Chip
    private lateinit var c3: Chip
    private lateinit var c4: Chip
    private lateinit var c5: Chip
    private lateinit var difficultyChip: Chip

    private lateinit var chipList : List<Chip>

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appData = AppData(requireContext())
        bottomNavigationView = view.findViewById(R.id.bottom_navigation)

        barChart = view.findViewById(R.id.chart)

        idChip = view.findViewById(R.id.chip0)
        dateChip = view.findViewById(R.id.chip1)
        answerChip = view.findViewById(R.id.chip2)
        timeChip = view.findViewById(R.id.chip3)

        c1 = view.findViewById(R.id.chip4)
        c2 = view.findViewById(R.id.chip5)
        c3 = view.findViewById(R.id.chip6)
        c4 = view.findViewById(R.id.chip7)
        c5 = view.findViewById(R.id.chip8)
        difficultyChip = view.findViewById(R.id.chip9)

        chipList = listOf(idChip,dateChip,answerChip,timeChip,c1,c2,c3,c4,c5,difficultyChip)

        for (i in chipList)
            i.visibility = View.INVISIBLE


        setChart()
        setListeners()
    }


    private fun setChart(){
        getData()
        val correctAnswer = BarDataSet(scoreBarData, "☑ ${requireContext().getString(R.string.Correct)}")
        val barData = BarData(correctAnswer)
        barChart.data = barData
        correctAnswer.color = Color.CYAN

        barChart.setBackgroundColor(requireContext().getColor(R.color.white))
        barChart.description.text = ""
        barChart.animateXY(0, 2000)
        barChart.invalidate()
    }

    private fun getHighlighted(){
        try {
            getSingleStatistic(barChart.highlighted[0].x)
            setChipText()
        }catch (_:NullPointerException){

        }
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun setChipText(){
        idChip.text = "ID: ${gameStats.id}"
        dateChip.text = gameStats.date
        if (gameStats.date.length > 12){
            val date = Date(gameStats.date.toLong())
            val format = SimpleDateFormat("yyyy.MM.dd📅 HH:mm🕐")
            dateChip.text = format.format(date)
        }

        answerChip.text = "${gameStats.correctAnswer}/${gameStats.totalAnswers} = ${((gameStats.correctAnswer*100/ gameStats.totalAnswers*100))/100}%"
        timeChip.text = "⏲ ${gameStats.timeUsed/1000}s"
        c1.text = "'+' ${gameStats.additionUsed}"
        c2.text = "'-' ${gameStats.subtractionUsed}"
        c3.text = "'×' ${gameStats.multiplicationUsed}"
        c4.text = "'÷' ${gameStats.divisionUsed}"
        c5.text = "'²' ${gameStats.powerUsed}"
        difficultyChip.text = gameStats.difficulty

        for (i in chipList){
            i.visibility = View.VISIBLE
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getData() = runBlocking {
        launch(newSingleThreadContext("iubuiuigguiogo")) {
            val chartData = ChartData(requireContext())
            scoreBarData = chartData.getCorrectAnswers()
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun getSingleStatistic(id: Float) = runBlocking {
        launch(newSingleThreadContext("dfdoinvniusvousdfh")) {
            val chartData = ChartData(requireContext())
            gameStats = chartData.getSingleStatistic(id.toInt())!!
        }
    }



    override fun onPause() {
        super.onPause()
        bottomNavigationView.selectedItemId = DEFAULT_NAVI_ID
    }

    @SuppressLint("SetTextI18n")
    private fun setListeners() {
        bottomNavigationView.selectedItemId = DEFAULT_NAVI_ID
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.page_0 -> {
                    true
                }
                R.id.page_1 -> {
                    (activity as MainActivity).openFragmentWithOutTransitions(StartFragment())
                    true
                }
                R.id.page_2 -> {
                    (activity as MainActivity).openFragmentWithOutTransitions(HelicopterStartFragment())
                    true
                }
                else -> {
                    false
                }
            }
        }

        barChart.setOnClickListener{
            getHighlighted()
        }


    }



    companion object {
        const val DEFAULT_NAVI_ID = R.id.page_0
    }

}
