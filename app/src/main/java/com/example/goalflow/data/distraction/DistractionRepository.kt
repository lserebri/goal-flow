package com.example.goalflow.data.distraction

import com.example.goalflow.data.activity.ActivityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DistractionRepository @Inject constructor(
	private val distractionDao: DistractionDao
) : ActivityRepository<Distraction>(distractionDao) {
	override fun getAll(): Flow<List<Distraction>> = distractionDao.getAll()

	override suspend fun insert(activity: Distraction) = distractionDao.insert(activity)

	override suspend fun delete(activity: Distraction) = distractionDao.delete(activity)
}
