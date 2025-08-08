package com.example.goalflow.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import timber.log.Timber

@Composable
fun ActivityDialog(
	onDismiss: () -> Unit,
	onSave: (String, Int) -> Unit,
	initialName: String = "",
	initialWeight: Int = 0,
) {
	var name by remember { mutableStateOf(initialName) }
	var weight by remember { mutableStateOf(initialWeight.toString()) }

	AlertDialog(
		onDismissRequest = onDismiss,
		title = { Text(if (initialName.isEmpty()) "Add Activity" else "Edit Activity") },
		text = {
			Column {
				OutlinedTextField(
					value = name,
					singleLine = true,
					onValueChange = { name = it },
					label = { Text("Name") }
				)
				OutlinedTextField(
					value = if (weight == "0") ""  else weight,
					placeholder = { Text("From 1 to 10") },
					singleLine = true,
					onValueChange = { weight = it },
					label = { Text("Weight") },
					keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
				)
			}
		},
		confirmButton = {
			TextButton(onClick = {
				val weightValue = weight.toIntOrNull() ?: 1
				Timber.d("Saving activity: name='$name', weight=$weightValue")
				onSave(name, weightValue)
			}) {
				Text("Save")
			}
		},
		dismissButton = {
			TextButton(onClick = onDismiss) {
				Text("Cancel")
			}
		}
	)
}