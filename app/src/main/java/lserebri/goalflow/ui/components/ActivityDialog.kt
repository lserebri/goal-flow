package lserebri.goalflow.ui.components

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

@Composable
fun ActivityDialog(
	onDismiss: () -> Unit,
	onSave: (String, Int) -> Unit,
	initialName: String = "",
	initialWeight: Int = 0,
) {
	var name by remember { mutableStateOf(initialName) }
	var weight by remember { mutableStateOf(initialWeight.toString()) }

	val weightNum = weight.toIntOrNull()

	val isWeightValid = weightNum != null && weightNum in 0..10
	val isFormValid = isWeightValid

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
					value = if (weight == "0") "" else weight,
					placeholder = { Text("1... 10") },
					singleLine = true,
					onValueChange = { weight = it },
					label = { Text("Weight") },
					isError = weight.isNotEmpty() && !isWeightValid,
					keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
					supportingText = {
						if (weight.isNotEmpty() && !isWeightValid) Text("Enter a number between 1 and 10")
					})
			}
		},
		confirmButton = {
			TextButton(
				onClick = {
					val weightValue = weight.toIntOrNull() ?: 1
					onSave(name, weightValue)
				},
				enabled = isFormValid
			) {
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