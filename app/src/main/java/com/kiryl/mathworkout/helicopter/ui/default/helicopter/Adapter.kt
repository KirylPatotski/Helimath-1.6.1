package com.kiryl.mathworkout.helicopter.ui.default.helicopter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kiryl.mathworkout.MainActivity
import com.kiryl.mathworkout.R
import com.kiryl.mathworkout.helicopter.mechanics.GameData
import com.kiryl.mathworkout.helicopter.mechanics.GameFragment
import com.kiryl.mathworkout.helicopter.ui.shop.ItemsViewModel

class CustomAdapter(private val mList: List<ItemsViewModel>, private val context: Context, private val activity: MainActivity) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_select_helicopter_item, parent, false)

        return ViewHolder(view)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]

        holder.titleView.text = itemsViewModel.title
        holder.imageView.setImageResource(itemsViewModel.image)
        holder.descriptionView.text = itemsViewModel.text

        holder.imageView.contentDescription = holder.titleView.text

        val gameData = GameData(context)

        val purchased = gameData.getBoolean(itemsViewModel.key)
        if (!purchased){
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(  0,0)
        }
        holder.itemView.setOnClickListener {
            selectHelicopter(gameData, position)
        }
        holder.descriptionView.setOnClickListener {
            selectHelicopter(gameData, position)
        }
        holder.titleView.setOnClickListener {
            selectHelicopter(gameData, position)
        }
    }

    private fun selectHelicopter(gameData: GameData, position: Int){
        gameData.setInt(GameData.SELECTED_HELICOPTER_PREF_KEY,position)
        println(gameData.getInt(GameData.SELECTED_HELICOPTER_PREF_KEY))
        activity.openFragment(GameFragment())
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val titleView: TextView = itemView.findViewById(R.id.title)
        val imageView: ImageView = itemView.findViewById(R.id.image)
        val descriptionView: TextView = itemView.findViewById(R.id.description)
    }

}
