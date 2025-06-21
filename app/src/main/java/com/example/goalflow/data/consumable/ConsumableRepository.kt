package com.example.goalflow.data.consumable

import com.example.goalflow.data.activity.ActivityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConsumableRepository @Inject constructor(
    private val consumableDao: ConsumableDao
) : ActivityRepository<Consumable>(consumableDao) {
    override val getAll: Flow<List<Consumable>> = consumableDao.getAll()

    override suspend fun insert(activity: Consumable) = consumableDao.insert(activity)

    override suspend fun delete(activity: Consumable) = consumableDao.delete(activity)
}
