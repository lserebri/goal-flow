package com.example.goalflow.data

import kotlinx.coroutines.flow.Flow

interface GoalRepository {
    suspend fun getAllGoals(): List<Goal>

    suspend fun insertGoal(goal: Goal)

    suspend fun deleteGoal(goal: Goal)
}
