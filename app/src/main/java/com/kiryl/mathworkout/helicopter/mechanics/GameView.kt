package com.kiryl.mathworkout.helicopter.mechanics

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.kiryl.mathworkout.MainActivity
import com.kiryl.mathworkout.R
import com.kiryl.mathworkout.helicopter.sprites.*
import com.kiryl.mathworkout.helicopter.ui.shop.Items
import com.kiryl.mathworkout.helicopter.ui.start.HelicopterStartFragment
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.system.exitProcess


class GameView(context: Context, attributes: AttributeSet) : SurfaceView(context, attributes), SurfaceHolder.Callback{

    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels

    private var building1: Buildings? = null
    private var building2: Buildings? = null
    private var building3: Buildings? = null
    private var building4: Buildings? = null
    private var building5: Buildings? = null
    private var building6: Buildings? = null

    private var explosion: Buildings? = null
    private var player: Player? = null
    private var rocket: Rocket? = null
    private var projectileToken: Coin? = null
    private var magnet: Coin? = null

    private var touched: Boolean = false
    private var touchedX: Int = 0
    private var touchedY: Int = 0

    private var paint = Paint()
    private var speed = -2.25F
    private var initialSpeed = speed
    private var score = speed-initialSpeed
    private var time = (score*16.6666666).roundToInt()
    private var projectileTokens = 0

    private var gameData: GameData
    private var magnetIsActive: Boolean = false

    private var projectileList = ArrayList<Projectile>()
    private var planeList = ArrayList<Plane>()
    private var coinList = ArrayList<Coin>()
    private var pointerList = ArrayList<Pointer>()

    val fps = FPS(context)
    private val currentFPS = fps.getFPS()

    init {
        holder.addCallback(this)
        thread = GameThread(holder,this,context)

        paint.style = Paint.Style.FILL
        paint.color = Color.YELLOW
        paint.textSize = 70F
        gameData = GameData(context)
    }



    companion object{
        private const val BRONZE_COIN_VALUE = 1
        private const val SILVER_COIN_VALUE = 2
        private const val GOLDEN_COIN_VALUE = 3
        private const val DEFAULT_SPEED = -2.25F
        private const val DEFAULT_HEARTS = 3

        var shoot = false
        var shootRocket = false
        var hearts = DEFAULT_HEARTS
        private var alreadySaved = false
        var coins = 0

        lateinit var thread: GameThread

        fun continueGame(){
            try {
                thread.setRunning(true)
                thread.start()
                hearts = DEFAULT_HEARTS
                alreadySaved = false
            }catch (e:Exception){
                println("Couldn't start thread")
                val gameFragment = GameFragment()
                (gameFragment.activity as MainActivity).openFragment(HelicopterStartFragment(),true)
            }
        }

    }


    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        alreadySaved = false
        hearts = DEFAULT_HEARTS
        speed = DEFAULT_SPEED
        coins = 0
        projectileTokens = 0


        val buildingsIndex = 0
        building1 = Buildings(BitmapFactory.decodeResource(resources, Items.buildingsPack[buildingsIndex][0]))
        building2 = Buildings(BitmapFactory.decodeResource(resources, Items.buildingsPack[buildingsIndex][1]))
        building3 = Buildings(BitmapFactory.decodeResource(resources, Items.buildingsPack[buildingsIndex][2]))
        building4 = Buildings(BitmapFactory.decodeResource(resources, Items.buildingsPack[buildingsIndex][3]))
        building5 = Buildings(BitmapFactory.decodeResource(resources, Items.buildingsPack[buildingsIndex][4]))
        building6 = Buildings(BitmapFactory.decodeResource(resources, Items.buildingsPack[buildingsIndex][5]))


        rocket = Rocket(BitmapFactory.decodeResource(resources,R.drawable.rocket))
        projectileToken = Coin(BitmapFactory.decodeResource(resources, R.drawable.projectile_all_token),null,3)
        explosion = Buildings(BitmapFactory.decodeResource(resources,R.drawable.explosion))
        magnet = Coin(BitmapFactory.decodeResource(resources,R.drawable.magnet),null, 4)

        val index = gameData.getInt(GameData.SELECTED_HELICOPTER_PREF_KEY)
        player = Player(BitmapFactory.decodeResource(resources, Items.drawableArray[index]))

