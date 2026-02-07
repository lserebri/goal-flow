package lserebri.goalflow.data.activity

import kotlinx.coroutines.flow.Flow

abstract class ActivityRepository<T : ActivityItem>(
	private val dao: ActivityDao<T>
) {
	abstract fun getAll(): Flow<List<T>>

	open suspend fun insert(activity: T) = dao.insert(activity)

	open suspend fun update(activity: T) = dao.update(activity)

	open suspend fun delete(activity: T) = dao.delete(activity)
}
