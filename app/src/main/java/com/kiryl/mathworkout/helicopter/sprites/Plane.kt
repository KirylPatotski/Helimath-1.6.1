package com.kiryl.mathworkout.helicopter.sprites

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import kotlin.math.roundToInt
import kotlin.random.Random

class Plane(var image: Bitmap) {
    var x: Int = 0
    var y: Int = 0
    var w: Int = 0
    var h: Int = 0
    var hasPointer = false
    var pointer: Pointer? = null
    private var xVelocity = 16
    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels
    var collider: Rect


    init {
        w = image.width
        h = image.height
        x =  screenWidth+1000 + Random.nextInt(0,3000)

        y = Random.nextInt(0,screenHeight-image.height)
        collider = Rect(this.x, this.y, this.x+this.w, this.y+ this.h)

    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, x.toFloat(), y.toFloat(), null)
    }

    fun update(speed:Float) {
        collider = Rect(this.x, this.y, this.x+this.w, this.y+ this.h)
        x -= (xVelocity+speed).roundToInt()
    }

    fun needsPointer(): Boolean {
        return this.x > screenWidth
    }

    fun removePointer(){
        this.pointer = null
    }

    fun exitedScreen(): Boolean {
        return this.x+this.image.width < 0
    }
}

