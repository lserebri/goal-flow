package com.example.goalflow.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.goalflow.data.consumable.Consumable
import com.example.goalflow.data.consumable.ConsumableDao
import com.example.goalflow.data.goal.Goal
import com.example.goalflow.data.goal.GoalDao

@Database(entities = [Goal::class, Consumable::class], version = 2, exportSchema = false)
abstract class GoalFlowDatabase : RoomDatabase() {
    abstract fun goalDao(): GoalDao
    abstract fun consumableDao(): ConsumableDao
}