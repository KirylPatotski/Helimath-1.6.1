package com.kiryl.mathworkout.ui.results

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bullfrog.particle.IParticleManager
import com.bullfrog.particle.Particles
import com.bullfrog.particle.enum.Shape
import com.bullfrog.particle.particle.Rotation
import kotlin.random.Random

class Confetti(
    private var context: Context,
    private var container: ViewGroup,
    private var view: View,
    private var anchor: TextView
) {

    private var particleManager: IParticleManager? = null

    fun animate(duration: Long? = 100000L) {
        generateBitmapFromView(view)
        val animator = ValueAnimator.ofInt(0, 1)
        animator.duration = duration!!
        particleManager = Particles.with(context, container)
        particleManager!!.colorFromView(anchor)
            .particleNum(1000)
            .anchor(anchor)
            .shape(Shape.RECTANGLE, Shape.HOLLOW_RECTANGLE)
            .radius(1, 3600)
            .strokeWidth(1f)
            .size(16, 16)
            .rotation(Rotation(45))
            .bitmap(generateBitmapFromView(view))

        particleManager!!.start()
    }

    private fun generateBitmapFromView(view: View): Bitmap {
        val specWidth = View.MeasureSpec.makeMeasureSpec(Random.nextInt(0,10000), View.MeasureSpec.AT_MOST)
        val specHeight = View.MeasureSpec.makeMeasureSpec(Random.nextInt(0,10000), View.MeasureSpec.AT_MOST)
        view.measure(specWidth, specHeight)
        val width = view.measuredWidth
        val height = view.measuredHeight
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.layout(view.left, view.top, view.right, view.bottom)
        view.draw(canvas)
        return bitmap
    }
}
