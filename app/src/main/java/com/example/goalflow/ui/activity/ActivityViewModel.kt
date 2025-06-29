package com.example.goalflow.ui.activity

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goalflow.data.activity.ActivityItem
import com.example.goalflow.data.activity.ActivityRepository
import com.example.goalflow.data.consumable.Consumable
import com.example.goalflow.data.goal.Goal
import com.example.goalflow.ui.activity.ActivityUIState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ActivityViewModel @Inject constructor (
	@Named("goalActivity") private val goalRepository: ActivityRepository<Goal>,
	@Named("consumableActivity") private val consumableRepository: ActivityRepository<Consumable>,
	savedStateHandle: SavedStateHandle
) : ViewModel() {
	private val isFirstTab: Boolean = savedStateHandle["isFirstTab"] ?: true

	private val activeRepo = if (isFirstTab) goalRepository else consumableRepository


	val getAll: StateFlow<ActivityUIState> = activeRepo
		.getAll.map<List<ActivityItem>, ActivityUIState>(::Success)
		.catch { emit(Error(it)) }
		.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

	fun add(activityItem: ActivityItem) {
		viewModelScope.launch {
			if (activityItem is Goal && isFirstTab) {
				goalRepository.insert(activityItem)
			} else if (activityItem is Consumable && !isFirstTab) {
				consumableRepository.insert(activityItem)
			} else {
				throw IllegalArgumentException("Invalid type for the current repository")
			}
		}

	}

	fun update(activityItem: ActivityItem) {
		viewModelScope.launch {
			if (activityItem is Goal && isFirstTab) {
				goalRepository.update(activityItem)
			} else if (activityItem is Consumable && !isFirstTab) {
				consumableRepository.update(activityItem)
			} else {
				throw IllegalArgumentException("Invalid type for the current repository")
			}
		}
	}

	fun delete(activityItem: ActivityItem) {
		viewModelScope.launch {
			if (activityItem is Goal && isFirstTab) {
				goalRepository.delete(activityItem)
			} else if (activityItem is Consumable && !isFirstTab) {
				consumableRepository.delete(activityItem)
			} else {
				throw IllegalArgumentException("Invalid type for the current repository")
			}
		}

	}
}
