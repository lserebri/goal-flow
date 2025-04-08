package com.example.goalflow.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.goalflow.data.Goal

@Composable
fun GoalListScreen(goalViewModel: GoalViewModel) {
    val goals = goalViewModel.allGoals
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.Companion.fillMaxSize().padding(16.dp)) {
        // Goal List
        LazyColumn(modifier = Modifier.Companion.weight(1f)) {
            items(goals) { goal -> // Correct!
                GoalItem(goal = goal, onDelete = { goalViewModel.deleteGoal(goal) })
            }
        }

        // Add Goal Button
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.Companion.fillMaxWidth()
        ) {
            Text("Add Goal")
        }
    }

    // Add Goal Dialog
    if (showDialog) {
        AddGoalDialog(onDismiss = { showDialog = false }, onSave = { name, weight ->
            goalViewModel.addGoal(Goal(name = name, weight = weight))
            showDialog = false
        })
    }
}

@Composable
fun GoalItem(goal: Goal, onDelete: () -> Unit) {
    Row(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onDelete() },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = goal.name)
        Text(text = "Weight: ${goal.weight}")
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