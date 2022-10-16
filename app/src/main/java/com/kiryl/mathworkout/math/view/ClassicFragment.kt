package com.kiryl.mathworkout.math.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.kiryl.mathworkout.MainActivity
import com.kiryl.mathworkout.R
import com.kiryl.mathworkout.math.viewmodel.ClassicViewModel
import com.kiryl.mathworkout.math.viewmodel.general.model.MathInstance


class ClassicFragment(var mathInstance: MathInstance) : Fragment() {


    private lateinit var b1: Button
    private lateinit var b2: Button
    private lateinit var b3: Button
    private lateinit var b4: Button

    private lateinit var taskText: TextView
    private lateinit var scoreText: TextView
    private lateinit var heartsText: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_game_classic, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskText = view.findViewById(R.id.task_text)
        b1 = view.findViewById(R.id.answer_1)
        b2 = view.findViewById(R.id.answer_2)
        b3 = view.findViewById(R.id.answer_3)
        b4 = view.findViewById(R.id.answer_4)

        progressBar = view.findViewById(R.id.progress)
        scoreText = view.findViewById(R.id.score_text)
        heartsText = view.findViewById(R.id.hearts_text)
        val game = ClassicViewModel((activity as MainActivity),b1,b2,b3,b4,scoreText,heartsText,taskText,progressBar,mathInstance)

        game.run()

    }
}