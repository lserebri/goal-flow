package com.example.goalflow.data.goal

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.goalflow.data.activity.ActivityDao
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao: ActivityDao<Goal> {
    @Query("SELECT * FROM goals")
    override fun getAll(): Flow<List<Goal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(goal: Goal)

    @Delete
    override suspend fun delete(goal: Goal)
}