package lserebri.goalflow.data.score

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {
	@Query("SELECT COALESCE((SELECT score FROM scores WHERE id = 0), 0)")
	fun getScore(): Flow<Int>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun updateScore(score: Score)
}