package com.example.goalflow.data.score

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scores")
data class Score(
    @PrimaryKey val id: Int = 0,
    val value: Int,
    @ColumnInfo(name = "score") val score: Int
)