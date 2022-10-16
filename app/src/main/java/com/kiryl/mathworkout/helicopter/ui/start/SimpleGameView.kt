package com.kiryl.mathworkout.helicopter.ui.start

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.kiryl.mathworkout.R
import com.kiryl.mathworkout.helicopter.sprites.Buildings


class SimpleGameView(context: Context, attributes: AttributeSet) : SurfaceView(context, attributes), SurfaceHolder.Callback{
    private var thread: SimpleGameThread

    private var building1: Buildings? = null
    private var building2: Buildings? = null
    private var building3: Buildings? = null
    private var building4: Buildings? = null
    private var building5: Buildings? = null
    private var building6: Buildings? = null

    private var speed = -0F


    init {
        holder.addCallback(this)
        thread = SimpleGameThread(holder,this)
    }

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {

        building1 = Buildings(BitmapFactory.decodeResource(resources, R.drawable.concrete_day))
        building2 = Buildings(BitmapFactory.decodeResource(resources, R.drawable.monument_day))
        building3 = Buildings(BitmapFactory.decodeResource(resources, R.drawable.pentagon_day))
        building4 = Buildings(BitmapFactory.decodeResource(resources, R.drawable.skyscraper_day))
        building5 = Buildings(BitmapFactory.decodeResource(resources, R.drawable.high_skyscraper_day))
        building6 = Buildings(BitmapFactory.decodeResource(resources, R.drawable.square_day))

        try {
            thread.start()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
        thread = SimpleGameThread(surfaceHolder, this)
    }


    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        thread = SimpleGameThread(surfaceHolder, this)
    }

    fun update() {
        building1!!.update(speed)
        building2!!.update(speed)
        building3!!.update(speed)
        building4!!.update(speed)
        building5!!.update(speed)
        building6!!.update(speed)
    }


    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawARGB(255, 135, 206,255)
        building1!!.draw(canvas)
        building2!!.draw(canvas)
        building3!!.draw(canvas)
        building4!!.draw(canvas)
        building5!!.draw(canvas)
        building6!!.draw(canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return true
    }
}



class SimpleGameThread(private val surfaceHolder: SurfaceHolder, private val simpleGameView: SimpleGameView) : Thread() {

    private var targetFPS = 60

    companion object {
        private var canvas: Canvas? = null
    }

    override fun run() {
        var startTime: Long
        var timeMillis: Long
        var waitTime: Long

        val targetTime = (1000 / targetFPS).toLong()
        while (HelicopterStartFragment.isRunning) {
            startTime = System.nanoTime()
            canvas = null
            try {
                canvas = this.surfaceHolder.lockCanvas()
                synchronized(surfaceHolder) {
                    this.simpleGameView.update()
                    this.simpleGameView.draw(canvas!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (canvas != null) try {surfaceHolder.unlockCanvasAndPost(canvas) }catch (_: Exception) { }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000
            waitTime = targetTime - timeMillis
            try {sleep(waitTime) } catch (_: Exception) { }
        }
    }
}
