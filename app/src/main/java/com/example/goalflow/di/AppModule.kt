package com.example.goalflow.di

import android.app.Application
import androidx.room.Room
import com.example.goalflow.data.goal.GoalDao
import com.example.goalflow.data.GoalFlowDatabase
import com.example.goalflow.data.consumable.ConsumableDao
import com.example.goalflow.data.consumable.ConsumableRepository
import com.example.goalflow.data.consumable.RealConsumableRepository
import com.example.goalflow.data.goal.GoalRepository
import com.example.goalflow.data.goal.RealGoalRepository
import com.example.goalflow.data.score.RealScoreRepository
import com.example.goalflow.data.score.ScoreDao
import com.example.goalflow.data.score.ScoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
    @Singleton
    fun provideGoalRepository(dao: GoalDao): GoalRepository {
        return RealGoalRepository(dao)
    }

    @Provides
    fun provideConsumableDao(db: GoalFlowDatabase): ConsumableDao = db.consumableDao()

    @Provides
    @Singleton
    fun provideConsumableRepository(dao: ConsumableDao): ConsumableRepository {
        return RealConsumableRepository(dao)
    }

    @Provides
    fun provideScoreDao(db: GoalFlowDatabase): ScoreDao = db.scoreDao()

    @Provides
    @Singleton
    fun provideScoreRepository(dao: ScoreDao): ScoreRepository {
        return RealScoreRepository(dao)
    }
}
