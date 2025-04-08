package com.example.goalflow.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Goal::class], version = 1, exportSchema = false)
abstract class GoalFlowDatabase : RoomDatabase() {
    abstract fun goalDao(): GoalDao
}
