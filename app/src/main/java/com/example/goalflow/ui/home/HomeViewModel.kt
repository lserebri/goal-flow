package com.example.goalflow.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goalflow.data.score.ScoreRepository
import com.example.goalflow.ui.home.ScoreUiState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val scoreRepository: ScoreRepository,
//    private val goalViewModel: GoalViewModel,
//    private val consumableViewModel: ConsumableViewModel
): ViewModel() {
    val score: StateFlow<ScoreUiState> = scoreRepository
        .getScore.map<Int, ScoreUiState> (::Success)
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)
}



sealed interface ScoreUiState {
    object Loading : ScoreUiState
    data class Error(val throwable: Throwable) : ScoreUiState
    data class Success(val data: Int?) : ScoreUiState
}