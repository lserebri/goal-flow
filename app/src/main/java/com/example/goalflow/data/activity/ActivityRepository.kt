package com.example.goalflow.data.activity

import kotlinx.coroutines.flow.Flow

open class ActivityRepository<T : ActivityItem>(
	private val dao: ActivityDao<T>
) {
	open fun getAll(): Flow<List<T>> = dao.getAll()

	open suspend fun insert(activity: T) = dao.insert(activity)

	open suspend fun update(activity: T) = dao.update(activity)

	open suspend fun delete(activity: T) = dao.delete(activity)
}
