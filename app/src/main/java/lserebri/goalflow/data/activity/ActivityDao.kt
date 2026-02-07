package lserebri.goalflow.data.activity

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
interface ActivityDao<T : ActivityItem> {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(activity: T)

	@Update
	suspend fun update(activity: T)

	@Delete
	suspend fun delete(activity: T)
}
