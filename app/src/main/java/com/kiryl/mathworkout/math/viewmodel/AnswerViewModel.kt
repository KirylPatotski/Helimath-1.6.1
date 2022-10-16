package com.kiryl.mathworkout.math.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.kiryl.mathworkout.MainActivity
import com.kiryl.mathworkout.R
import com.kiryl.mathworkout.math.viewmodel.general.GameTransition
import com.kiryl.mathworkout.math.viewmodel.general.TaskGeneration
import com.kiryl.mathworkout.math.viewmodel.general.model.MathInstance
import com.kiryl.mathworkout.ui.results.ResultFragment
import com.kiryl.mathworkout.math.view.CorrectAnswerFragment

class AnswerViewModel(
    private var activity: Activity,
    private var b1: TextView,
    private var e1: EditText,
    private var scoreText: TextView,
    private var heartsText: TextView,
    private var taskText: TextView,
    private var mathInstance: MathInstance,
    private var progressBar: ProgressBar) : Thread() {

    private val act = (activity as MainActivity)
    private val gameTransition = GameTransition(activity)
    private val gameLogic = TaskGeneration(act.applicationContext,mathInstance)

    private var task = ""
    private var answer = 0

    companion object {
        const val TAG = "ANSWER_FRAGMENT"
    }

    override fun run() {
        generateTask()
        setListeners()
    }



    @SuppressLint("SetTextI18n")
    private fun nextTask() {
        act.vibrate()

        if (e1.text.toString() == answer.toString()){
            mathInstance.score++
            act.playSound(R.raw.correc2)
            gameTransition.decideWhichFragment(mathInstance, TAG)
        }else{
            mathInstance.hearts--
            act.playSound(R.raw.wrong1)
            (activity as MainActivity).openFragment(CorrectAnswerFragment(mathInstance, TAG,"$task $answer"))
        }

    }

    private fun setListeners() {
        b1.setOnClickListener{
            nextTask()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun generateTask() {
        task = gameLogic.getTask()
        answer = gameLogic.getAnswer()
        heartsText.text = mathInstance.hearts.toString()
        scoreText.text = "${mathInstance.score}/${GameTransition.DEFAULT_MAX_SCORE}"
        taskText.text = task
        gameTransition.checkForInfinitePlay(mathInstance, progressBar)
        debug()
    }

    private fun debug() {
        if (taskText.text == null || taskText.text == ""){
            generateTask()
        }
        if (mathInstance.hearts < 0){
            act.openFragment(ResultFragment(mathInstance.score, mathInstance),true)
        }
    }
}


