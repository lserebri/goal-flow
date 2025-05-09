package com.example.goalflow.data.goal

import kotlinx.coroutines.flow.Flow

interface GoalRepository {
    val allGoals: Flow<List<Goal>>

    suspend fun insertGoal(goal: Goal)

    suspend fun deleteGoal(goal: Goal)
}
