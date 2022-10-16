package com.kiryl.mathworkout.helicopter.sprites

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import kotlin.math.roundToInt
import kotlin.random.Random


class Buildings(var image: Bitmap) {
    var x: Int = 0
    var y: Int = 0
    var w: Int = 0
    var h: Int = 0
    private var xVelocity = 3
    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels

    init {
        w = image.width
        h = image.height
        x = Random.nextInt(0,3000)
        y = screenHeight - image.height
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, x.toFloat(), y.toFloat(), null)
    }

    fun update(speed: Float) {
        if (x < 0-image.width){
            x = screenWidth+1000 +image.width + Random.nextInt(1000,3000)
        }
        x -= (xVelocity+speed).roundToInt()
    }

}