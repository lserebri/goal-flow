package com.example.goalflow.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goalflow.data.Goal
import com.example.goalflow.data.GoalRepository
import com.example.goalflow.ui.GoalUiState.*
import com.example.goalflow.ui.GoalUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalViewModel @Inject constructor (
    private val goalRepository: GoalRepository
) : ViewModel() {
    val allGoals: StateFlow<GoalUiState> = goalRepository
        .allGoals.map<List<Goal>, GoalUiState>(::Success)
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addGoal(goal: Goal) {
        viewModelScope.launch {
            goalRepository.insertGoal(goal)
        }
    }

    fun deleteGoal(goal: Goal) {
        viewModelScope.launch {
            goalRepository.deleteGoal(goal)
        }
    }
}

sealed interface GoalUiState {
    object Loading : GoalUiState
    data class Error(val throwable: Throwable) : GoalUiState
    data class Success(val data: List<Goal>) : GoalUiState
}
