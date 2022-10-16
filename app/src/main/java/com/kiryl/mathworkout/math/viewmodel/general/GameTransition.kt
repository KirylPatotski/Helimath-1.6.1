package com.kiryl.mathworkout.math.viewmodel.general

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.widget.ProgressBar
import com.kiryl.mathworkout.MainActivity
import com.kiryl.mathworkout.math.view.AnswerFragment
import com.kiryl.mathworkout.math.view.ClassicFragment
import com.kiryl.mathworkout.math.view.TrueFalseFragment
import com.kiryl.mathworkout.math.viewmodel.AnswerViewModel
import com.kiryl.mathworkout.math.viewmodel.ClassicViewModel
import com.kiryl.mathworkout.math.viewmodel.TrueFalseViewModel
import com.kiryl.mathworkout.math.viewmodel.general.model.MathInstance
import com.kiryl.mathworkout.ui.results.ResultFragment
import kotlin.random.Random

class GameTransition(val activity: Activity):Thread() {

    private val act = (activity as MainActivity)

    companion object {
        const val DEFAULT_HEARTS = 3
        const val DEFAULT_MAX_SCORE = 15
    }

    fun checkForInfinitePlay(mathInstance: MathInstance, progressBar: ProgressBar){
        if (!mathInstance.infinitePlay) {
            progressBar.max = DEFAULT_MAX_SCORE *1000
            val max = mathInstance.score*1000
            progressBar.progress = (mathInstance.score-1)*1000
            val min = progressBar.progress
            val animator = ValueAnimator.ofInt(min, max)
            animator.duration = 1000
            animator.addUpdateListener {
                progressBar.progress = Integer.parseInt(animator.animatedValue.toString())
            }
            animator.start()

            if (mathInstance.score > DEFAULT_MAX_SCORE -1)
                act.openFragment(ResultFragment(mathInstance.score, mathInstance),true)

        }else{
            progressBar.alpha = 0.0F
        }
    }

    @SuppressLint("SetTextI18n")
    fun decideWhichFragment(mathInstance: MathInstance, TAG: String) {
        mathInstance.totalAnswers++
        openFragment(mathInstance,TAG)
    }

    private fun openFragment(mathInstance: MathInstance, TAG: String) {
        val localRandomShuffle = mathInstance.randomShuffle

        if (mathInstance.hearts <= 0) {
            act.openFragment(ResultFragment(mathInstance.score, mathInstance), true, TAG)
        }
        if (mathInstance.hearts > 0) {
            if (localRandomShuffle) {
                val fragment = Random.nextInt(0, 3)
                if (fragment == 0) act.openFragment(AnswerFragment(mathInstance), true)
                if (fragment == 1) act.openFragment(ClassicFragment(mathInstance), true)
                if (fragment == 2) act.openFragment(TrueFalseFragment(mathInstance), true)
            } else {
                if (TAG == AnswerViewModel.TAG) act.openFragment(AnswerFragment(mathInstance), true)
                if (TAG == ClassicViewModel.TAG) act.openFragment(ClassicFragment(mathInstance), true)
                if (TAG == TrueFalseViewModel.TAG) act.openFragment(TrueFalseFragment(mathInstance), true)
            }
        }
    }
}


