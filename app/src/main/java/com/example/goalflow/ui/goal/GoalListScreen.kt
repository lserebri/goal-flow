package com.example.goalflow.ui.goal

import androidx.compose.foundation.clickable
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import com.example.goalflow.data.goal.Goal
import com.example.goalflow.ui.action.ActionIcon
import com.example.goalflow.ui.action.SwappableItemWithAction
import com.example.goalflow.ui.home.HomeViewModel


@Composable
fun GoalListScreen(goalViewModel: GoalViewModel = hiltViewModel()) {
    val uiState by goalViewModel.allGoals.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var goalUIList by remember { mutableStateOf(listOf<GoalUI>()) }

    if (uiState is GoalUiState.Success) {
        val goals = (uiState as GoalUiState.Success).data
        goalUIList = remember(goals) {
            goals.map { GoalUI(it) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(goalUIList) { goalUI ->
                SwappableItemWithAction(
                    isRevealed = goalUI.isOptionsRevealed,
                    onExpanded = {
                        goalUIList = goalUIList.map {
                            if (it.goal.id == goalUI.goal.id) it.copy(isOptionsRevealed = true)
                            else it.copy(isOptionsRevealed = false)
                        }
                    },
                    onCollapsed = {
                        goalUIList = goalUIList.map {
                            if (it.goal.id == goalUI.goal.id) it.copy(isOptionsRevealed = false)
                            else it
                        }
                    },
                    actions = {
                        ActionIcon(
                            onClick = {
                                goalViewModel.deleteGoal(goalUI.goal)
                            },
                            backgroundColor = Color.Red,
                            icon = Icons.Default.Delete
                        )
                        ActionIcon(
                            onClick = { },
                            backgroundColor = Color.Yellow,
                            icon = Icons.Default.Edit
                        )
                    }
                ) {
                    GoalItem(goal = goalUI.goal)
                }
            }
        }

//        Spacer(modifier = Modifier.size(50.dp))


        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Add Goal")
        }
    }

    if (showDialog) {
        AddGoalDialog(onDismiss = { showDialog = false }, onSave = { name, weight ->
            goalViewModel.addGoal(Goal(name = name, weight = weight))
            showDialog = false
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
) {
    val timePickerState = rememberTimePickerState(
        initialHour = 0,
        initialMinute = 0,
        is24Hour = true,
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(timePickerState) }) {
                Text("OK")
            }
        },
        text = {
            TimePicker(
                state = timePickerState,
            )
        }
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalItem(goal: Goal, homeViewModel: HomeViewModel = hiltViewModel()) {
    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { showDialog = true },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = goal.name)
        Text(text = "Weight: ${goal.weight}")
    }
    if (showDialog) {
        TimePickerDialog(
            onDismiss = { showDialog = false },
            onConfirm = {
                homeViewModel.updateScore(
                    ((it.hour * 60) + it.minute),
                    goal.weight,
                    isGoal = true,
                )
                showDialog = false
            }
        )
    }
}

@Composable
fun AddGoalDialog(onDismiss: () -> Unit, onSave: (String, Int) -> Unit) {
    var name by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Goal") },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Goal Name") })
                Spacer(modifier = Modifier.Companion.height(8.dp))
                TextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Weight") })
            }
        },
        confirmButton = {
            Button(onClick = {
                val weightInt = weight.toIntOrNull() ?: 0
                if (name.isNotBlank() && weightInt > 0) {
                    onSave(name, weightInt)
                }
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Cancel") }
        }
    )
}