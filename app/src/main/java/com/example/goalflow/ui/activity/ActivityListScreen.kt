package com.example.goalflow.ui.activity

import TimePickerDialog
import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun ActivityListScreen(
	isGoal: Boolean,
	activityViewModel: ActivityViewModel,
	homeViewModel: HomeViewModel = hiltViewModel()
) {
	val uiState by activityViewModel.getAll.collectAsState()

	var showDeleteDialog by remember { mutableStateOf(false) }
	var showAddActivityDialog by remember { mutableStateOf(false) }
	var showEditDialog by remember { mutableStateOf(false) }
	var selectedActivity by remember { mutableStateOf<ActivityItem?>(null) }
	var showTimePickerDialog by remember { mutableStateOf(false) }


	var activityUIList by remember { mutableStateOf(listOf<ActivityUI>()) }

	if (uiState is ActivityUIState.Success) {
		val activities = (uiState as ActivityUIState.Success).data
		activityUIList = remember(activities) {
			activities.map { ActivityUI(it) }.sortedByDescending { it.activity.weight }
		}
	}

	ActivityListComposable(
		activityUIList,
		onAddActivityButtonClick = {
			showAddActivityDialog = true
		},
		onEditClick = {
			selectedActivity = it
			showEditDialog = true
		},
		onDeleteClick = {
			selectedActivity = it
			showDeleteDialog = true
		},
		onActivityClick = {
			selectedActivity = it
			showTimePickerDialog = true
		},
	)

	if (showDeleteDialog && selectedActivity != null) {
		DeleteConfirmationDialog(
			dialogTitle =  "Delete Activity",
			dialogSubTitle = "Are you sure you want to delete \"${selectedActivity!!.name}\"?",
			onDismissRequest = { showDeleteDialog = false },
			onConfirmation = {
				activityViewModel.delete(selectedActivity!!)
				showDeleteDialog = false
			}
		)
	}

	if (showEditDialog && selectedActivity != null) {
		ActivityDialog(
			initialName = selectedActivity!!.name,
			initialWeight = selectedActivity!!.weight,
			onDismiss = { showEditDialog = false },
			onSave = { name, weight ->
				val updated = when (selectedActivity) {
					is Goal -> (selectedActivity as Goal).copy(name = name, weight = weight)
					is Distraction -> (selectedActivity as Distraction).copy(name = name, weight = weight)
					else -> return@ActivityDialog
				}
				activityViewModel.add(updated) // this uses insert(onConflict = REPLACE)
				showEditDialog = false
			}

		)
	}

	if (showAddActivityDialog) {
		ActivityDialog(
			onDismiss = { showAddActivityDialog = false },
			onSave = { name, weight ->
				activityViewModel.add(
					if (isGoal)
						Goal(name = name, weight = weight)
					else
						Distraction(name = name, weight = weight))
				showAddActivityDialog = false
			}
		)
	}

	if (showTimePickerDialog) {
		TimePickerDialog(
			initialHour = -1,
			initialMinute = -1,
			onDismiss = { showTimePickerDialog = false },
			onConfirm = { hour, minute ->
				homeViewModel.updateScore(
					(hour * 60 + minute),
					selectedActivity!!.weight,
					isGoal = isGoal
				)
				showTimePickerDialog = false
			},
		)
	}
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
				modifier = Modifier
					.weight(1f),
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
	showBackground = true,
	device = Devices.PIXEL_7_PRO,
	showSystemUi = true
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
		activity = Goal(id = 1, name = "Activity 1", weight = 10),
		onEditClick = {},

		onDeleteClick = {},
		onActivityClick = {},
		modifier = Modifier
	)
}
