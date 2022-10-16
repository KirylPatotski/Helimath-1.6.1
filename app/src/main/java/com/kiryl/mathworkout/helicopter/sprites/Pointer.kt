package com.kiryl.mathworkout.helicopter.sprites

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import kotlin.math.roundToInt


class Pointer(var image: Bitmap) {
    var x: Int = 0
    var y: Int = 0
    var w: Int = 0
    var h: Int = 0
    var collider: Rect
    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels

    init {
        w = image.width
        h = image.height
        x = screenWidth-w
        y = screenHeight/2
        collider = Rect(this.x, this.y, this.x+this.w, this.y+ this.h)
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, x.toFloat(), y.toFloat(), null)
    }

    fun updateCollider(){
        this.collider = Rect(this.x, this.y, this.x + this.w, this.y + this.h)
    }

}

