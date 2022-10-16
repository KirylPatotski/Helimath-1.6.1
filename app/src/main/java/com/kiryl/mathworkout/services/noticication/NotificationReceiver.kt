package com.kiryl.mathworkout.services.noticication

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.kiryl.mathworkout.R


class NotificationReceiver : BroadcastReceiver() {

    companion object{
        const val NOTIFICATION_ID = 0
        const val CHANNEL_ID = "channel"
        const val TITLE = "title"
        const val MESSAGE = "message"
    }
    override fun onReceive(context: Context, intent: Intent) {
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.earth_2)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_round))
            .setContentTitle(intent.getStringExtra(TITLE))
            .setContentText(intent.getStringExtra(MESSAGE))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val  manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }

}