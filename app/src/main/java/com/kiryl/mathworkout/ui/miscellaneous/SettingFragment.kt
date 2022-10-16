package com.kiryl.mathworkout.ui.miscellaneous

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.kiryl.mathworkout.MainActivity
import com.kiryl.mathworkout.R
import com.kiryl.mathworkout.helicopter.sprites.FPS
import com.kiryl.mathworkout.services.data.pref.AppData
import com.kiryl.mathworkout.ui.start.StartFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random.Default.nextInt


class SettingFragment : Fragment() {

    private lateinit var appData: AppData

    private lateinit var continueButton: Button
    private lateinit var languageButton: Button

    private lateinit var privacyPolicy: Chip
    private lateinit var infoButton: Chip
    private lateinit var fpsButton: Chip
    private lateinit var tiktokButton: Button
    private lateinit var youtubeButton: Chip
    private lateinit var emailButton: Chip

    private lateinit var darkMode: CheckBox
    private lateinit var vibrations: CheckBox
    private lateinit var soundCheckBox: CheckBox
    private lateinit var notificationCheckBox: CheckBox

    private lateinit var surpriseButton: Button



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_settings, container, false)
    }

    private lateinit var act: MainActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appData = AppData(requireContext())

        privacyPolicy = view.findViewById(R.id.privacyButton)
        infoButton = view.findViewById(R.id.infoButton)
        darkMode = view.findViewById(R.id.darkModeCheckBox)
        vibrations = view.findViewById(R.id.vibrationsCheckBox)
        soundCheckBox = view.findViewById(R.id.soundCheckBox)
        continueButton = view.findViewById(R.id.continue_button)
        languageButton = view.findViewById(R.id.languageButton)
        fpsButton = view.findViewById(R.id.fps)
        youtubeButton = view.findViewById(R.id.youtubeButton)
        emailButton = view.findViewById(R.id.emailButton)
        tiktokButton = view.findViewById(R.id.tiktok)
        surpriseButton = view.findViewById(R.id.surprise_button)





        setListeners()
        restoreDefaults()
        radio(view)
    }

    private fun radio(view: View){
        val radioGroup: RadioGroup = view.findViewById(R.id.radio_group)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            println(checkedId)
            var index: Int? = null
            when(checkedId){
                R.id.last_100->index = 100
                R.id.last_10->index = 15
                R.id.all->index = 0
            }
            appData.setAnyInt(AppData.LAST_ELEMENS_TO_LOAD_IN_DIAGRAM_PREF_KEY,index!!)
        }
    }

    private fun setListeners() {
        act = (activity as MainActivity)

        privacyPolicy.setOnClickListener {
            openLink("https://www.freeprivacypolicy.com/live/fd0f1ba1-4795-4914-8791-4b74bad8b033")
        }
        view?.setOnClickListener {
            restoreDefaults()
        }
        soundCheckBox.setOnClickListener {
            save(AppData.SOUND_TURNED_ON_PREF_KEY,soundCheckBox.isChecked)
        }
        darkMode.setOnClickListener {
            save(AppData.DARK_MODE_PREF_KEY, darkMode.isChecked)
            try { (activity as MainActivity).checkDarkLightMode() }catch(e: Exception){ Toast.makeText(requireContext(),R.string.Something_went_wrong,Toast.LENGTH_SHORT).show()}
        }
        tiktokButton.setOnClickListener {
            openLink("https://www.tiktok.com/@kirylpatotski")
        }
        vibrations.setOnClickListener {
            save(AppData.VIBRATIONS_PREF_KEY, vibrations.isChecked)
        }

        infoButton.setOnClickListener {
            act.openFragment(InfoFragment())
        }
        continueButton.setOnClickListener {
            act.openFragment(StartFragment())
        }
        languageButton.setOnClickListener {
            act.languageDialogue()
        }
        youtubeButton.setOnClickListener {
            openLink("https://www.youtube.com/channel/UCB1sJFWLxpsWq5Yfzfbnx-w?sub_confirmation=1")
        }
        fpsButton.setOnClickListener {
            setFPS()
        }

        surpriseButton.setOnClickListener {
            appData.addDiamonds(100)
            Toast.makeText(requireContext(),"You scrolled 20 meters", Toast.LENGTH_LONG).show()
            Toast.makeText(requireContext(),"+100💎", Toast.LENGTH_LONG).show()
            val random = nextInt(0,10)
            if (random == 5) openLink("https://www.youtube.com/watch?v=dQw4w9WgXcQ")
        }
        emailButton.setOnClickListener {
            showEmailDialogue()
        }
    }

    private fun openLink(url: String){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    private fun showEmailDialogue(){
        val listItems = arrayOf("Help", "Bug Report", "Translation Error", "Feedback", "Suggestion/Idea", "Fan Mail", "Other")

        AlertDialog.Builder(requireContext())
            .setSingleChoiceItems(listItems, 0, null)

            .setPositiveButton(
                requireContext().getString(R.string.done)
            ) { dialog, _ ->
                dialog.dismiss()
                val selectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
                println(selectedPosition.toString())
                val subject = listItems[selectedPosition]
                askForNewsLetterPermission(subject)
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .show()

    }
    private fun askForNewsLetterPermission(subject: String) {
        var granted = appData.getAnyBoolean(AppData.CONSENT_TO_NEWS_LETTER_PREF_KEY,false)
        if (!granted) {
            AlertDialog.Builder(requireContext())
                .setMessage("Do you want to receive news, updates and other emails?")
                .setPositiveButton("Yes, I want") { _, _ ->
                    granted = true
                    appData.setAnyBoolean(AppData.CONSENT_TO_NEWS_LETTER_PREF_KEY,granted)
                    openEmail(subject,granted)
                }
                .setNegativeButton("No") { _, _ ->
                    granted = false
                    appData.setAnyBoolean(AppData.CONSENT_TO_NEWS_LETTER_PREF_KEY,granted)
                    openEmail(subject,granted)
                }
                .setCancelable(false)
                .show()
        }

        openEmail(subject,granted)

    }


    private fun openEmail(subject: String,consent: Boolean) {
        var emailText = ""
        if (consent) {
            emailText = "\n\n\n\n\n\n\n\n\n\n\n\n I want to subscribe to the newsletter"
        }
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        emailIntent.type = "vnd.android.cursor.item/email"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("k.patotski@outlook.com"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailText)
        startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email_using)))
    }

    private fun restoreDefaults(){
        darkMode.isChecked = appData.getAnyBoolean(AppData.DARK_MODE_PREF_KEY)
        vibrations.isChecked = appData.getAnyBoolean(AppData.VIBRATIONS_PREF_KEY)
        soundCheckBox.isChecked = appData.getAnyBoolean(AppData.SOUND_TURNED_ON_PREF_KEY,true)
        languageButton.text = requireContext().getString(R.string.current_language)
    }

    @SuppressLint("SimpleDateFormat")
    fun timeDialog(key: String, textView: TextView) {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            val time = SimpleDateFormat("HH:mm").format(cal.time)
            println(time)
            appData.setAnyString(key,time)
            textView.text = time
        }
        TimePickerDialog(requireContext(), timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    private fun save(key: String,value: Boolean){
        appData.setAnyBoolean(key,value)
    }


    private fun setFPS() {
        var index = 0
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("FPS")
        val styles = arrayOf("24", "30", "60", "90", "120", "144")
        val checkedItem = 0

        builder.setSingleChoiceItems(styles, checkedItem) { dialog, which ->

            when (which) {
                0 -> { index = 0}
                1 -> { index = 1}
                2 -> { index = 2}
                3 -> { index = 3}
                4 -> { index = 4}
                5 -> { index = 5}
            }
        }
        builder.setPositiveButton("✔") { dialog, _ ->
            dialog.dismiss()
            val fps = FPS(requireContext())
            fps.setFPS(styles[index].toInt())
        }


        val dialog = builder.create()
        dialog.show()
    }
}