package com.kiryl.mathworkout.math.viewmodel

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.kiryl.mathworkout.MainActivity
import com.kiryl.mathworkout.R
import com.kiryl.mathworkout.math.viewmodel.general.GameTransition
import com.kiryl.mathworkout.math.viewmodel.general.TaskGeneration
import com.kiryl.mathworkout.math.viewmodel.general.model.MathInstance
import com.kiryl.mathworkout.math.view.CorrectAnswerFragment
import kotlin.random.Random

class TrueFalseViewModel(
    private var activity: Activity,
    private var b1: ImageView,
    private var b2: ImageView,
    private var scoreText: TextView,
    private var heartsText: TextView,
    private var taskText: TextView,
    private var progressBar: ProgressBar,
    private var mathInstance: MathInstance
) : Thread() {

    private val act = (activity as MainActivity)
    private val gameTransition = GameTransition(act)
    private val gameLogic = TaskGeneration(act.applicationContext,mathInstance)

    private var task = ""
    private var answer = 0
    private var fakeAnswer = 0
    private var trueOrFalse = 0

    companion object {
        const val TAG = "TRUE_FALSE_FRAGMENT"
    }

    override fun run() {
        generateTask()
        setListeners()
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor", "WrongConstant")
    private fun nextTask(button: ImageView) {
        act.vibrate()

        if (button == b1 && trueOrFalse == 0) {
            continueGame()
            return
        }
        if (button == b2 && trueOrFalse == 1) {
            continueGame()
            return
        }
        mathInstance.hearts--
        act.playSound(R.raw.wrong1)
        (activity as MainActivity).openFragment(CorrectAnswerFragment(mathInstance, TAG,"$task $answer"))
    }

    private fun continueGame() {
        mathInstance.score++
        act.playSound(R.raw.correc2)
        gameTransition.decideWhichFragment(mathInstance, TAG)
    }

    private fun setListeners() {
        b1.setOnClickListener {
            nextTask(b1)
        }
        b2.setOnClickListener {
            nextTask(b2)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun generateTask() {
        task = gameLogic.getTask()
        answer = gameLogic.getAnswer()
        heartsText.text = mathInstance.hearts.toString()
        scoreText.text = "${mathInstance.score}/${GameTransition.DEFAULT_MAX_SCORE}"
        progressBar.max = GameTransition.DEFAULT_MAX_SCORE
        progressBar.progress = mathInstance.score
        assignButton(answer)

        if (task == "" || task == null){
            task = gameLogic.getTask()
            answer = gameLogic.getAnswer()
        }

        trueOrFalse = Random.nextInt(0, 2)
        if (trueOrFalse == 0) {
            taskText.text = "$task $answer"
        }
        if (trueOrFalse == 1) {
            taskText.text = "$task $fakeAnswer"
        }
        gameTransition.checkForInfinitePlay(mathInstance, progressBar)
        animateTime()




    }

    private fun animateTime(){
        val max = 3000
        progressBar.max = max
        progressBar.progress = max
        val animator = ValueAnimator.ofInt(max, 0)
        animator.duration = max.toLong()
        animator.addUpdateListener {
            progressBar.progress = animator.animatedValue as Int
        }
        animator.start()
    }

    private fun assignButton(localAnswer: Int) {
        val a = Random.nextInt(0, 3)
        if (a == 0) {
            fakeAnswer = localAnswer + 1
        }
        if (a == 1) {
            fakeAnswer = localAnswer - 1
        }
        if (a == 2) {
            val index = Random.nextInt(0, 2)
            if (index == 0) {
                fakeAnswer = localAnswer - Random.nextInt(2, 4)
            }
            if (index == 1) {
                fakeAnswer = localAnswer + Random.nextInt(2, 4)
            }
        }
    }
}


