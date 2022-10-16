package com.kiryl.mathworkout.services.data.room.game

import androidx.room.*

@Dao
interface GameDao {

    @Query("SELECT * FROM game_stats_table")
    fun getAll(): List<GameStats>

    @Query("SELECT * FROM game_stats_table WHERE _id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<GameStats>

    @Query("SELECT * FROM game_stats_table WHERE _id LIKE :id")
    fun findById(id: Int): GameStats

    @Query("SELECT * FROM game_stats_table WHERE _date IN (:date)")
    fun loadByDateIds(date: String): List<GameStats>

    @Insert
    fun insertAll(vararg gameStats: GameStats)

    @Delete
    fun delete(gameStats: GameStats)
}