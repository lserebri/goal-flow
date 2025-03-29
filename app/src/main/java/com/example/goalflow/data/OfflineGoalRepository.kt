package com.example.goalflow.data

import kotlinx.coroutines.flow.Flow

class OfflineGoalsRepository(private val goalDao: GoalDao) : GoalRepository {
    override fun getAllGoals(): Flow<List<Goal>> = goalDao.getAllGoals()

    override suspend fun insertItem(item: Goal) = goalDao.insert(item)

    override suspend fun deleteItem(item: Goal) = itemDao.delete(item)

    override suspend fun updateItem(item: Item) = itemDao.update(item)
}