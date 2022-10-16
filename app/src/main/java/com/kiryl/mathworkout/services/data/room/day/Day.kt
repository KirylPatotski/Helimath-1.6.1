package com.kiryl.mathworkout.services.data.room.day

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "day_table")
data class Day(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")var id: Int = 0,
    @ColumnInfo(name = "_date")var date: String = "",
    @ColumnInfo(name =  "_xp") var xp : Int = 0,
    @ColumnInfo(name = "_diamonds") var diamonds: Int = 0
)
