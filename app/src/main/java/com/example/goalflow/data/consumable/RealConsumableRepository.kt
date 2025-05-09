package com.example.goalflow.data.consumable

import javax.inject.Inject

class RealConsumableRepository @Inject constructor(
    private val consumableDao: ConsumableDao
) : ConsumableRepository {
    override val allConsumables = consumableDao.getAllConsumables()

    override suspend fun insertConsumable(consumable: Consumable) = consumableDao.insertConsumable(consumable)

    override suspend fun deleteConsumable(consumable: Consumable) = consumableDao.deleteConsumable(consumable)
}