package com.example.goalflow.data.consumable

import kotlinx.coroutines.flow.Flow

interface ConsumableRepository {
    val allConsumables: Flow<List<Consumable>>

    suspend fun insertConsumable(consumable: Consumable)

    suspend fun deleteConsumable(consumable: Consumable)
}