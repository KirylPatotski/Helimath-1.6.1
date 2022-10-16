package com.kiryl.mathworkout.helicopter.sprites

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect

class Player(private val image: Bitmap) {
    var x: Int = 0
    var y: Int = 0
    val w: Int = image.width
    val h: Int = image.height
    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels
    var collider: Rect

    init {
        x = 0
        y = screenHeight/2
        collider = Rect(this.x, this.y, this.x+this.w, this.y+ this.h)
    }


    fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, x.toFloat(), y.toFloat(), null)
    }

    fun update(touch: Boolean, touchX: Int? = this.x, touchY: Int? = this.y) {
        if (touch) {
            if (touchX != null && touchX < screenWidth-image.width) x = touchX - w / 2

            y = touchY!! - h / 2
        }

        if (y > screenHeight-image.height){
            y = screenHeight-image.height
        }
        if (x < 0){
            x = 0
        }
        if (y < 0){
            y = 0
        }

        x-=2
        collider = Rect(this.x, this.y, this.x+this.w, this.y+ this.h)
    }

}