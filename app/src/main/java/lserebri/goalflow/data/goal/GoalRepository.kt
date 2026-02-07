package lserebri.goalflow.data.goal

import lserebri.goalflow.data.activity.ActivityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GoalRepository @Inject constructor(
	private val goalDao: GoalDao
) : ActivityRepository<Goal>(goalDao) {
	override fun getAll(): Flow<List<Goal>> = goalDao.getAll()

	override suspend fun insert(activity: Goal) = goalDao.insert(activity)

	override suspend fun delete(activity: Goal) = goalDao.delete(activity)
}

