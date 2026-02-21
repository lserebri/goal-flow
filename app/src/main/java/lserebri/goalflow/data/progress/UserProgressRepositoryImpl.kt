package lserebri.goalflow.data.progress

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserProgressRepositoryImpl @Inject constructor(
	private val userProgressDao: UserProgressDao
) : UserProgressRepository {
	override fun getUserProgress(): Flow<UserProgress?> = userProgressDao.getUserProgress()

	override suspend fun updateUserProgress(progress: UserProgress) =
		userProgressDao.updateUserProgress(progress.copy(id = 0))
}
