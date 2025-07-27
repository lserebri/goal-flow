package com.example.goalflow.ui.activity

import TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.example.goalflow.data.activity.ActivityItem
import com.example.goalflow.data.distraction.Distraction
import com.example.goalflow.data.goal.Goal
import com.example.goalflow.ui.action.ActionIcon
import com.example.goalflow.ui.components.ActivityDialog
import com.example.goalflow.ui.components.DeleteConfirmationDialog
import com.example.goalflow.ui.home.HomeViewModel

@Composable
private fun DeleteActivityDialog(
	show: Boolean,
	selectedActivity: ActivityItem?,
	onDismiss: () -> Unit,
	onConfirm: (ActivityItem) -> Unit
) {
	if (show && selectedActivity != null) {
		DeleteConfirmationDialog(
			dialogTitle = "Delete Activity",
			dialogSubTitle = "Are you sure you want to delete \"${selectedActivity.name}\"?",
			onDismissRequest = onDismiss,
			onConfirmation = { onConfirm(selectedActivity) })
	}
}

@Composable
private fun EditActivityDialog(
	show: Boolean,
	selectedActivity: ActivityItem?,
	onDismiss: () -> Unit,
	onSave: (String, Int) -> Unit
) {
	if (show && selectedActivity != null) {
		ActivityDialog(
			initialName = selectedActivity.name,
			initialWeight = selectedActivity.weight,
			onDismiss = onDismiss,
			onSave = onSave
		)
	}
}

@Composable
private fun AddActivityDialog(
	show: Boolean, isGoal: Boolean, onDismiss: () -> Unit, onSave: (String, Int) -> Unit
) {
	if (show) {
		ActivityDialog(
			onDismiss = onDismiss, onSave = { name, weight ->
				onSave(name, weight)
			})
	}
}

@Composable
private fun ActivityTimePickerDialog(
	show: Boolean, onDismiss: () -> Unit, onConfirm: (Int, Int) -> Unit
) {
	if (show) {
		TimePickerDialog(
			initialHour = -1, initialMinute = -1, onDismiss = onDismiss, onConfirm = onConfirm
		)
	}
}

@Composable
fun ActivityListScreen(
	isGoal: Boolean,
	activityViewModel: ActivityViewModel,
	homeViewModel: HomeViewModel = hiltViewModel()
) {
	// Call loadActivities() once when the screen is first composed
	LaunchedEffect(Unit) {
		activityViewModel.loadActivities()
	}

	val activityUIList by activityViewModel.activities.collectAsState()

	ActivityListComposable(
		activityUIList,
		onAddActivityButtonClick = {
			activityViewModel.onAddActivityClick()
		},
		onEditClick = {
			activityViewModel.onSelectActivity(it)
			activityViewModel.onEditDialogShow()
		},
		onDeleteClick = {
			activityViewModel.onSelectActivity(it)
			activityViewModel.onDeleteDialogShow()
		},
		onActivityClick = {
			activityViewModel.onSelectActivity(it)
			activityViewModel.onTimePickerShow()
		},
	)

	val showDeleteDialog by activityViewModel.showDeleteDialog.collectAsState()
	val showAddActivityDialog by activityViewModel.showAddActivityDialog.collectAsState()
	val selectedActivity by activityViewModel.selectedActivity.collectAsState()
	val showEditDialog by activityViewModel.showEditDialog.collectAsState()
	val showTimePickerDialog by activityViewModel.showTimePickerDialog.collectAsState()

	DeleteActivityDialog(
		show = showDeleteDialog,
		selectedActivity = selectedActivity,
		onDismiss = { activityViewModel.onDeleteDialogDismiss() },
		onConfirm = {
			activityViewModel.delete(it)
			activityViewModel.onDeleteDialogDismiss()
		})

	EditActivityDialog(
		show = showEditDialog,
		selectedActivity = selectedActivity,
		onDismiss = { activityViewModel.onEditDialogDismiss() },
		onSave = { name, weight ->
			val updated = when (selectedActivity) {
				is Goal -> (selectedActivity as Goal).copy(name = name, weight = weight)
				is Distraction -> (selectedActivity as Distraction).copy(
					name = name, weight = weight
				)

				else -> return@EditActivityDialog
			}
			activityViewModel.update(updated)
			activityViewModel.onEditDialogDismiss()
		})

	AddActivityDialog(
		show = showAddActivityDialog,
		isGoal = isGoal,
		onDismiss = { activityViewModel.onAddActivityDismiss() },
		onSave = { name, weight ->
			activityViewModel.add(
				if (isGoal) Goal(name = name, weight = weight)
				else Distraction(name = name, weight = weight)
			)
			activityViewModel.onAddActivityDismiss()
		})

	ActivityTimePickerDialog(
		show = showTimePickerDialog,
		onDismiss = { activityViewModel.onTimePickerDismiss() },
		onConfirm = { hour, minute ->
			selectedActivity?.let {
				homeViewModel.updateScore(
					(hour * 60 + minute), it.weight, isGoal = isGoal
				)
			}
			activityViewModel.onTimePickerDismiss()
		})
}

