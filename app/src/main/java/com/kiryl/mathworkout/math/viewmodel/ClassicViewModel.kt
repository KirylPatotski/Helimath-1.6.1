package com.kiryl.mathworkout.math.viewmodel

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.kiryl.mathworkout.MainActivity
import com.kiryl.mathworkout.R
import com.kiryl.mathworkout.math.view.CorrectAnswerFragment
import com.kiryl.mathworkout.math.viewmodel.general.GameTransition
import com.kiryl.mathworkout.math.viewmodel.general.TaskGeneration
import com.kiryl.mathworkout.math.viewmodel.general.model.MathInstance
import kotlin.random.Random

class ClassicViewModel(private var activity: Activity, private var b1: Button, private var b2: Button, private var b3: Button, private var b4: Button, private var scoreText: TextView, private var heartsText: TextView, private var taskText: TextView, private var progressBar: ProgressBar, private var mathInstance: MathInstance) : Thread() {


    private val act = (activity as MainActivity)
    private val gameLogic = TaskGeneration(act.applicationContext,mathInstance)
    private val gameTransition = GameTransition(act)

    private var task = ""
    var answer = 0

    companion object {
        const val TAG = "CLASSIC_FRAGMENT"
    }

    override fun run() {
        generateTask()
        setListeners()
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor", "WrongConstant")
    private fun nextTask(button: Button) {
        act.vibrate()

        if (button.text.equals(answer.toString())) {
            mathInstance.score++
            act.playSound(R.raw.correc2)
            gameTransition.decideWhichFragment(mathInstance, TAG)
        }else{
            mathInstance.hearts--
            showButtonColors()
            act.playSound(R.raw.wrong1)
            (activity as MainActivity).openFragment(CorrectAnswerFragment(mathInstance, TAG,"$task $answer"))
        }

    }

    private fun setListeners() {
        b1.setOnClickListener {
            nextTask(b1)
        }

        b2.setOnClickListener {
            nextTask(b2)
        }

        b3.setOnClickListener {
            nextTask(b3)
        }

        b4.setOnClickListener {
            nextTask(b4)
        }

        taskText.setOnClickListener {
            assignButton(b1, b2, b3, b4, answer, taskText)
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

        assignButton(b1,b2,b3,b4,answer,taskText)
        gameTransition.checkForInfinitePlay(mathInstance, progressBar)
        animateTime()

        if (b1.text == "") generateTask()
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
    private fun assignButton(b1: Button,b2: Button,b3: Button,b4: Button,localAnswer: Int,taskText: TextView) {

        val order = Random.nextInt(0, 25)

        val a1 = localAnswer + 1
        val a2 = localAnswer - 1
        var a3 = 0

        val answer3Index = Random.nextInt(0, 2)
        if (answer3Index == 0) {
            a3 = localAnswer - Random.nextInt(2, 4)
        }
        if (answer3Index == 1) {
            a3 = localAnswer + Random.nextInt(2, 4)
        }


        if (order == 0) {
            setTextOfButtons(b1, b2, b3, b4, a1, a2, a3, localAnswer, taskText)
        }
        if (order == 2) {
            setTextOfButtons(b1, b2, b4, b3, a1, a2, a3, localAnswer, taskText)
        }
        if (order == 3) {
            setTextOfButtons(b1, b3, b2, b4, a1, a2, a3, localAnswer, taskText)
        }
        if (order == 4) {
            setTextOfButtons(b1, b3, b4, b2, a1, a2, a3, localAnswer, taskText)
        }
        if (order == 5) {
            setTextOfButtons(b1, b4, b2, b3, a1, a2, a3, localAnswer, taskText)
        }
        if (order == 6) {
            setTextOfButtons(b1, b4, b3, b2, a1, a2, a3, localAnswer, taskText)
        }



        if (order == 7) {
            setTextOfButtons(b2, b1, b3, b4, a1, a2, a3, localAnswer, taskText)
        }
        if (order == 8) {
            setTextOfButtons(b2, b1, b4, b3, a1, a2, a3, localAnswer, taskText)
        }
        if (order == 9) {
            setTextOfButtons(b2, b3, b1, b4, a1, a2, a3, localAnswer, taskText)
        }
        if (order == 10) {
            setTextOfButtons(b2, b3, b4, b1, a1, a2, a3, localAnswer, taskText)
        }
        if (order == 11) {
            setTextOfButtons(b2, b4, b1, b3, a1, a2, a3, localAnswer, taskText)
        }
        if (order == 12) {
            setTextOfButtons(b2, b4, b3, b1, a1, a2, a3, localAnswer, taskText)
        }

        if (order == 13) {
            setTextOfButtons(b3, b1, b2, b4, a1, a2, a3, localAnswer, taskText)
        }
        if (order == 14) {
            setTextOfButtons(b3, b1, b4, b2, a1, a2, a3, localAnswer, taskText)
        }
        if (order == 15) {
            setTextOfButtons(b3, b2, b1, b4, a1, a2, a3, localAnswer, taskText)
        }
        if (order == 16) {
            setTextOfButtons(b3, b2, b4, b1, a1, a2, a3, localAnswer, taskText)
        }
        if (order == 17) {
            setTextOfButtons(b3, b4, b1, b2, a1, a2, a3, localAnswer, taskText)
        }
        if (order == 18) {
            setTextOfButtons(b3, b4, b2, b1, a1, a2, a3, localAnswer, taskText)
        }


        if (order == 19) {
            setTextOfButtons(b4, b1, b2, b3, a1, a2, a3, localAnswer, taskText)
        }
        if (order == 20) {
            setTextOfButtons(b4, b1, b3, b2, a1, a2, a3, localAnswer, taskText)
        }
        if (order == 21) {
            setTextOfButtons(b4, b2, b1, b3, a1, a2, a3, localAnswer, taskText)
        }
        if (order == 22) {
            setTextOfButtons(b4, b2, b3, b1, a1, a2, a3, localAnswer, taskText)
        }
        if (order == 23) {
            setTextOfButtons(b4, b3, b1, b2, a1, a2, a3, localAnswer, taskText)
        }
        if (order == 24) {
            setTextOfButtons(b4, b3, b2, b1, a1, a2, a3, localAnswer, taskText)
        }
    }

    private fun setTextOfButtons(b1: Button, b2: Button, b3: Button, b4: Button, answer1: Int, answer2: Int, answer3: Int, localAnswer: Int, taskText: TextView) {
        b1.text = localAnswer.toString()
        b2.text = answer1.toString()
        b3.text = answer2.toString()
        b4.text = answer3.toString()
        taskText.text = task
    }

    private fun showButtonColors(){
        if (b1.text == answer.toString()) {
            b1.setBackgroundColor(Color.GREEN)
        }
        if (b1.text != answer.toString()) {
            b1.setBackgroundColor(Color.RED)
        }
        if (b2.text == answer.toString()) {
            b2.setBackgroundColor(Color.GREEN)
        }
        if (b2.text != answer.toString()) {
            b2.setBackgroundColor(Color.RED)
        }
        if (b3.text == answer.toString()) {
            b3.setBackgroundColor(Color.GREEN)
        }
        if (b3.text != answer.toString()) {
            b3.setBackgroundColor(Color.RED)
        }
        if (b4.text != answer.toString()) {
            b4.setBackgroundColor(Color.RED)
        }
        if (b4.text == answer.toString()) {
            b4.setBackgroundColor(Color.GREEN)
        }
    }

}


