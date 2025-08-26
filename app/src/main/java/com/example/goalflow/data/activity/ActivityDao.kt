package com.example.goalflow.data.activity

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

interface ActivityDao<T : ActivityItem> {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(activity: T)

	@Update
	suspend fun update(activity: T)

	@Delete
	suspend fun delete(activity: T)
}
