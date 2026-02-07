package lserebri.goalflow.data

import androidx.room.Database
import androidx.room.RoomDatabase
import lserebri.goalflow.data.distraction.Distraction
import lserebri.goalflow.data.distraction.DistractionDao
import lserebri.goalflow.data.goal.Goal
import lserebri.goalflow.data.goal.GoalDao
import lserebri.goalflow.data.score.Score
import lserebri.goalflow.data.score.ScoreDao

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