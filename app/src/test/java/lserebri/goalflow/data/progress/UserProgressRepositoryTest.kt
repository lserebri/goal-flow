package lserebri.goalflow.data.progress

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
class UserProgressRepositoryTest {

	private val testDispatcher = StandardTestDispatcher()
	private val testScope = TestScope(testDispatcher)

	private lateinit var userProgressDao: UserProgressDao
	private lateinit var repository: UserProgressRepository

	@Before
	fun setUp() {
		userProgressDao = mockk(relaxed = true)
		repository = UserProgressRepositoryImpl(userProgressDao)
	}

	@Test
	fun `getUserProgress should return flow from dao`() = runTest {
		val progress = UserProgress(level = 5, xp = 100)
		every { userProgressDao.getUserProgress() } returns flowOf(progress)

		val result = repository.getUserProgress().first()

		assertEquals(progress, result)

		verify { userProgressDao.getUserProgress() }
	}

	@Test
	fun `updateUserProgress should call dao updateUserProgress`() = testScope.runTest {
		val progress = UserProgress(level = 3, xp = 50)

		repository.updateUserProgress(progress)

		coVerify { userProgressDao.updateUserProgress(progress.copy(id = 0)) }
	}
}
