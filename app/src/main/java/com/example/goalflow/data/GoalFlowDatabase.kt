package com.example.goalflow.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.goalflow.data.distraction.Distraction
import com.example.goalflow.data.distraction.DistractionDao
import com.example.goalflow.data.goal.Goal
import com.example.goalflow.data.goal.GoalDao
import com.example.goalflow.data.score.Score
import com.example.goalflow.data.score.ScoreDao

@Database(
	entities = [Goal::class, Distraction::class, Score::class],
	version = 3,
	exportSchema = false
)
abstract class GoalFlowDatabase : RoomDatabase() {
	abstract fun goalDao(): GoalDao
	abstract fun distractionDao(): DistractionDao
	abstract fun scoreDao(): ScoreDao
}