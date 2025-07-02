package com.example.goalflow.data.distraction

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.goalflow.data.activity.ActivityDao
import kotlinx.coroutines.flow.Flow

@Dao
interface DistractionDao : ActivityDao<Distraction> {
    @Query("SELECT * FROM distraction")
    override fun getAll(): Flow<List<Distraction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(distraction: Distraction)

    @Delete
    override suspend fun delete(distraction: Distraction)
}