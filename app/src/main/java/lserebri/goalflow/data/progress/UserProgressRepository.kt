package lserebri.goalflow.data.progress

import kotlinx.coroutines.flow.Flow

interface UserProgressRepository {
	fun getUserProgress(): Flow<UserProgress?>

	suspend fun updateUserProgress(progress: UserProgress)
}
