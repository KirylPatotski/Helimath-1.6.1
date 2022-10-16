package com.kiryl.mathworkout.helicopter.mechanics

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kiryl.mathworkout.MainActivity
import com.kiryl.mathworkout.R
import com.kiryl.mathworkout.ui.start.StartFragment


class GameFragment : Fragment(){

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var shootButton: ImageButton
        @SuppressLint("StaticFieldLeak")
        lateinit var shootRocketButton: ImageButton
    }


    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        println("Created View")
        return inflater.inflate(R.layout.helicopter_game_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shootButton = view.findViewById(R.id.game_shoot_button)
        shootRocketButton = view.findViewById(R.id.game_shoot_rocket_button)

        shootButton.setOnClickListener {
            vibrate(50)
            GameView.shoot = true
            if (checkForDialogue()) shootButton.visibility = View.GONE
        }

        shootRocketButton.setOnClickListener {
            vibrate(75)
            GameView.shootRocket = true
            if (checkForDialogue()) shootRocketButton.visibility = View.GONE
        }
    }

    private fun vibrate(time: Long) {
        (activity as MainActivity).vibrate(time)
    }

    private fun checkForDialogue():Boolean{
        if (GameView.hearts < 1) {
            val gameData = GameData(requireContext())
            val coins = GameView.coins
            var totalCoins = gameData.getInt(GameData.COINS_PREF_KEY)
            totalCoins+=coins
            gameData.setInt(GameData.COINS_PREF_KEY,totalCoins)
            println("Added $coins coins")


            val activity = (activity as MainActivity)
            val ct = requireContext()
            var tokens = gameData.getInt(GameData.REVIVE_TOKENS_PREF_KEY)
            val builder = AlertDialog.Builder(ct)
            builder.setTitle(ct.getString(R.string.Game_Over))
            builder.setMessage(ct.getString(R.string.Pay_1_to_revive))
            builder.setCancelable(false)
            builder.setPositiveButton(ct.getString(R.string.Yes)) { dialogue, _ ->
                if (tokens > 0) {
                    try {
                        GameView.continueGame()
                        tokens--
                        gameData.setInt(GameData.REVIVE_TOKENS_PREF_KEY,tokens)
                    } catch (e: Exception) {
                        activity.openFragment(StartFragment(), true)
                        Toast.makeText(ct, ct.getString(R.string.You_got_a_refund), Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(ct, ct.getString(R.string.You_have_0_tokens),Toast.LENGTH_SHORT).show()
                    dialogue.dismiss()
                    activity.openFragment(StartFragment(),true)
                }
            }
            builder.setNegativeButton(ct.getString(R.string.No)) { dialogue, _ ->
                dialogue.dismiss()
                activity.openFragment(StartFragment(),true)
            }

            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
            return true
        }else{
            return false
        }
    }
}



