package com.kiryl.mathworkout.helicopter.mechanics

import android.content.Context
import android.graphics.Canvas
import android.view.SurfaceHolder
import com.kiryl.mathworkout.helicopter.sprites.FPS


class GameThread(private val surfaceHolder: SurfaceHolder, private val gameView: GameView, private var context:Context) : Thread() {


    fun setRunning(isRunning: Boolean) {
        running = isRunning
    }

    private val fps = FPS(context)
    private val currentFPS = fps.getFPS()

    override fun run() {
        var startTime: Long
        var timeMillis: Long
        var waitTime: Long
        val targetTime = (1000/currentFPS).toLong()
        while (running) {
            startTime = System.nanoTime()
            canvas = null
            try {
                canvas = this.surfaceHolder.lockCanvas()
                synchronized(surfaceHolder) {
                    this.gameView.update()
                    this.gameView.draw(canvas!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas)
                    } catch (e: Exception) {
                    }
                }
            }
            timeMillis = (System.nanoTime() - startTime) / 1000000
            waitTime = targetTime - timeMillis
            try {
                sleep(waitTime)
            } catch (e: Exception) {
            }
        }
    }

    companion object {
        private var canvas: Canvas? = null
        var running = false
    }

}