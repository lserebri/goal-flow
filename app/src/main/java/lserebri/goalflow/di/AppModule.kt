package lserebri.goalflow.di

import android.app.Application
import androidx.room.Room
import lserebri.goalflow.data.goal.GoalDao
import lserebri.goalflow.data.GoalFlowDatabase
import lserebri.goalflow.data.activity.ActivityRepository
import lserebri.goalflow.data.distraction.Distraction
import lserebri.goalflow.data.distraction.DistractionDao
import lserebri.goalflow.data.distraction.DistractionRepository
import lserebri.goalflow.data.goal.Goal
import lserebri.goalflow.data.goal.GoalRepository
import lserebri.goalflow.data.score.ScoreRepositoryImplementation
import lserebri.goalflow.data.score.ScoreDao
import lserebri.goalflow.data.score.ScoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
	@Provides
	@Singleton
	fun provideDatabase(app: Application): GoalFlowDatabase {
		return Room.databaseBuilder(
			app,
			GoalFlowDatabase::class.java,
			"goalflow_db"
		)
			.fallbackToDestructiveMigration(false)
			.build()
	}

	@Provides
	fun provideGoalDao(db: GoalFlowDatabase): GoalDao = db.goalDao()


	@Provides
	fun provideDistractionDao(db: GoalFlowDatabase): DistractionDao = db.distractionDao()

	@Provides
	@Singleton
	@Named("goalActivity")
	fun provideGoalRepository(dao: GoalDao): ActivityRepository<Goal> {
		return GoalRepository(dao)
	}


	@Provides
	@Singleton
	@Named("distractionActivity")
	fun provideDistractionRepository(dao: DistractionDao): ActivityRepository<Distraction> {
		return DistractionRepository(dao)
	}

	@Provides
	fun provideScoreDao(db: GoalFlowDatabase): ScoreDao = db.scoreDao()

	@Provides
	@Singleton
	fun provideScoreRepository(dao: ScoreDao): ScoreRepository {
		return ScoreRepositoryImplementation(dao)
	}
}
