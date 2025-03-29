import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.goalflow.data.Goal
import com.example.goalflow.data.GoalDao

@Database(entities = [Goal::class], version = 1, exportSchema = false)
abstract class GoalFlowDatabase : RoomDatabase() {
    abstract fun goalDao(): GoalDao

    companion object {
        @Volatile
        private var INSTANCE: GoalFlowDatabase? = null

        fun getDatabase(context: Context): GoalFlowDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GoalFlowDatabase::class.java,
                    "goal_flow_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
