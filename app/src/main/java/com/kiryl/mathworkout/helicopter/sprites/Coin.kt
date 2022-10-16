package com.kiryl.mathworkout.helicopter.sprites

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import kotlin.math.roundToInt
import kotlin.random.Random


class Coin(private var image: Bitmap, private val worth: Int? = 1, private val rarity: Int? = 1) {
    var x: Int = 0
    var y: Int = 0
    var w: Int = 0
    var h: Int = 0
    private var xVelocity = 6
    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels
    var collider: Rect

    init {
        w = image.width
        h = image.height
        x = screenWidth+image.width + Random.nextInt(0,2000)
        y = Random.nextInt(0,screenHeight)
        collider = Rect(this.x, this.y, this.x+this.w, this.y+ this.h)
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, x.toFloat(), y.toFloat(), null)
    }

    fun update(speed:Float) {
        collider = Rect(this.x, this.y, this.x+this.w, this.y+ this.h)
        x -= (xVelocity+speed).roundToInt()

        if (x < 0-image.width+100){
            reset()
        }
    }

    fun reset(): Int {
        x = screenWidth+1000 +image.width + Random.nextInt(0,1000)
        if (rarity != null){
            x += rarity * Random.nextInt(0,1000)
        }
        y = Random.nextInt(0,screenHeight-image.height)
        return worth ?: 1
    }


}



