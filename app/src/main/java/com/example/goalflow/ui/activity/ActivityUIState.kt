package com.example.goalflow.ui.activity

import com.example.goalflow.data.activity.ActivityItem

sealed interface ActivityUIState {
	object Loading : ActivityUIState
	data class Error(val throwable: Throwable) : ActivityUIState
	data class Success(val data: List<ActivityItem>) : ActivityUIState
}