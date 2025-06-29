package com.example.goalflow.ui.activity

import TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import com.example.goalflow.data.activity.ActivityItem
import com.example.goalflow.data.consumable.Consumable
import com.example.goalflow.data.goal.Goal
import com.example.goalflow.ui.action.ActionIcon
import com.example.goalflow.ui.action.SwappableItemWithAction
import com.example.goalflow.ui.components.ActivityDialog
import com.example.goalflow.ui.components.DeleteConfirmationDialog
import com.example.goalflow.ui.home.HomeViewModel

@Composable
fun ActivityListScreen(
	isGoal: Boolean,
	activityViewModel: ActivityViewModel = hiltViewModel()
) {
	val uiState by activityViewModel.getAll.collectAsState()

	var showDeleteDialog by remember { mutableStateOf(false) }
	var showAddActivityDialog by remember { mutableStateOf(false) }
	var showEditDialog by remember { mutableStateOf(false) }
	var selectedActivity by remember { mutableStateOf<ActivityItem?>(null) }

	var activityUIList by remember { mutableStateOf(listOf<ActivityUI>()) }

	if (uiState is ActivityUIState.Success) {
		val activities = (uiState as ActivityUIState.Success).data
		activityUIList = remember(activities) {
			activities.map { ActivityUI(it) }.sortedByDescending { it.activity.weight }
		}
	}

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp)
	) {
		LazyColumn(modifier = Modifier.weight(1f)) {
			items(activityUIList) { activityUI ->
				SwappableItemWithAction(
					isRevealed = activityUI.isOptionsRevealed,
					onExpanded = {
						activityUIList = activityUIList.map {
							if (it.activity.id == activityUI.activity.id) it.copy(isOptionsRevealed = true)
							else it.copy(isOptionsRevealed = false)
						}
					},
					onCollapsed = {
						activityUIList = activityUIList.map {
							if (it.activity.id == activityUI.activity.id) it.copy(isOptionsRevealed = false)
							else it
						}
					},
					actions = {
						ActionIcon(
							onClick = {
								selectedActivity = activityUI.activity
								showDeleteDialog = true
							},
							backgroundColor = Color.Red,
							icon = Icons.Default.Delete
						)
						ActionIcon(
							onClick = {
								selectedActivity = activityUI.activity
								showEditDialog = true
							},
							backgroundColor = Color.Blue,
							icon = Icons.Default.Edit
						)
					}

				) {
					ActivityItemComposable(
						activity = activityUI.activity,
						isGoal = isGoal,
						onClick = {
							// Collapse all swipes
							activityUIList = activityUIList.map { it.copy(isOptionsRevealed = false) }
						})
				}
			}
		}

		Button(
			onClick = { showAddActivityDialog = true },
			modifier = Modifier.fillMaxWidth()
		) {
			Text(if (isGoal) "Add Goal" else "Add Consumable")
		}
	}

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
					is Consumable -> (selectedActivity as Consumable).copy(name = name, weight = weight)
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
						Consumable(name = name, weight = weight))
				showAddActivityDialog = false
			}
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityItemComposable(
	activity: ActivityItem,
	isGoal: Boolean,
	onClick: () -> Unit,
	homeViewModel: HomeViewModel = hiltViewModel()
) {
	var showDialog by remember { mutableStateOf(false) }

	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(8.dp)
			.clickable {
				onClick()
				showDialog = true
			},
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Text(text = activity.name)
		Text(text = "Weight: ${activity.weight}")
	}

	if (showDialog) {
		TimePickerDialog(
			initialHour = 0,
			initialMinute = 0,
			onDismiss = { showDialog = false },
			onConfirm = { hour, minute ->
				homeViewModel.updateScore(
					(hour * 60 + minute),
					activity.weight,
					isGoal = isGoal
				)
				showDialog = false
			}
		)
	}
}
