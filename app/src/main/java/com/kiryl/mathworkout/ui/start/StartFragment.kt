package com.kiryl.mathworkout.ui.start

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kiryl.mathworkout.MainActivity
import com.kiryl.mathworkout.R
import com.kiryl.mathworkout.helicopter.ui.start.HelicopterStartFragment
import com.kiryl.mathworkout.math.view.ClassicFragment
import com.kiryl.mathworkout.math.viewmodel.general.GameTransition
import com.kiryl.mathworkout.math.viewmodel.general.model.MathInstance
import com.kiryl.mathworkout.services.Analytics
import com.kiryl.mathworkout.services.data.pref.AppData
import com.kiryl.mathworkout.ui.miscellaneous.SettingFragment
import com.kiryl.mathworkout.ui.statistic.StatsFragment
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking


@OptIn(DelicateCoroutinesApi::class)
class StartFragment : Fragment() {

    private lateinit var appData: AppData
    private lateinit var playButton: ImageView
    private lateinit var diamondsText: TextView
    private lateinit var streakText: TextView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var streakProgressBar: ProgressBar
    private lateinit var goalTextView: TextView
    private lateinit var settingsButton: TextView

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_start, container, false)
    }

    companion object{
        const val DEFAULT_NAVI_ID = R.id.page_1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appData = AppData(requireContext())
        playButton = view.findViewById(R.id.playButton)
        streakText = view.findViewById(R.id.streak_text)
        bottomNavigationView = view.findViewById(R.id.bottom_navigation)
        streakProgressBar = view.findViewById(R.id.progress)
        goalTextView = view.findViewById(R.id.goal_text_view)
        settingsButton = view.findViewById(R.id.settings_button)
        diamondsText = view.findViewById(R.id.diamonds_text)

        setListeners()
        restoreData()
        animateData()
    }

    private fun animateData(){
        var goal = appData.getDayGoal()
        val streak  = appData.getStreak()
        streakText.text = streak.toString() + " " +getString(R.string.day_s)

        if (streak > goal-1){
            goal+=10
            appData.upDateGoal(goal)
        }
        val streakAnimator = ValueAnimator.ofInt(0, streak*100)
        streakProgressBar.max = goal*100
        streakProgressBar.progress  = 0
        streakAnimator.duration = 2000
        streakAnimator.addUpdateListener {
            streakProgressBar.progress = streakAnimator.animatedValue as Int
        }
        streakAnimator.start()
    }
    override fun onPause() {
        super.onPause()
        bottomNavigationView.selectedItemId = DEFAULT_NAVI_ID
    }

    private fun analyze() = runBlocking {
        launch(newSingleThreadContext("AnalyticsCoroutine")) {
            val analytics = Analytics(requireContext())
            analytics.analyze()
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setListeners() {
        playButton.setOnClickListener{
            analyze()
            val mathInstance = MathInstance(0, GameTransition.DEFAULT_HEARTS, mRandomShuffle = true, mInfinitePlay = false)
            mathInstance.startTime = System.currentTimeMillis()
            (activity as MainActivity).openFragment(ClassicFragment(mathInstance))

        }

        view?.setOnClickListener{
            restoreData()
        }

        settingsButton.setOnClickListener {
            (activity as MainActivity).openFragment(SettingFragment())
        }

        bottomNavigationView.selectedItemId = DEFAULT_NAVI_ID
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.page_0 -> {
                    (activity as MainActivity).openFragmentWithOutTransitions(StatsFragment())
                    true
                }
                R.id.page_1 -> {
                    true
                }
                R.id.page_2 ->{
                    (activity as MainActivity).openFragmentWithOutTransitions(HelicopterStartFragment())
                    true
                }
                else -> {
                    false
                }
            }
        }
    }




    @SuppressLint("SetTextI18n")
    private fun restoreData() {
        diamondsText.text = "${appData.getDiamonds()} 💎"
        goalTextView.text = "${appData.getDayGoal()}🚩"
    }
}
