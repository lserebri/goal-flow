package com.example.goalflow.data

import kotlinx.coroutines.flow.Flow

class OfflineGoalsRepository(private val goalDao: GoalDao) : GoalRepository {
    override fun getAllGoals(): Flow<List<Goal>> = goalDao.getAllGoals()

    override suspend fun insertGoal(goal: Goal) = goalDao.insertGoal(goal)

    override suspend fun deleteGoal(goal: Goal) = goalDao.deleteGoal(goal)
}