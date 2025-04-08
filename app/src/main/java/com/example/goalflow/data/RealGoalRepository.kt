package com.example.goalflow.data

class RealGoalRepository(private val goalDao: GoalDao) : GoalRepository {
        override suspend fun getAllGoals(): List<Goal> = goalDao.getAllGoals()
//    override suspend fun getAllGoals(): List<Goal> = listOf(
//        Goal(123, "fg", 1),
//        Goal(12, "jlkj", 1)
//    )

    override suspend fun insertGoal(goal: Goal) = goalDao.insertGoal(goal)

    override suspend fun deleteGoal(goal: Goal) = goalDao.deleteGoal(goal)
}