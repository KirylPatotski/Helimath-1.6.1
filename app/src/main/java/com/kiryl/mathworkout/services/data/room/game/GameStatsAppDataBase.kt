package com.kiryl.mathworkout.services.data.room.game

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [GameStats::class], version = 1/** autoMigrations = [AutoMigration (from = 1, to = 2)],*/)
abstract class GameStatsAppDataBase : RoomDatabase() {
    abstract fun gameStatsDao(): GameDao
}
