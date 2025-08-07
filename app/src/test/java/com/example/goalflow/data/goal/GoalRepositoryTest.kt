import com.example.goalflow.data.goal.Goal
import com.example.goalflow.data.goal.GoalDao
import com.example.goalflow.data.goal.GoalRepository
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GoalRepositoryTest {

	private val testDispatcher = StandardTestDispatcher()
	private val testScope = TestScope(testDispatcher)

	private lateinit var goalDao: GoalDao
	private lateinit var repository: GoalRepository

	@Before
	fun setUp() {
		goalDao = mockk(relaxed = true)
		repository = GoalRepository(goalDao)
	}

	@Test
	fun `getAll should return flow from dao`() = runTest {
		val goals = listOf(Goal(name = "Read", weight = 1))
		every { goalDao.getAll() } returns flowOf(goals)

		val result = repository.getAll().first()

		assertEquals(goals, result)
		verify { goalDao.getAll() }
	}

	@Test
	fun `insert should delegate to dao`() = testScope.runTest {
		val goal = Goal(name = "Read", weight = 5)

		repository.insert(goal)

		coVerify { goalDao.insert(goal) }
	}

	@Test
	fun `update should delegate to dao`() = testScope.runTest {
		val goal = Goal(name = "Write tests", weight = 10)

		repository.update(goal)

		coVerify { goalDao.update(goal) }
	}

	@Test
	fun `delete should delegate to dao`() = testScope.runTest {
		val goal = Goal(name = "Exercise", weight = 8)

		repository.delete(goal)

		coVerify { goalDao.delete(goal) }
	}
}
