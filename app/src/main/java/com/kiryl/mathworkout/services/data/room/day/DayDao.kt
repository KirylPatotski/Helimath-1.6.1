package com.kiryl.mathworkout.services.data.room.day

import androidx.room.*


@Dao
interface DayDao {

    @Query("SELECT * FROM day_table")
    fun getAll(): List<Day>

    @Query("SELECT * FROM day_table WHERE _id IN (:ids)")
    fun loadAllByIds(ids: IntArray): List<Day>

    @Query("SELECT * FROM day_table WHERE _date IN (:date)")
    fun loadAllByIds(date: String): List<Day>

    @Query("SELECT * FROM day_table WHERE _id LIKE :id")
    fun findById(id: Int): Day

    @Query("SELECT * FROM day_table WHERE _date LIKE :date")
    fun findById(date: String): Day

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg day: Day)

    @Delete
    fun delete(day: Day)
}