package com.example.goalflow.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goalflow.data.Goal
import com.example.goalflow.data.GoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalViewModel @Inject constructor (
    private val goalRepository: GoalRepository
) : ViewModel() {
    var  allGoals = emptyList<Goal>()

    init {
        viewModelScope.launch {
            allGoals = goalRepository.getAllGoals()
        }
    }

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