@Composable
fun ActivityListComposable(
	activities: List<ActivityUI>,
	onEditClick: (ActivityItem) -> Unit,
	onDeleteClick: (ActivityItem) -> Unit,
	onActivityClick: (ActivityItem) -> Unit,
	onAddActivityButtonClick: () -> Unit,
) {
	var cardHeight by remember { mutableStateOf(100.dp) }

	Column(
	) {
		BoxWithConstraints(
			modifier = Modifier
				.fillMaxWidth()
				.weight(6f)
		) {
			val boxScope = this
			cardHeight = (boxScope.maxHeight / 4) - 2.dp
			LazyColumn(
				modifier = Modifier.padding(top = 2.dp),
				verticalArrangement = Arrangement.SpaceEvenly,
			) {
				items(activities) { activityItem ->
					ActivityComposable(
						activity = activityItem.activity,
						onEditClick = onEditClick,

						onDeleteClick = onDeleteClick,
						onActivityClick = onActivityClick,
						modifier = Modifier.height(cardHeight)
					)
					Spacer(modifier = Modifier.size(2.dp))
				}
			}
		}
		Spacer(modifier = Modifier.size(2.dp))
		Card(
			modifier = Modifier
				.height(cardHeight)
				.align(Alignment.CenterHorizontally)
		) {
			FloatingActionButton(
				shape = RectangleShape,
				onClick = onAddActivityButtonClick,
				modifier = Modifier
					.align(Alignment.CenterHorizontally)
					.weight(1f)
					.fillMaxWidth()
			) {
				Icon(Icons.Filled.Add, "Floating action button.")
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityComposable(
	activity: ActivityItem,
	onEditClick: (ActivityItem) -> Unit,
	onDeleteClick: (ActivityItem) -> Unit,
	onActivityClick: (ActivityItem) -> Unit,
	modifier: Modifier
) {
	Card(
		modifier = modifier
			.clickable { onActivityClick(activity) }
			.fillMaxWidth()
			.fillMaxHeight(),
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically
		) {
			Box(
				modifier = Modifier.weight(1f),
				contentAlignment = Alignment.Center,
			) {
				Text(
					text = "${activity.weight}"
				)
			}
			Box(
				modifier = Modifier.weight(5f),
				contentAlignment = Alignment.CenterStart,
			) {
				Text(
					text = activity.name
				)
			}
			ActionIcon(
				modifier = Modifier.weight(1f),
				onClick = { onEditClick(activity) },
				backgroundColor = Color.Transparent,
				tint = Color.DarkGray,
				icon = Icons.Default.Edit
			)
			ActionIcon(
				modifier = Modifier.weight(1f),
				onClick = { onDeleteClick(activity) },
				backgroundColor = Color.Transparent,
				tint = Color.DarkGray,
				icon = Icons.Default.Delete
			)
		}
	}
}

@Preview(
	showBackground = true, device = Devices.PIXEL_7_PRO, showSystemUi = true
)
@Composable
fun ActivityListPreview() {
	val activities = listOf(
		ActivityUI((Goal(id = 1, name = "Activity 1", weight = 10))),
		ActivityUI((Goal(id = 1, name = "Activity 2", weight = 10))),
		ActivityUI((Goal(id = 1, name = "Activity 3", weight = 10))),
		ActivityUI((Goal(id = 1, name = "Activity 4", weight = 10))),
		ActivityUI((Goal(id = 1, name = "Activity 5", weight = 10))),
		ActivityUI((Goal(id = 1, name = "Activity 6", weight = 10))),
		ActivityUI((Goal(id = 1, name = "Activity 7", weight = 10))),
		ActivityUI((Goal(id = 1, name = "Activity 8", weight = 10))),
		ActivityUI((Goal(id = 1, name = "Activity 9", weight = 10))),
		ActivityUI((Goal(id = 1, name = "Activity 10", weight = 10))),
		ActivityUI((Goal(id = 1, name = "Activity 11", weight = 10))),
		ActivityUI((Goal(id = 1, name = "Activity 12", weight = 10))),
		ActivityUI((Goal(id = 1, name = "Activity 13", weight = 10))),
		ActivityUI((Goal(id = 1, name = "Activity 14", weight = 10))),
		ActivityUI((Goal(id = 1, name = "Activity 15", weight = 10))),
		ActivityUI((Goal(id = 1, name = "Activity 16", weight = 10))),
	)
	Column() {
		Spacer(modifier = Modifier.size(200.dp))
		ActivityListComposable(
			activities,
			onAddActivityButtonClick = {},
			onEditClick = {},
			onDeleteClick = {},
			onActivityClick = {},
		)
	}
}

@Preview(showBackground = true)
@Composable
fun ActivityItemPreview() {
	ActivityComposable(
		activity = Goal(id = 1, name = "Activity 1", weight = 10), onEditClick = {},

		onDeleteClick = {}, onActivityClick = {}, modifier = Modifier
	)
}
