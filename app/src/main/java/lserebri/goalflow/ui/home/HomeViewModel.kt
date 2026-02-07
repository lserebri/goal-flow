package lserebri.goalflow.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import lserebri.goalflow.data.score.Score
import lserebri.goalflow.data.score.ScoreRepository
import lserebri.goalflow.ui.home.ScoreUiState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
	private val scoreRepository: ScoreRepository,
) : ViewModel() {
	private val _currentActivityTab = mutableStateOf(ActivityTabType.GOALS)
	val currentActivityTab: State<ActivityTabType> = _currentActivityTab

	fun setCurrentActivityTab(newType: ActivityTabType) {
		_currentActivityTab.value = newType
	}

	val score: StateFlow<ScoreUiState> = flow {
		emitAll(scoreRepository.getScore())
	}.map<Int, ScoreUiState>(::Success)
		.catch { error ->
			emit(Error(error))
		}
		.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)


	fun updateScore(minutes: Int, weight: Int, isGoal: Boolean) {
		viewModelScope.launch {
			try {
				val currentScore = scoreRepository.getScore().first()
				val deltaPoints = ((minutes.toFloat() / 60f) * weight).toInt()
				val newScore = if (isGoal) {
					currentScore + deltaPoints
				} else {
					(currentScore - deltaPoints).coerceAtLeast(0)
				}
				scoreRepository.updateScore(Score(score = newScore))
			} catch (e: Exception) {
				// Log error instead of crashing the UI
				// TODO: Implement proper error reporting mechanism
			}
		}
	}
}


sealed interface ScoreUiState {
	object Loading : ScoreUiState
	data class Error(val throwable: Throwable) : ScoreUiState
	data class Success(val data: Int?) : ScoreUiState
}