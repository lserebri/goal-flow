package com.example.goalflow.ui.activity

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goalflow.data.activity.ActivityItem
import com.example.goalflow.data.activity.ActivityRepository
import com.example.goalflow.data.distraction.Distraction
import com.example.goalflow.data.goal.Goal
import com.example.goalflow.ui.activity.ActivityUIState.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel(assistedFactory = ActivityViewModel.ActivityViewModelFactory::class)
class ActivityViewModel @AssistedInject constructor (
	@Named("goalActivity") private val goalRepository: ActivityRepository<Goal>,
	@Named("distractionActivity") private val distractionRepository: ActivityRepository<Distraction>,
	@Assisted private val isGoal: Boolean) : ViewModel() {

	@AssistedFactory
	interface ActivityViewModelFactory {
		fun create(isGoal: Boolean): ActivityViewModel
	}

	private fun getActiveRepo() = if (isGoal) goalRepository else distractionRepository


	val getAll: StateFlow<ActivityUIState> = getActiveRepo()
		.getAll.map<List<ActivityItem>, ActivityUIState>(::Success)
		.catch { emit(Error(it)) }
		.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

	fun add(activityItem: ActivityItem) {
		viewModelScope.launch {
			if (activityItem is Goal && isGoal) {
				goalRepository.insert(activityItem)
			} else if (activityItem is Distraction && !isGoal) {
				distractionRepository.insert(activityItem)
			} else {
				throw IllegalArgumentException("Invalid type for the current repository")
			}
		}

	}

	fun update(activityItem: ActivityItem) {
		viewModelScope.launch {
			if (activityItem is Goal && isGoal) {
				goalRepository.update(activityItem)
			} else if (activityItem is Distraction && !isGoal) {
				distractionRepository.update(activityItem)
			} else {
				throw IllegalArgumentException("Invalid type for the current repository")
			}
		}
	}

	fun delete(activityItem: ActivityItem) {
		viewModelScope.launch {
			if (activityItem is Goal && isGoal) {
				goalRepository.delete(activityItem)
			} else if (activityItem is Distraction && !isGoal) {
				distractionRepository.delete(activityItem)
			} else {
				throw IllegalArgumentException("Invalid type for the current repository")
			}
		}

	}
}
