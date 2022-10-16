package com.kiryl.mathworkout.ui.results

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.kiryl.mathworkout.MainActivity
import com.kiryl.mathworkout.R
import com.kiryl.mathworkout.math.ui.results.ResultStreakFragment
import com.kiryl.mathworkout.math.viewmodel.general.model.MathInstance
import com.kiryl.mathworkout.services.data.pref.AppData
import com.kiryl.mathworkout.services.data.pref.GameIDPref
import com.kiryl.mathworkout.services.data.room.game.GameStats
import com.kiryl.mathworkout.services.data.room.game.GameStatsAppDataBase
import com.kiryl.mathworkout.ui.start.StartFragment
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random


class ResultFragment(private var score: Int, private var mathInstance: MathInstance) : Fragment() {

    private lateinit var appData: AppData
    private lateinit var continueButton: Button
    private lateinit var shareButton: Button

    private lateinit var scoreText: TextView
    private lateinit var diamondsText: TextView
    private lateinit var container: ViewGroup

    private lateinit var anchor: TextView



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_result, container, false)
    }

    companion object {
        private var diamondsCount = 0
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appData = AppData(requireContext())
        scoreText = view.findViewById(R.id.resultFragmentScoreText)
        continueButton = view.findViewById(R.id.continue_button)
        shareButton = view.findViewById(R.id.share_button)
        diamondsText = view.findViewById(R.id.resultFragmentDiamondsText)
        anchor = view.findViewById(R.id.animation_anchor)
        diamondsCount = score * Random.nextInt(10, 15) + Random.nextInt(0, 15)
        container = view.findViewById(R.id.container)
        setListeners()
        countScore()
        animate()
    }





    private fun countScore() {
        val scoreAnimator = ValueAnimator.ofInt(0, score)
        scoreAnimator.duration = 2000
        scoreAnimator.addUpdateListener {
            scoreText.text = scoreAnimator.animatedValue.toString()
        }
        scoreAnimator.start()


        val diamondsAnimator = ValueAnimator.ofInt(0, diamondsCount)
        diamondsAnimator.duration = 4000
        diamondsAnimator.addUpdateListener {
            diamondsText.text = diamondsAnimator.animatedValue.toString()
        }
        diamondsAnimator.start()
    }

    private fun animate() {
        val animation = Confetti(requireContext(), container, requireView(), anchor)
        animation.animate()
    }

    private fun setListeners() {
        continueButton.setOnClickListener {
            saveAll()
            val alreadyUsed = appData.streak()
            val streak = appData.getAnyInt(AppData.STREAK_PREF_KEY)

            if (!alreadyUsed) continueWithFragment(ResultStreakFragment(streak))
            else continueWithFragment(StartFragment())

        }
        shareButton.setOnClickListener { share() }

    }


    private fun continueWithFragment(f: Fragment) {
        (activity as MainActivity).openFragment(f, true)
    }

    private fun share() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, "I scored ${scoreText.text} in this app - You can download it here btw: play.google.com/store/apps/details?id=com.kiryl.arithmetic")
        intent.type = "text/plain"
        startActivity(intent)
        appData.addDiamonds(10)
        Toast.makeText(requireContext(),getString(R.string.Added_10_diamonds),Toast.LENGTH_SHORT).show()
        shareButton.visibility = View.GONE
    }

    private fun saveAll() {
        appData.addDiamonds(diamondsCount)
        appData.addTotalScore(score)
        appData.addXP(score)
        saveGameStats()
    }

    @SuppressLint("CommitPrefEdits")
    @OptIn(DelicateCoroutinesApi::class)
    private fun saveGameStats() = runBlocking {
        launch(newSingleThreadContext("ResultFragmentGameSave")) {
            val db = Room.databaseBuilder(requireContext(), GameStatsAppDataBase::class.java, "database-name").build()
            val dtf: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
            val now: LocalDateTime = LocalDateTime.now()

            val pref = GameIDPref(requireContext())
            val gameID = pref.getID()
            pref.update()

            val gameStats = GameStats(
                id = gameID,
                date = System.currentTimeMillis().toString(),
                timeUsed = (System.currentTimeMillis()-mathInstance.startTime).toInt(),
                totalAnswers = mathInstance.totalAnswers,
                correctAnswer = mathInstance.score,

                additionUsed = mathInstance.additionUsed,
                subtractionUsed = mathInstance.subtractionUsed,
                multiplicationUsed = mathInstance.multiplicationUsed,
                divisionUsed = mathInstance.divisionUsed,
                powerUsed = mathInstance.powerUsed,
                logarithmUsed = mathInstance.logarithmUsed,

                difficulty = appData.getDifficulty()
            )

            val game = db.gameStatsDao()
            game.insertAll(gameStats)
        }
    }

}



