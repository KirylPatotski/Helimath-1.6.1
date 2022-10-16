package com.kiryl.mathworkout.helicopter.ui.start

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kiryl.mathworkout.MainActivity
import com.kiryl.mathworkout.R
import com.kiryl.mathworkout.helicopter.mechanics.GameData
import com.kiryl.mathworkout.helicopter.ui.default.helicopter.SelectHelicopterFragment
import com.kiryl.mathworkout.helicopter.ui.shop.ShopFragment
import com.kiryl.mathworkout.services.data.pref.AppData
import com.kiryl.mathworkout.ui.start.StartFragment
import com.kiryl.mathworkout.ui.statistic.StatsFragment


class HelicopterStartFragment : Fragment() {

    private lateinit var b1: Button
    private lateinit var b2: Button
    private lateinit var b3: Button

    private lateinit var moneyDisplay: TextView

    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        isRunning = true
        return inflater.inflate(R.layout.layout_helicoptergame_start, container, false)
    }

    companion object{
        var isRunning = false
        const val DEFAULT_NAVI_ID = R.id.page_2
        const val DIAMONDS_PRICE = 250
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b1 = view.findViewById(R.id.b1)
        b2 = view.findViewById(R.id.b2)
        b3 = view.findViewById(R.id.b3)
        moneyDisplay = view.findViewById(R.id.money_display)
        bottomNavigationView = view.findViewById(R.id.bottom_navigation)

        b1.text = "${b1.text} ${DIAMONDS_PRICE}💎"

        val gameData = GameData(requireContext())
        gameData.setBoolean(GameData.PURCHASED_BLUE_HELICOPTER_PREF_KEY, true)




        setListeners()
        restore()
    }

    private fun setListeners(){
        b1.setOnClickListener {
            val appData = AppData(requireContext())
            if (appData.getDiamonds() > DIAMONDS_PRICE){
                appData.addDiamonds(-DIAMONDS_PRICE)
                (activity as MainActivity).openFragment(SelectHelicopterFragment())
            }else{
                Toast.makeText(requireContext(), requireContext().getString(R.string.Too_poor), Toast.LENGTH_SHORT).show()
            }
        }
        b2.setOnClickListener {
            (activity as MainActivity).openFragment(ShopFragment())
        }
        view?.setOnClickListener {
            restore()
        }
        b3.setOnClickListener {
            (activity as MainActivity).openFragment(RewardAdFragment())
        }

        bottomNavigationView.selectedItemId = DEFAULT_NAVI_ID
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.page_0 -> {
                    (activity as MainActivity).openFragmentWithOutTransitions(StatsFragment())
                    true
                }
                R.id.page_1 -> {
                    (activity as MainActivity).openFragmentWithOutTransitions(StartFragment())
                    true
                }
                R.id.page_2 -> {
                    true
                }
                else -> {
                    false
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun restore(){
        val gameData = GameData(requireContext())
        moneyDisplay.text = "${gameData.getInt(GameData.COINS_PREF_KEY)}💵\n${gameData.getInt(GameData.REVIVE_TOKENS_PREF_KEY)}📀"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isRunning = false
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
    }

    override fun onResume() {
        super.onResume()
        isRunning = true
    }

    override fun onStop() {
        super.onStop()
        isRunning = false
    }

    override fun onPause() {
        super.onPause()
        bottomNavigationView.selectedItemId = DEFAULT_NAVI_ID
    }



}
