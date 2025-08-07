
import com.example.goalflow.data.distraction.Distraction
import com.example.goalflow.data.distraction.DistractionDao
import com.example.goalflow.data.distraction.DistractionRepository
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
class DistractionRepositoryTest {

	private val testDispatcher = StandardTestDispatcher()
	private val testScope = TestScope(testDispatcher)

	private lateinit var distractionDao: DistractionDao
	private lateinit var repository: DistractionRepository

	@Before
	fun setUp() {
		distractionDao = mockk(relaxed = true)
		repository = DistractionRepository(distractionDao)
	}

	@Test
	fun `getAll should return flow from dao`() = runTest {
		val distractions = listOf(Distraction(name = "Read", weight = 1))
		every { distractionDao.getAll() } returns flowOf(distractions)

		val result = repository.getAll().first()

		assertEquals(distractions, result)
		verify { distractionDao.getAll() }
	}

	@Test
	fun `insert should delegate to dao`() = testScope.runTest {
		val distraction = Distraction(name = "Read", weight = 5)

		repository.insert(distraction)

		coVerify { distractionDao.insert(distraction) }
	}

	@Test
	fun `update should delegate to dao`() = testScope.runTest {
		val distraction = Distraction(name = "Instagram", weight = 9)

		repository.update(distraction)

		coVerify { distractionDao.update(distraction) }
	}

	@Test
	fun `delete should delegate to dao`() = testScope.runTest {
		val distraction = Distraction(name = "Exercise", weight = 8)

		repository.delete(distraction)

		coVerify { distractionDao.delete(distraction) }
	}
}
