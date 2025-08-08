package com.example.goalflow.ui.activity

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
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel(assistedFactory = ActivityViewModel.ActivityViewModelFactory::class)
class ActivityViewModel @AssistedInject constructor (
	@Named("goalActivity") private val goalRepository: ActivityRepository<Goal>,
	@Named("distractionActivity") private val distractionRepository: ActivityRepository<Distraction>,
	@Assisted val isGoal: Boolean
) : ViewModel() {

	private fun getActiveRepo() = if (isGoal) goalRepository else distractionRepository

	private val _showDeleteDialog = MutableStateFlow(false)
	val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog

	private val _showAddActivityDialog = MutableStateFlow(false)
	val showAddActivityDialog: StateFlow<Boolean> = _showAddActivityDialog

	private val _showEditDialog = MutableStateFlow(false)
	val showEditDialog: StateFlow<Boolean> = _showEditDialog


	private val _showTimePickerDialog = MutableStateFlow(false)
	val showTimePickerDialog: StateFlow<Boolean> = _showTimePickerDialog

	private val _selectedActivity = MutableStateFlow<ActivityItem?>(null)
	val selectedActivity: StateFlow<ActivityItem?> = _selectedActivity

	private val _activities = MutableStateFlow<List<ActivityUI>>(emptyList())
	val activities: StateFlow<List<ActivityUI>> = _activities


	fun loadActivities() {
		viewModelScope.launch {
			getActiveRepo().getAll()
				.map { list ->
					list.map { ActivityUI(it) }
						.sortedByDescending { it.activity.weight }
				}
				.catch { error ->
					Timber.e(error, "Failed to load activities for type: ${if (isGoal) "Goal" else "Distraction"}")
					_activities.value = emptyList()
				}
				.collect { sortedList ->
					_activities.value = sortedList
				}
		}
	}

	fun onAddActivityClick() { _showAddActivityDialog.value = true }
	fun onAddActivityDismiss() { _showAddActivityDialog.value = false }
	fun onSelectActivity(activity: ActivityItem?) { _selectedActivity.value = activity }
	fun onDeleteDialogShow() { _showDeleteDialog.value = true }
	fun onDeleteDialogDismiss() { _showDeleteDialog.value = false }
	fun onEditDialogShow() { _showEditDialog.value = true }
	fun onEditDialogDismiss() { _showEditDialog.value = false }
	fun onTimePickerShow() { _showTimePickerDialog.value = true }
	fun onTimePickerDismiss() { _showTimePickerDialog.value = false }


	@AssistedFactory
	interface ActivityViewModelFactory {
		fun create(isGoal: Boolean): ActivityViewModel
	}

	fun add(activityItem: ActivityItem) {
		viewModelScope.launch {
			try {
				if (activityItem is Goal && isGoal) {
					goalRepository.insert(activityItem)
					Timber.d("Successfully added goal: ${activityItem.name}")
				} else if (activityItem is Distraction && !isGoal) {
					distractionRepository.insert(activityItem)
					Timber.d("Successfully added distraction: ${activityItem.name}")
				} else {
					val error = IllegalArgumentException("Invalid type for the current repository")
					Timber.e(error, "Failed to add activity: ${activityItem.javaClass.simpleName} for isGoal: $isGoal")
					throw error
				}
			} catch (e: Exception) {
				Timber.e(e, "Failed to add activity: ${activityItem.name}")
				throw e
			}
		}
	}

	fun update(activityItem: ActivityItem) {
		viewModelScope.launch {
			try {
				if (activityItem is Goal && isGoal) {
					goalRepository.update(activityItem)
					Timber.d("Successfully updated goal: ${activityItem.name}")
				} else if (activityItem is Distraction && !isGoal) {
					distractionRepository.update(activityItem)
					Timber.d("Successfully updated distraction: ${activityItem.name}")
				} else {
					val error = IllegalArgumentException("Invalid type for the current repository")
					Timber.e(error, "Failed to update activity: ${activityItem.javaClass.simpleName} for isGoal: $isGoal")
					throw error
				}
			} catch (e: Exception) {
				Timber.e(e, "Failed to update activity: ${activityItem.name}")
				throw e
			}
		}
	}

	fun delete(activityItem: ActivityItem) {
		viewModelScope.launch {
			try {
				if (activityItem is Goal && isGoal) {
					goalRepository.delete(activityItem)
					Timber.d("Successfully deleted goal: ${activityItem.name}")
				} else if (activityItem is Distraction && !isGoal) {
					distractionRepository.delete(activityItem)
					Timber.d("Successfully deleted distraction: ${activityItem.name}")
				} else {
					val error = IllegalArgumentException("Invalid type for the current repository")
					Timber.e(error, "Failed to delete activity: ${activityItem.javaClass.simpleName} for isGoal: $isGoal")
					throw error
				}
			} catch (e: Exception) {
				Timber.e(e, "Failed to delete activity: ${activityItem.name}")
				throw e
			}
		}
	}
}
