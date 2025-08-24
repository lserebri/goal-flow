package com.example.goalflow.di

import android.app.Application
import androidx.room.Room
import com.example.goalflow.data.goal.GoalDao
import com.example.goalflow.data.GoalFlowDatabase
import com.example.goalflow.data.activity.ActivityRepository
import com.example.goalflow.data.distraction.Distraction
import com.example.goalflow.data.distraction.DistractionDao
import com.example.goalflow.data.distraction.DistractionRepository
import com.example.goalflow.data.goal.Goal
import com.example.goalflow.data.goal.GoalRepository
import com.example.goalflow.data.score.ScoreRepositoryImplementation
import com.example.goalflow.data.score.ScoreDao
import com.example.goalflow.data.score.ScoreRepository
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
			.fallbackToDestructiveMigration()
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
