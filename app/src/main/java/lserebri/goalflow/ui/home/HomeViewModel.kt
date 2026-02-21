package lserebri.goalflow.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import lserebri.goalflow.data.level.LevelCalculator
import lserebri.goalflow.data.level.LevelInfo
import lserebri.goalflow.data.progress.UserProgress
import lserebri.goalflow.data.progress.UserProgressRepository
import lserebri.goalflow.ui.home.ProgressUiState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
	private val userProgressRepository: UserProgressRepository,
) : ViewModel() {
	private val _currentActivityTab = mutableStateOf(ActivityTabType.GOALS)
	val currentActivityTab: State<ActivityTabType> = _currentActivityTab

	fun setCurrentActivityTab(newType: ActivityTabType) {
		_currentActivityTab.value = newType
	}

	val progress: StateFlow<ProgressUiState> = userProgressRepository.getUserProgress()
		.map<UserProgress?, ProgressUiState> { userProgress ->
			val progress = userProgress ?: UserProgress()
			val levelInfo = LevelCalculator.getLevelInfo(progress.level, progress.xp)
			Success(levelInfo)
		}
		.catch { error ->
			emit(Error(error))
		}
		.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)


	fun updateProgress(minutes: Int, weight: Int, isGoal: Boolean) {
		viewModelScope.launch {
			try {
				val currentProgress = userProgressRepository.getUserProgress().first() ?: UserProgress()
				val xpDelta = LevelCalculator.calculateXpGain(minutes, weight)

				val (newLevel, newXp) = LevelCalculator.applyXpChange(
					currentLevel = currentProgress.level,
					currentXp = currentProgress.xp,
					xpDelta = xpDelta,
					isGain = isGoal
				)

				userProgressRepository.updateUserProgress(
					UserProgress(level = newLevel, xp = newXp)
				)
			} catch (e: Exception) {
				// Log error instead of crashing the UI
			}
		}
	}
}


sealed interface ProgressUiState {
	object Loading : ProgressUiState
	data class Error(val throwable: Throwable) : ProgressUiState
	data class Success(val levelInfo: LevelInfo) : ProgressUiState
}