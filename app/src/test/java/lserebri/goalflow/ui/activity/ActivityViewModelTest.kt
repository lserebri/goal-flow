package lserebri.goalflow.ui.activity

import lserebri.goalflow.data.activity.ActivityRepository
import lserebri.goalflow.data.distraction.Distraction
import lserebri.goalflow.data.goal.Goal
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ActivityViewModelTest {
	private val testDispatcher = StandardTestDispatcher()

	private val goalObject = Goal(name = "Test Goal", weight = 5)
	private val distractionObject = Distraction(name = "Test Distraction", weight = 5)
	private lateinit var goalRepository: ActivityRepository<Goal>
	private lateinit var distractionRepository: ActivityRepository<Distraction>
	private lateinit var goalActivityViewModel: ActivityViewModel
	private lateinit var distractionActivityViewModel: ActivityViewModel

	@Before
	fun setup() {
		Dispatchers.setMain(testDispatcher)
		goalRepository = mockk(relaxed = true)
		distractionRepository = mockk(relaxed = true)

		goalActivityViewModel =
			ActivityViewModel(goalRepository, distractionRepository, isGoal = true)
		distractionActivityViewModel =
			ActivityViewModel(goalRepository, distractionRepository, isGoal = false)
	}

	@Test
	fun `addActivity() inserts goal into repository when isGoal flag is true and activityType is Goal`() =
		runTest {
			goalActivityViewModel.addActivity(goalObject)
			advanceUntilIdle()

			coEvery { goalRepository.insert(goalObject) } returns Unit
		}

	@Test
	fun `addActivity() inserts distraction into repository when isGoal flag is false and activityType is Distraction`() =
		runTest {
			distractionActivityViewModel.addActivity(distractionObject)
			advanceUntilIdle()

			coEvery { distractionRepository.insert(distractionObject) } returns Unit
		}


	@Test
	fun `updateActivity() updates current goal when isGoal flag is true and activityType is Goal`() =
		runTest {
			goalActivityViewModel.updateActivity(goalObject)
			advanceUntilIdle()

			coVerify { goalRepository.update(goalObject) }
		}

	@Test
	fun `updateActivity() updates current distraction when isGoal flag is false and activityType is Distraction`() =
		runTest {
			distractionActivityViewModel.updateActivity(distractionObject)
			advanceUntilIdle()

			coVerify { distractionRepository.update(distractionObject) }
		}

	@Test
	fun `deleteActivity() deletes current goal when isGoal flag is true and activityType is Goal`() =
		runTest {
			goalActivityViewModel.deleteActivity(goalObject)
			advanceUntilIdle()

			coVerify { goalRepository.delete(goalObject) }
		}

	@Test
	fun `deleteActivity() deletes current distraction when isGoal flag is false and activityType is Distraction`() =
		runTest {
			distractionActivityViewModel.deleteActivity(distractionObject)
			advanceUntilIdle()

			coVerify { distractionRepository.delete(distractionObject) }
		}

	@Test(expected = IllegalArgumentException::class)
	fun `addActivity() throws IllegalArgumentException when isGoal flag is false and activityType is Goal`() =
		runTest {
			distractionActivityViewModel.addActivity(goalObject)
			advanceUntilIdle()
		}

	@Test(expected = IllegalArgumentException::class)
	fun `updateActivity() throws IllegalArgumentException when isGoal is false and activityType is Goal`() =
		runTest {
			distractionActivityViewModel.updateActivity(goalObject)
			advanceUntilIdle()
		}

	@Test(expected = IllegalArgumentException::class)
	fun `deleteActivity() throws IllegalArgumentException when isGoal is false and activityType is Goal`() =
		runTest {
			distractionActivityViewModel.deleteActivity(goalObject)
			advanceUntilIdle()
		}

	@Test
	fun `loadActivities should return sorted list of activities`() = runTest {
		val goals = listOf(
			Goal(id = 1, name = "Low Priority Goal", weight = 3),
			Goal(id = 2, name = "High Priority Goal", weight = 8),
			Goal(id = 3, name = "Medium Priority Goal", weight = 5)
		)

		coEvery { goalRepository.getAll() } returns flowOf(goals)

		goalActivityViewModel.loadActivities()
		advanceUntilIdle()

		val result = goalActivityViewModel.activities.value
		assertEquals(3, result.size)

		assertEquals("High Priority Goal", result[0].activity.name)
		assertEquals(8, result[0].activity.weight)

		assertEquals("Medium Priority Goal", result[1].activity.name)
		assertEquals(5, result[1].activity.weight)

		assertEquals("Low Priority Goal", result[2].activity.name)
		assertEquals(3, result[2].activity.weight)
	}

	@Test
	fun `loadActivities should return sorted list of distractions`() = runTest {
		val distractions = listOf(
			Distraction(id = 1, name = "Low Priority Distraction", weight = 2),
			Distraction(id = 2, name = "High Priority Distraction", weight = 9),
			Distraction(id = 3, name = "Medium Priority Distraction", weight = 6)
		)

		coEvery { distractionRepository.getAll() } returns flowOf(distractions)

		distractionActivityViewModel.loadActivities()
		advanceUntilIdle()

		val result = distractionActivityViewModel.activities.value
		assertEquals(3, result.size)

		assertEquals("High Priority Distraction", result[0].activity.name)
		assertEquals(9, result[0].activity.weight)

		assertEquals("Medium Priority Distraction", result[1].activity.name)
		assertEquals(6, result[1].activity.weight)

		assertEquals("Low Priority Distraction", result[2].activity.name)
		assertEquals(2, result[2].activity.weight)
	}

	@Test
	fun `loadActivities should return empty list when no activities exist`() = runTest {
		coEvery { goalRepository.getAll() } returns flowOf(emptyList())

		goalActivityViewModel.loadActivities()
		advanceUntilIdle()

		val result = goalActivityViewModel.activities.value
		assertEquals(0, result.size)
	}
}