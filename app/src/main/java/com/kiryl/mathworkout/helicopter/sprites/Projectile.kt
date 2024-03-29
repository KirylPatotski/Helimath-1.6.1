package com.kiryl.mathworkout.helicopter.sprites

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import kotlin.math.roundToInt


class Projectile(var image: Bitmap) {
    var x: Int = 0
    var y: Int = 0
    var w: Int = 0
    var h: Int = 0
    var collider: Rect
    var xVelocity = 32

    init {
        w = image.width
        h = image.height
        x = -4000
        collider = Rect(this.x, this.y, this.x+this.w, this.y+ this.h)
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, x.toFloat(), y.toFloat(), null)
    }

    fun update(speed: Float) {
        this.collider = Rect(this.x, this.y, this.x + this.w, this.y + this.h)
        x += (xVelocity + speed).roundToInt()
    }

}

