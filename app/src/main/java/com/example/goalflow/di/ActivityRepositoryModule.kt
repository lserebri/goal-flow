package com.example.goalflow.di

import com.example.goalflow.data.activity.ActivityRepository
import com.example.goalflow.data.consumable.Consumable
import com.example.goalflow.data.consumable.ConsumableDao
import com.example.goalflow.data.consumable.ConsumableRepository
import com.example.goalflow.data.goal.Goal
import com.example.goalflow.data.goal.GoalDao
import com.example.goalflow.data.goal.GoalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ActivityRepositoryModule {


}