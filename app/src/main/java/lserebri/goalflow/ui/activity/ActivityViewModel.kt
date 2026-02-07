package lserebri.goalflow.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import lserebri.goalflow.data.activity.ActivityItem
import lserebri.goalflow.data.activity.ActivityRepository
import lserebri.goalflow.data.distraction.Distraction
import lserebri.goalflow.data.goal.Goal
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Named

@HiltViewModel(assistedFactory = ActivityViewModel.ActivityViewModelFactory::class)
class ActivityViewModel @AssistedInject constructor(
	@param:Named("goalActivity") private val goalRepository: ActivityRepository<Goal>,
	@param:Named("distractionActivity") private val distractionRepository: ActivityRepository<Distraction>,
	@Assisted val isGoal: Boolean
) : ViewModel() {

	@AssistedFactory
	interface ActivityViewModelFactory {
		fun create(isGoal: Boolean): ActivityViewModel
	}

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
					_activities.value = emptyList()
				}
				.collect { sortedList ->
					_activities.value = sortedList
				}
		}
	}

	fun onAddActivityClick() {
		_showAddActivityDialog.value = true
	}

	fun onAddActivityDismiss() {
		_showAddActivityDialog.value = false
	}

	fun onSelectActivity(activity: ActivityItem?) {
		_selectedActivity.value = activity
	}

	fun onDeleteDialogShow() {
		_showDeleteDialog.value = true
	}

	fun onDeleteDialogDismiss() {
		_showDeleteDialog.value = false
	}

	fun onEditDialogShow() {
		_showEditDialog.value = true
	}

	fun onEditDialogDismiss() {
		_showEditDialog.value = false
	}

	fun onTimePickerShow() {
		_showTimePickerDialog.value = true
	}

	fun onTimePickerDismiss() {
		_showTimePickerDialog.value = false
	}

	fun addActivity(activityItem: ActivityItem) = viewModelScope.launch {
		when {
			activityItem is Goal && isGoal -> goalRepository.insert(activityItem)
			activityItem is Distraction && !isGoal -> distractionRepository.insert(activityItem)
			else -> throw IllegalArgumentException("Invalid type for the current repository")
		}
	}

	fun updateActivity(activityItem: ActivityItem) = viewModelScope.launch {
		when {
			activityItem is Goal && isGoal -> goalRepository.update(activityItem)
			activityItem is Distraction && !isGoal -> distractionRepository.update(activityItem)
			else -> throw IllegalArgumentException("Invalid type for the current repository")
		}
	}

	fun deleteActivity(activityItem: ActivityItem) = viewModelScope.launch {
		when {
			activityItem is Goal && isGoal -> goalRepository.delete(activityItem)
			activityItem is Distraction && !isGoal -> distractionRepository.delete(activityItem)
			else -> throw IllegalArgumentException("Invalid type for the current repository")
		}
	}
}
