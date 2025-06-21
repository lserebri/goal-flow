package com.example.goalflow.data.consumable

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.goalflow.data.activity.ActivityDao
import com.example.goalflow.data.activity.ActivityItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsumableDao : ActivityDao<Consumable> {
    @Query("SELECT * FROM consumable")
    override fun getAll(): Flow<List<Consumable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(consumable: Consumable)

    @Delete
    override suspend fun delete(consumable: Consumable)
}