package com.kiryl.mathworkout.helicopter.ui.shop

import android.content.Context
import com.kiryl.mathworkout.R
import com.kiryl.mathworkout.helicopter.mechanics.GameData


internal class Items {

    fun returnData(context: Context): ArrayList<ItemsViewModel> {
        val data = ArrayList<ItemsViewModel>()

        data.add(ItemsViewModel(context.getString(R.string.Blue_Helicopter), (R.drawable.helicopter_dark_blue), 0, context.getString(R.string.Starter_Helicopter), GameData.PURCHASED_BLUE_HELICOPTER_PREF_KEY))
        data.add(ItemsViewModel(context.getString(R.string.White_Helicopter), (R.drawable.helicopter_white), 1000, context.getString(R.string.Nice_Color), GameData.PURCHASED_WHITE_HELICOPTER_PREF_KEY))
        data.add(ItemsViewModel(context.getString(R.string.Gray_Helicopter),(R.drawable.helicopter_grey),2000,"", GameData.PURCHASED_GREY_HELICOPTER_PREF_KEY))
        data.add(ItemsViewModel(context.getString(R.string.Green_Helicopter), (R.drawable.helicopter_green), 10000, context.getString(R.string.Start_your_game_with_1_rocket), GameData.PURCHASED_GREEN_HELICOPTER_PREF_KEY))
        data.add(ItemsViewModel(context.getString(R.string.Red_Helicopter), (R.drawable.helicopter_red), 50000, context.getString(R.string._Hearts), GameData.PURCHASED_RED_HELICOPTER_PREF_KEY))

        return data
    }

    companion object {
        val drawableArray = arrayOf((R.drawable.helicopter_dark_blue),(R.drawable.helicopter_white),(R.drawable.helicopter_grey),(R.drawable.helicopter_green),(R.drawable.helicopter_red))

        private val dayBuildingsArray = arrayOf(R.drawable.concrete_day,R.drawable.monument_day,R.drawable.pentagon_day, R.drawable.skyscraper_day, R.drawable.high_skyscraper_day,R.drawable.square_day)
        private val nightBuildingsArray = arrayOf(R.drawable.concrete_night,R.drawable.monument_night,R.drawable.pentagon_night, R.drawable.skyscraper_nigth, R.drawable.high_skyscraper_night,R.drawable.square_night)

        val buildingsPack = arrayOf(dayBuildingsArray, nightBuildingsArray)
    }

}