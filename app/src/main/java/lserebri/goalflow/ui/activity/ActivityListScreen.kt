package lserebri.goalflow.ui.activity

import lserebri.goalflow.ui.components.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.RocketLaunch
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import lserebri.goalflow.data.activity.ActivityItem
import lserebri.goalflow.data.distraction.Distraction
import lserebri.goalflow.data.goal.Goal
import lserebri.goalflow.ui.components.ActivityDialog
import lserebri.goalflow.ui.components.DeleteConfirmationDialog
import lserebri.goalflow.ui.home.HomeViewModel

@Composable
private fun DeleteActivityDialogHandler(
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
private fun EditActivityDialogHandler(
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
private fun ActivityTimePickerDialogHandler(
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
	activityViewModel: ActivityViewModel, homeViewModel: HomeViewModel = hiltViewModel()
) {
	// Calls loadActivities() once when the screen is first composed
	LaunchedEffect(Unit) {
		activityViewModel.loadActivities()
	}

	val activityUIList by activityViewModel.activities.collectAsState()

	if (activityUIList.isEmpty()) {
		EmptyActivityState(isGoal = activityViewModel.isGoal)
	} else {
		ActivityListComposable(
			activityUIList,
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
	}

	val showDeleteDialog by activityViewModel.showDeleteDialog.collectAsState()
	val selectedActivity by activityViewModel.selectedActivity.collectAsState()
	val showEditDialog by activityViewModel.showEditDialog.collectAsState()
	val showTimePickerDialog by activityViewModel.showTimePickerDialog.collectAsState()

	DeleteActivityDialogHandler(
		show = showDeleteDialog,
		selectedActivity = selectedActivity,
		onDismiss = { activityViewModel.onDeleteDialogDismiss() },
		onConfirm = {
			activityViewModel.deleteActivity(it)
			activityViewModel.onDeleteDialogDismiss()
		})

	EditActivityDialogHandler(
		show = showEditDialog,
		selectedActivity = selectedActivity,
		onDismiss = { activityViewModel.onEditDialogDismiss() },
		onSave = { name, weight ->
			val updated = when (selectedActivity) {
				is Goal -> (selectedActivity as Goal).copy(name = name, weight = weight)
				is Distraction -> (selectedActivity as Distraction).copy(
					name = name, weight = weight
				)

				else -> return@EditActivityDialogHandler
			}
			activityViewModel.updateActivity(updated)
			activityViewModel.onEditDialogDismiss()
		})

	ActivityTimePickerDialogHandler(
		show = showTimePickerDialog,
		onDismiss = { activityViewModel.onTimePickerDismiss() },
		onConfirm = { hours, minutes ->
			selectedActivity?.let {
				homeViewModel.updateProgress(
					(hours * 60 + minutes), it.weight, isGoal = activityViewModel.isGoal
				)
			}
			activityViewModel.onTimePickerDismiss()
		})
}

@Composable
private fun EmptyActivityState(isGoal: Boolean) {
	Column(
		modifier = Modifier.fillMaxSize(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
	) {
		Icon(
			imageVector = if (isGoal) Icons.Outlined.RocketLaunch else Icons.Outlined.SportsEsports,
			contentDescription = null,
			modifier = Modifier.size(64.dp),
			tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
		)
		Spacer(modifier = Modifier.height(16.dp))
		Text(
			text = if (isGoal) "No goals yet" else "No distractions yet",
			style = MaterialTheme.typography.titleMedium,
			color = MaterialTheme.colorScheme.onSurface,
		)
		Spacer(modifier = Modifier.height(4.dp))
		Text(
			text = if (isGoal) "Tap + to add your first goal"
			else "Tap + to track what slows you down",
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center,
		)
	}
}

@Composable
fun ActivityListComposable(
	activities: List<ActivityUI>,
	onEditClick: (ActivityItem) -> Unit,
	onDeleteClick: (ActivityItem) -> Unit,
	onActivityClick: (ActivityItem) -> Unit
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
				modifier = Modifier.padding(top = 4.dp),
				verticalArrangement = Arrangement.spacedBy(6.dp),
			) {
				items(activities) { activityItem ->
					ActivityComposable(
						activity = activityItem.activity,
						onEditClick = onEditClick,
						onDeleteClick = onDeleteClick,
						onActivityClick = onActivityClick,
						modifier = Modifier.height(cardHeight)
					)
				}
			}
		}
	}
}

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
			.padding(horizontal = 4.dp)
			.fillMaxHeight(),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surfaceVariant
		),
	) {
		Row(
			modifier = Modifier
				.fillMaxSize()
				.padding(horizontal = 12.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(10.dp)
		) {
			Surface(
				color = MaterialTheme.colorScheme.primaryContainer,
				shape = MaterialTheme.shapes.extraSmall,
			) {
				Text(
					text = "${activity.weight}",
					modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
					style = MaterialTheme.typography.titleSmall,
					color = MaterialTheme.colorScheme.onPrimaryContainer,
					fontWeight = FontWeight.Bold,
				)
			}
			Text(
				text = activity.name,
				modifier = Modifier.weight(1f),
				style = MaterialTheme.typography.titleLarge,
			)
			ActionIcon(
				onClick = { onEditClick(activity) },
				backgroundColor = Color.Transparent,
				icon = Icons.Default.Edit,
				tint = MaterialTheme.colorScheme.primary
			)
			ActionIcon(
				onClick = { onDeleteClick(activity) },
				backgroundColor = Color.Transparent,
				icon = Icons.Default.Delete,
				tint = MaterialTheme.colorScheme.error
			)
		}
	}
}

@Composable
fun ActionIcon(
	onClick: () -> Unit,
	backgroundColor: Color,
	icon: ImageVector,
	modifier: Modifier = Modifier,
	contentDescription: String? = null,
	tint: Color = Color.Unspecified
) {
	IconButton(
		onClick = onClick,
		modifier = modifier.background(backgroundColor, shape = RoundedCornerShape(12.dp))
	) {
		Icon(
			imageVector = icon,
			contentDescription = contentDescription,
			tint = tint
		)
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
