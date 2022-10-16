package com.kiryl.mathworkout.services.data.room.day

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Day::class], version = 1)
abstract class DayAppDataBase : RoomDatabase() {
    abstract fun dayDao(): DayDao
}
