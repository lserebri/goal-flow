package lserebri.goalflow.data.score

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
class GoalRepositoryTest {

	private val testDispatcher = StandardTestDispatcher()
	private val testScope = TestScope(testDispatcher)

	private lateinit var scoreDao: ScoreDao
	private lateinit var repository: ScoreRepository

	@Before
	fun setUp() {
		scoreDao = mockk(relaxed = true)
		repository = ScoreRepositoryImplementation(scoreDao)
	}

	@Test
	fun `getScore should return flow from dao`() = runTest {
		val score = Score(score = 10)
		every { scoreDao.getScore() } returns flowOf(score.score)

		val result = repository.getScore().first()

		assertEquals(score.score, result)

		verify { scoreDao.getScore() }
	}

	@Test
	fun `updateScore should call dao updateScore`() = testScope.runTest {
		val newScoreValue = 25
		val expectedScoreObject = Score(score = newScoreValue)

		repository.updateScore(Score(score = newScoreValue))

		coVerify { scoreDao.updateScore(expectedScoreObject) }
	}
}