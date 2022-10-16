package com.kiryl.mathworkout.services.data.room.game

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "game_stats_table")
data class GameStats(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")var id: Int = 0,
    @ColumnInfo(name = "_date")var date: String = "",
    @ColumnInfo(name = "_averageTime")var timeUsed: Int = 0,
    @ColumnInfo(name = "_totalAnswers")var totalAnswers: Int = 0,
    @ColumnInfo(name = "_correctAnswers")var correctAnswer: Int = 0,

    @ColumnInfo(name = "_additionsUsed") var additionUsed: Int = 0,
    @ColumnInfo(name = "_subtractionUsed") var subtractionUsed: Int = 0,
    @ColumnInfo(name = "_multiplicationUsed") var multiplicationUsed: Int = 0,
    @ColumnInfo(name = "_divisionUsed") var divisionUsed: Int = 0,
    @ColumnInfo(name = "_powerUsed") var powerUsed: Int = 0,
    @ColumnInfo(name = "_logarithmUsed") var logarithmUsed: Int = 0,

    @ColumnInfo(name = "_difficulty")var difficulty: String = ""
)
