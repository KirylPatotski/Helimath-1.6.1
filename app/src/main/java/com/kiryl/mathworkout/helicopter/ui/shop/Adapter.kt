package com.kiryl.game.game.ui.shop

import android.annotation.SuppressLint
import android.content.Context
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.kiryl.mathworkout.R
import com.kiryl.mathworkout.helicopter.mechanics.GameData
import com.kiryl.mathworkout.helicopter.ui.shop.ItemsViewModel
import com.kiryl.mathworkout.helicopter.ui.shop.ShopFragment

class CustomAdapter(private val mList: List<ItemsViewModel>, private val context: Context) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_shop_item, parent, false)

        return ViewHolder(view)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]

        holder.titleView.text = itemsViewModel.title
        holder.imageView.setImageResource(itemsViewModel.image)
        holder.priceView.text = "${itemsViewModel.price}💵"
        holder.descriptionView.text = itemsViewModel.text

        holder.imageView.contentDescription = holder.titleView.text

        val gameData = GameData(context)

        var purchased = gameData.getBoolean(itemsViewModel.key)
        if (purchased){
            holder.buyButton.alpha = 0.5F
        }
        println(purchased)

        holder.buyButton.setOnClickListener {
            purchased = gameData.getBoolean(itemsViewModel.key)
            buy(gameData,purchased,holder.buyButton, itemsViewModel.price,itemsViewModel.key)
        }
    }

    private fun buy(gameData: GameData, purchased:Boolean, buyButton: Button, price:Int, key:String){
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(50)
        var money = gameData.getInt(GameData.COINS_PREF_KEY)
        if (!purchased){
            println(purchased)
            if (money < price){
                Toast.makeText(context,context.getString(R.string.Too_poor), Toast.LENGTH_SHORT).show()
            }
            if (money >= price && !purchased) {
                money -= price
                gameData.setBoolean(key, true)
                gameData.setInt(GameData.COINS_PREF_KEY,money)
                buyButton.alpha = 0.5F
                Toast.makeText(context, context.getString(R.string.Successfully_purchased), Toast.LENGTH_LONG).show()

            }
        }else{
            Toast.makeText(context, context.getString(R.string.Already_purchased), Toast.LENGTH_LONG).show()
        }
        ShopFragment.updateButtonText(context)
    }



    override fun getItemCount(): Int {
        return mList.size
    }
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val titleView: TextView = itemView.findViewById(R.id.title)
        val imageView: ImageView = itemView.findViewById(R.id.image)
        val priceView: TextView = itemView.findViewById(R.id.price)
        val descriptionView: TextView = itemView.findViewById(R.id.description)
        val buyButton: Button = itemView.findViewById(R.id.buy)
    }

}
