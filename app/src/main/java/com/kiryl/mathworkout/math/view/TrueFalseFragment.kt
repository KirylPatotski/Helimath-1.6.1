package com.kiryl.mathworkout.math.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.kiryl.mathworkout.MainActivity
import com.kiryl.mathworkout.R
import com.kiryl.mathworkout.math.viewmodel.general.model.MathInstance
import com.kiryl.mathworkout.math.viewmodel.TrueFalseViewModel


class TrueFalseFragment(var mathInstance: MathInstance) : Fragment() {

    private lateinit var button1: ImageView
    private lateinit var button2: ImageView

    private lateinit var taskText: TextView
    private lateinit var scoreText: TextView
    private lateinit var heartsText: TextView
    private lateinit var progressBar: ProgressBar


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_game_true_false, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskText = view.findViewById(R.id.task_text)
        button1 = view.findViewById(R.id.true_button)
        button2 = view.findViewById(R.id.false_button)

        scoreText = view.findViewById(R.id.score_text)
        heartsText = view.findViewById(R.id.hearts_text)
        progressBar = view.findViewById(R.id.progress)

        val game = TrueFalseViewModel(
            (activity as MainActivity),
            button1,
            button2,
            scoreText,
            heartsText,
            taskText,
            progressBar,
            mathInstance
        )

        game.run()


    }



}

