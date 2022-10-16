package com.kiryl.mathworkout.math.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.kiryl.mathworkout.MainActivity
import com.kiryl.mathworkout.R
import com.kiryl.mathworkout.math.viewmodel.general.model.MathInstance
import com.kiryl.mathworkout.math.viewmodel.general.GameTransition


class CorrectAnswerFragment(var mathInstance: MathInstance, var TAG: String, var task: String) : Fragment() {

    private lateinit var continueButton: Button
    private lateinit var taskText: TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_game_correct_answer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskText = view.findViewById(R.id.task_text)
        continueButton = view.findViewById(R.id.confirm_button)


        taskText.text = task

        continueButton.setOnClickListener {
            continueGame()
        }
        view.setOnClickListener {
            continueGame()
        }
    }

    private fun continueGame(){
        val gameTransition = GameTransition((activity as MainActivity))
        gameTransition.decideWhichFragment(mathInstance, TAG)
    }
}


