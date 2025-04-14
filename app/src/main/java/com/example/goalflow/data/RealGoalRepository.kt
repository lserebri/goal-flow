package com.example.goalflow.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RealGoalRepository @Inject constructor(
    private val goalDao: GoalDao
) : GoalRepository {
    override val allGoals = goalDao.getAllGoals()

    override suspend fun insertGoal(goal: Goal) = goalDao.insertGoal(goal)

    override suspend fun deleteGoal(goal: Goal) = goalDao.deleteGoal(goal)
}
