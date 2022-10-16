package com.kiryl.mathworkout


import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.kiryl.mathworkout.services.data.pref.AppData
import com.kiryl.mathworkout.services.noticication.NotificationReceiver
import com.kiryl.mathworkout.services.noticication.NotificationReceiver.Companion.CHANNEL_ID
import com.kiryl.mathworkout.services.noticication.NotificationReceiver.Companion.MESSAGE
import com.kiryl.mathworkout.services.noticication.NotificationReceiver.Companion.NOTIFICATION_ID
import com.kiryl.mathworkout.services.noticication.NotificationReceiver.Companion.TITLE
import com.kiryl.mathworkout.ui.miscellaneous.SettingFragment
import com.kiryl.mathworkout.ui.start.StartFragment
import java.util.*


class MainActivity : AppCompatActivity() {

    private var currentLanguage = "en"
    private var currentLang: String? = null
    private lateinit var locale: Locale

    private lateinit var appData : AppData

    companion object {
        const val NAVIGATION_EVENT = "navigation_event"
        const val NAVIGATION_EVENT_DATA_KEY = "navigation_event_data_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        appData = AppData(this)
        appData.addDiamonds(10000)
        appData = AppData(this)

        currentLanguage = intent.getStringExtra(currentLang).toString()
        val language = appData.getAnyString(AppData.LANGUAGE_PREF_KEY,"en").toString()
        setLocale(language)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.navigationBarColor = getColor(R.color.white)
        checkDarkLightMode()

        val firstTime = appData.getAnyBoolean(AppData.FIRST_TIME_PREF_KEY,true)
        if (firstTime){
            openFragment(SettingFragment())
            appData.setAnyBoolean(AppData.FIRST_TIME_PREF_KEY,false)
        }else{
            openFragment(StartFragment(), true, null)
        }
        listenNavigationEvents()
        if (appData.getDiamonds() < 0){
            appData.setAnyInt(AppData.DIAMONDS_PREF_KEY,0)
        }
    }

    fun checkDarkLightMode() {
        if (appData.getAnyBoolean(AppData.DARK_MODE_PREF_KEY)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        if (!appData.getAnyBoolean(AppData.DARK_MODE_PREF_KEY)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        delegate.applyDayNight()
    }


    override fun onBackPressed() {
        val fragmentCount = supportFragmentManager.backStackEntryCount
        if (fragmentCount > 1) {
            super.onBackPressed()
        } else {
            openFragmentWithOutTransitions(StartFragment())
        }
    }

    fun openFragment(fragment: Fragment, doClearBackStack: Boolean = false, tag: String? = null) {
        if (doClearBackStack) {
            clearBackStack()
        }
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
            .replace(R.id.main_fragment_container, fragment, tag)
            .addToBackStack(null)
            .commit()
    }

    fun openFragmentWithOutTransitions(fragment: Fragment,doClearBackStack: Boolean = false,tag: String? = null) {
        if (doClearBackStack) {
            clearBackStack()
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_fragment_container, fragment, tag)
            .addToBackStack(null)
            .commit()
    }

    private fun clearBackStack() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    private fun listenNavigationEvents() {
        supportFragmentManager.setFragmentResultListener(NAVIGATION_EVENT, this) { _, bundle ->
            bundle.get(NAVIGATION_EVENT_DATA_KEY) as String
        }
    }



    fun vibrate(time: Long? = 100) {
        val vibrate = appData.getAnyBoolean(AppData.VIBRATIONS_PREF_KEY)
        if (vibrate) {
            val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (time == null){ vibrator.vibrate(100)}
            else{ vibrator.vibrate(time)}
        } else {
            println("Vibrations Disabled")
        }
    }

    fun playSound(id: Int){
        val soundEnabled = appData.getAnyBoolean(AppData.SOUND_TURNED_ON_PREF_KEY)
        if (soundEnabled) {
            var mMediaPlayer: MediaPlayer? = null
            if (mMediaPlayer == null) {
                mMediaPlayer = MediaPlayer.create(this, id)
            }
            mMediaPlayer?.start()
        }
    }

    fun languageDialogue(){
        val listItems = arrayOf("English", "Deutsch", "Русский")
        val languageList = arrayOf("en","de","ru")
        var language: String

        AlertDialog.Builder(this)
            .setSingleChoiceItems(listItems, 0, null)
            .setPositiveButton(getString(R.string.done)) { dialog, _ ->
                dialog.dismiss()
                val selectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
                println(selectedPosition.toString())
                language = languageList[selectedPosition]
                setLocale(language)
                openFragment(SettingFragment())
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .show()


    }

    private fun setLocale(localeName: String) {
            locale = Locale(localeName)
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = locale
            res.updateConfiguration(conf, dm)
        appData.setAnyString(AppData.LANGUAGE_PREF_KEY,localeName)
        scheduleNotification()
    }



    private fun scheduleNotification() {
        createNotificationChannel()
        val intent = Intent(applicationContext, NotificationReceiver::class.java)

        val title = "Daily reminder to practice"
        val message = ""

        intent.putExtra(TITLE, title)
        intent.putExtra(MESSAGE,message)

        val pendingIntent = PendingIntent.getBroadcast(applicationContext, NOTIFICATION_ID, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()

        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()
        dueDate.set(Calendar.HOUR_OF_DAY,6)
        dueDate.set(Calendar.MINUTE, 30)
        dueDate.set(Calendar.SECOND, 0)
        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }
        val timeDiff = dueDate.timeInMillis- currentDate.timeInMillis

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, dueDate.timeInMillis, pendingIntent)
    }

    private fun createNotificationChannel() {
        val name = "Notification Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

