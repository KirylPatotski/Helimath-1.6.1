package com.kiryl.mathworkout.math.ui.results

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kiryl.mathworkout.MainActivity
import com.kiryl.mathworkout.R
import com.kiryl.mathworkout.services.data.pref.AppData
import com.kiryl.mathworkout.ui.results.Confetti
import com.kiryl.mathworkout.ui.start.StartFragment


class ResultStreakFragment(private var streak:Int) : Fragment() {
    private lateinit var appData: AppData

    private lateinit var continueButton: Button
    private lateinit var shareButton: Button
    private lateinit var streakText: TextView
    private lateinit var streakProgressBar: ProgressBar
    private lateinit var container: ViewGroup
    private lateinit var anchor: TextView
    private lateinit var diamondsText: TextView
    private lateinit var tomorrowDiamondsTextView: TextView
    private val todayDiamonds = 100+streak

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_streak, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appData = AppData(requireContext())
        streakText = view.findViewById(R.id.streak_number)
        continueButton = view.findViewById(R.id.continue_button)
        shareButton = view.findViewById(R.id.share_button)
        streakProgressBar = view.findViewById(R.id.progressStreak)
        anchor = view.findViewById(R.id.animation_anchor)
        container = view.findViewById(R.id.container)
        tomorrowDiamondsTextView = view.findViewById(R.id.tomorrow_claim_text)
        diamondsText = view.findViewById(R.id.diamonds_text)


        setListeners()

            countStreak()

    }

    @SuppressLint("SetTextI18n")
    private fun countStreak() {
        val tomorrowDiamonds = todayDiamonds

        val scoreAnimator = ValueAnimator.ofInt(0, tomorrowDiamonds+1)
        scoreAnimator.duration = 2000
        scoreAnimator.addUpdateListener {
            try {
                tomorrowDiamondsTextView.text = requireContext().getString(R.string.diamonds_tomorrow) + ": " + scoreAnimator.animatedValue.toString()
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        scoreAnimator.start()


        val diamondsAnimator = ValueAnimator.ofInt(0,todayDiamonds)
        diamondsAnimator.duration = 4000
        diamondsAnimator.addUpdateListener {
            diamondsText.text = "+" + diamondsAnimator.animatedValue.toString() + "💎"
        }
        diamondsAnimator.start()

        val animation = Confetti(requireContext(), container, requireView(), anchor)
        animation.animate()

        val animator = ValueAnimator.ofInt(0,streak)
        animator.duration = 300
        animator.addUpdateListener {
            streakText.text = animator.animatedValue.toString()
        }
        animator.start()

        streakProgressBar.max = streak * 1000
        streakProgressBar.progress = (streak-1)*1000
        val max = streakProgressBar.max
        val min = streakProgressBar.progress
        val animator2 = ValueAnimator.ofInt(min,max)
        animator2.duration = 4000
        animator2.addUpdateListener {
            streakProgressBar.progress = Integer.parseInt(animator2.animatedValue.toString())
        }
        animator2.start()
    }

    private fun setListeners() {
        continueButton.setOnClickListener {
            appData.addDiamonds(todayDiamonds)
//            (activity as MainActivity).openFragment(InterstitialAdFragment(),true)
            (activity as MainActivity).openFragment(StartFragment(),true)
            return@setOnClickListener
        }
        shareButton.setOnClickListener {
            share()
        }

    }



    private fun share() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT,"I have a ${streakText.text} day streak in ${getString(R.string.app_name)}. You can download it here: play.google.com/store/apps/details?id=com.kiryl.arithmetic")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "I ♥ math")
        startActivity(shareIntent)
        shareButton.visibility = View.GONE
        appData.addDiamonds(10)
        Toast.makeText(requireContext(),getString(R.string.Added_10_diamonds), Toast.LENGTH_SHORT).show()
    }

}