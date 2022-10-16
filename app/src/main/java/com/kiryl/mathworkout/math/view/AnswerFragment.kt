package com.kiryl.mathworkout.math.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.kiryl.mathworkout.MainActivity
import com.kiryl.mathworkout.R
import com.kiryl.mathworkout.math.viewmodel.AnswerViewModel
import com.kiryl.mathworkout.math.viewmodel.general.model.MathInstance


class AnswerFragment(var mathInstance: MathInstance) : Fragment() {
    private lateinit var button1: TextView
    private lateinit var editText: EditText
    private lateinit var taskText: TextView
    private lateinit var scoreText: TextView
    private lateinit var heartsText: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_game_answer, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskText = view.findViewById(R.id.task_text)
        button1 = view.findViewById(R.id.confirm_button)
        editText = view.findViewById(R.id.edit_text)
        scoreText = view.findViewById(R.id.score_text)
        heartsText = view.findViewById(R.id.hearts_text)
        progressBar = view.findViewById(R.id.progress)

        val game = AnswerViewModel((activity as MainActivity), button1,editText,scoreText,heartsText,taskText,mathInstance,progressBar)
        game.run()
    }
}