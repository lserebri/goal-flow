package com.example.goalflow.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.goalflow.data.consumable.Consumable
import com.example.goalflow.data.consumable.ConsumableDao
import com.example.goalflow.data.goal.Goal
import com.example.goalflow.data.goal.GoalDao
import com.example.goalflow.data.score.Score
import com.example.goalflow.data.score.ScoreDao

@Database(
    entities = [Goal::class, Consumable::class, Score::class],
    version = 2,
    exportSchema = false
)
abstract class GoalFlowDatabase : RoomDatabase() {
    abstract fun goalDao(): GoalDao
    abstract fun consumableDao(): ConsumableDao
    abstract fun scoreDao(): ScoreDao
}