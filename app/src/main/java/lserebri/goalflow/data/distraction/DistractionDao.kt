package lserebri.goalflow.data.distraction

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import lserebri.goalflow.data.activity.ActivityDao
import kotlinx.coroutines.flow.Flow

@Dao
interface DistractionDao : ActivityDao<Distraction> {
	@Query("SELECT * FROM distractions")
	fun getAll(): Flow<List<Distraction>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	override suspend fun insert(activity: Distraction)

	@Update
	override suspend fun update(activity: Distraction)

	@Delete
	override suspend fun delete(activity: Distraction)
}