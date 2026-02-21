package lserebri.goalflow.data

import androidx.room.Database
import androidx.room.RoomDatabase
import lserebri.goalflow.data.distraction.Distraction
import lserebri.goalflow.data.distraction.DistractionDao
import lserebri.goalflow.data.goal.Goal
import lserebri.goalflow.data.goal.GoalDao
import lserebri.goalflow.data.progress.UserProgress
import lserebri.goalflow.data.progress.UserProgressDao

@Database(
	entities = [Goal::class, Distraction::class, UserProgress::class],
	version = 4,
	exportSchema = false
)
abstract class GoalFlowDatabase : RoomDatabase() {
	abstract fun goalDao(): GoalDao
	abstract fun distractionDao(): DistractionDao
	abstract fun userProgressDao(): UserProgressDao
}