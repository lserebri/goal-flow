package com.example.goalflow.data.consumable

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsumableDao {
    @Query("SELECT * FROM consumable")
    fun getAllConsumables(): Flow<List<Consumable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConsumable(consumable: Consumable)

    @Delete
    suspend fun deleteConsumable(consumable: Consumable)
}