        if (index == 3) projectileTokens++
        if (index == 4) hearts++


        thread.setRunning(true)

        try {
            thread.start()
        }catch (e:Exception){
            exitProcess(0)
        }

    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
        println("Changed GameView")
        thread = GameThread(surfaceHolder, gameView = this,context)
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        println("Destroyed GameView")
        thread.setRunning(false)
        thread = GameThread(surfaceHolder, gameView = this,context)
    }

    private fun updatePointer(){
        val pointersToRemove = ArrayList<Pointer>()
        for (pointer in pointerList) {
            pointer.updateCollider()
            if (rocket!!.collider.intersect(pointer.collider))
                pointersToRemove.add(pointer)
        }
        if (pointersToRemove.isNotEmpty())for (pointers in pointersToRemove) pointerList.remove(pointers)

        for (plane in planeList){
            if (plane.needsPointer() && !plane.hasPointer) {
                pointerList.add(Pointer(BitmapFactory.decodeResource(resources, R.drawable.pointer)))
                plane.hasPointer = true
                pointerList[pointerList.size-1].y = plane.y
                plane.pointer = pointerList[pointerList.size-1]
            }
        }

        for (planes in planeList) {
            if (planes.pointer != null) {
                planes.pointer?.updateCollider()
                if (planes.x < screenWidth){
                    pointerList.remove(planes.pointer)
                }
            }
        }
    }

    fun update() {

        building1!!.update(speed)
        building2!!.update(speed)
        building3!!.update(speed)
        building4!!.update(speed)
        building5!!.update(speed)
        building6!!.update(speed)


        updateRocketToken()
        updatePlayer(player!!)

        checkForProjectile()
        checkForRocket()

        updateProjectile()
        updatePlane()
        updateCoins()

        updatePointer()
        updateMagnet(magnet!!)

        speed+=0.001F
        score = speed-initialSpeed

        if (hearts < 1 && !alreadySaved) {
            alreadySaved = true
            endGame()
        }

    }

    private fun updateMagnet(magnet:Coin){
        magnet.update(speed)

        if (magnet.collider.intersect(player!!.collider)){
            magnet.reset()
            magnetIsActive = true
        }
        if (magnetIsActive){
            for (coin in coinList){
                coin.y = player!!.y
            }
        }
        magnetIsActive = false
    }

    private fun updateRocketToken(){
        projectileToken!!.update(speed)
        if (projectileToken!!.collider.intersect(player!!.collider)){
            projectileTokens++
            projectileToken!!.reset()
        }
    }

    private fun generatePlanes(){
        if (planeList.size < 4){
           val newPlaneSkinIndex = Random.nextInt(0,6)
           val newPlane = Plane(BitmapFactory.decodeResource(resources, skinArray[newPlaneSkinIndex]))
            planeList.add(newPlane)
        }
    }

    private fun endGame(){
        thread.setRunning(false)
        var timePlayed = gameData.getInt(GameData.TIME_PLAYED_PREF_KEY)
        timePlayed+=time
        gameData.setInt(GameData.TIME_PLAYED_PREF_KEY,timePlayed)
    }

    private fun updatePlane() {
        generatePlanes()

        for (plane in planeList) {
            plane.update(speed)

            if (plane.exitedScreen()) {
                planeList.remove(plane)
                break
            }

            if (plane.collider.intersect(player!!.collider)) {
                hearts--
                explodeAndRemove(plane)
                break
            }

            if (plane.collider.intersect(rocket!!.collider)) {
                explodeAndRemove(plane)
                break
            }
        }
        val planesToRemove = ArrayList<Plane>()
        val projectilesToRemove = ArrayList<Projectile>()

        for (plane in planeList) {
            for (projectile in projectileList) {
                if (projectile.collider.intersect(plane.collider)) {
                    coins += 10
                    planesToRemove.add(plane)
                    projectilesToRemove.add(projectile)
                    explode(plane.x-plane.w/2,plane.y-plane.h/2)
                }
            }
        }
        for (planes in planesToRemove){
            planeList.remove(planes)
        }
        for (projectiles in projectilesToRemove){
            projectileList.remove(projectiles)
        }
    }

    private fun updateProjectile(){
        for(projectile in projectileList){
            projectile.update(speed)
        }
        for (projectile in projectileList){
            if (projectile.x > screenWidth+projectile.w) {
                projectileList.remove(projectile)
                break
            }
        }
    }

    private fun explodeAndRemove(plane: Plane){
        explode(plane.x-plane.w/2,plane.y-plane.h/2)
        planeList.remove(plane)
    }

    private fun updateCoins() {
        if (coinList.size < 10){
            val coinIndex = Random.nextInt(0,3)
            lateinit var coin: Coin
            when(coinIndex) {
                0 -> coin = Coin(BitmapFactory.decodeResource(resources, R.drawable.bronze_coin), BRONZE_COIN_VALUE)
                1 -> coin = Coin(BitmapFactory.decodeResource(resources, R.drawable.silver_coin), SILVER_COIN_VALUE)
                2 -> coin = Coin(BitmapFactory.decodeResource(resources, R.drawable.golden_coin), GOLDEN_COIN_VALUE)
            }
            coinList.add(coin)
        }

        val coinsToDelete = ArrayList<Coin>()

        for (coin in coinList) {
            coin.update(speed)
            if (coin.collider.intersect(player!!.collider)) {
                coins += coin.reset()
                return
            }
            if (rocket!!.collider.intersect(coin.collider)) {
                coins += coin.reset()
                return
            }
            if (coin.x+100 < 0) coinsToDelete.add(coin)
        }
        for (coins in coinsToDelete) coinList.remove(coins)
    }

    private fun explode(x: Int, y: Int){
        explosion!!.x = x
        explosion!!.y = y
    }

    private fun updatePlayer(player: Player) {
        player.update(false)
        if (touched) player.update(true, touchedX, touchedY)
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


        for (plane in planeList)plane.draw(canvas)
        for (coin in coinList)coin.draw(canvas)
        for (projectile in projectileList)projectile.draw(canvas)
        for (pointer in pointerList) pointer.draw(canvas)

        magnet!!.draw(canvas)

        time = (score*16.6666666).roundToInt()
        canvas.drawText("${hearts}❤ $coins💵 ${time}s", screenWidth.toFloat()-1000 , 100F, paint)
        player!!.draw(canvas)

        rocket!!.draw(canvas)
        projectileToken!!.draw(canvas)
        explosion!!.draw(canvas)
        explosion!!.x = -1000
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        touchedX = event.x.toInt()
        touchedY = event.y.toInt()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> touched = true
            MotionEvent.ACTION_MOVE -> touched = true
            MotionEvent.ACTION_UP -> touched = false
            MotionEvent.ACTION_CANCEL -> touched = false
            MotionEvent.ACTION_OUTSIDE -> touched = false
        }
        return true
    }

    override fun onKeyUp(keyCode: Int, event: android.view.KeyEvent?): Boolean {
        return when (keyCode) {
            android.view.KeyEvent.KEYCODE_W -> {true}
            android.view.KeyEvent.KEYCODE_A -> {
                shoot = true
                true
            }
            android.view.KeyEvent.KEYCODE_S ->{true}
            android.view.KeyEvent.KEYCODE_D ->{true}

            else -> super.onKeyUp(keyCode, event)
        }
    }
    private fun checkForProjectile() {
        if (shoot) {
            val projectile = Projectile(BitmapFactory.decodeResource(resources, R.drawable.projectile))
            projectile.x = player!!.x + player!!.w / 2
            projectile.y = player!!.y + player!!.h / 2
            projectileList.add(projectile)
            shoot = false
        }
    }

    private fun checkForRocket() {
        if (rocket!!.x > screenWidth+rocket!!.w){
            rocket!!.x = player!!.x
            rocket!!.y = player!!.y
            shootRocket = false
            projectileTokens--
        }

        if (projectileTokens < 1){
            GameFragment.shootRocketButton.alpha = 0.5F
        }
        if (projectileTokens > 0) {
            GameFragment.shootRocketButton.alpha = 1.0F
            if (shootRocket) {
                rocket!!.start(speed)
                return
            }
        }
        shootRocket = false
            rocket!!.x = (x - rocket!!.w - 100).roundToInt()
            rocket!!.y = player!!.y - rocket!!.h / 2
    }

    private val skinArray = arrayOf(R.drawable.plane_blue,R.drawable.plane_red,R.drawable.plane_yellow,R.drawable.plane_green,R.drawable.plane_grey,R.drawable.plane_pink)

}
