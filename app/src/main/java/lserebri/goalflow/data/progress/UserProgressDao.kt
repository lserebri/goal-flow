package lserebri.goalflow.data.progress

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProgressDao {
	@Query("SELECT * FROM user_progress WHERE id = 0")
	fun getUserProgress(): Flow<UserProgress?>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun updateUserProgress(progress: UserProgress)
}
