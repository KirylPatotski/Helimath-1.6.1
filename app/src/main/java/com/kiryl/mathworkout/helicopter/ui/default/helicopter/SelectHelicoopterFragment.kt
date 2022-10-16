package com.kiryl.mathworkout.helicopter.ui.default.helicopter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kiryl.mathworkout.MainActivity
import com.kiryl.mathworkout.R
import com.kiryl.mathworkout.helicopter.mechanics.GameData
import com.kiryl.mathworkout.helicopter.ui.shop.Items


class SelectHelicopterFragment : Fragment() {


    private lateinit var recyclerview: RecyclerView
    private lateinit var gameData: GameData

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_select_helicopter, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameData = GameData(requireContext())
        recyclerview = view.findViewById(R.id.select_recycler_view)
        recyclerview.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)


        val items = Items()
        val data = items.returnData(requireContext())
        val adapter = CustomAdapter(data,requireContext(),(activity as MainActivity))
        recyclerview.adapter = adapter
    }

}
