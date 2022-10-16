package com.kiryl.mathworkout.services.noticication

import android.content.Context
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.ToDoubleBiFunction

class ExactDailyService(val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()
        dueDate.set(Calendar.HOUR_OF_DAY,10)
        dueDate.set(Calendar.MINUTE, 10)
        dueDate.set(Calendar.SECOND, 0)
        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }

        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
//        val dailyWorkRequest = OneTimeWorkRequestBuilder<TODO Add the worker class here>()
//            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
//            .build()
//
//        WorkManager.getInstance(applicationContext)
//            .enqueue(dailyWorkRequest)
//
        return Result.success()
    }

}