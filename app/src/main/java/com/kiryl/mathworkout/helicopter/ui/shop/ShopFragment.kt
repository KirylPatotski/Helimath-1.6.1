package com.kiryl.mathworkout.helicopter.ui.shop

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kiryl.mathworkout.MainActivity
import com.kiryl.mathworkout.R
import com.kiryl.mathworkout.helicopter.mechanics.GameData
import com.kiryl.mathworkout.helicopter.ui.default.helicopter.SelectHelicopterFragment
import com.kiryl.game.game.ui.shop.CustomAdapter


class ShopFragment : Fragment() {


    private lateinit var recyclerview: RecyclerView
    private lateinit var gameData: GameData
    private lateinit var selectHelicopterButton: Button


    companion object{
        @SuppressLint("StaticFieldLeak")
        private lateinit var moneyTextView: Button
        @SuppressLint("SetTextI18n")
        fun updateButtonText(context: Context){
            try {
                val gameData = GameData(context)
                moneyTextView.text = "${gameData.getInt(GameData.COINS_PREF_KEY)}💵"
            }catch (e:NullPointerException){

            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_shop, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameData = GameData(requireContext())
        recyclerview = view.findViewById(R.id.shop_recycler_view)
        recyclerview.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

        selectHelicopterButton = view.findViewById(R.id.select_helicopter_button)
        moneyTextView = view.findViewById(R.id.money_display)

        val items = Items()
        val data = items.returnData(context!!)
        val adapter = CustomAdapter(data,requireContext())
        recyclerview.adapter = adapter
        moneyTextView.text = "${gameData.getInt(GameData.COINS_PREF_KEY)}💵"

        setListeners()
//        sellAll(gameData)
    }

    private fun sellAll(gameData: GameData){
        gameData.setBoolean(GameData.PURCHASED_BLUE_HELICOPTER_PREF_KEY,false)
        gameData.setBoolean(GameData.PURCHASED_RED_HELICOPTER_PREF_KEY,false)
        gameData.setBoolean(GameData.PURCHASED_GREEN_HELICOPTER_PREF_KEY,false)
        gameData.setBoolean(GameData.PURCHASED_WHITE_HELICOPTER_PREF_KEY,false)
        gameData.setBoolean(GameData.PURCHASED_GREY_HELICOPTER_PREF_KEY,false)
        gameData.setInt(GameData.COINS_PREF_KEY,65000)
    }

    private fun setListeners(){
        selectHelicopterButton.setOnClickListener {
            (activity as MainActivity).openFragment(SelectHelicopterFragment())
        }
    }


}
