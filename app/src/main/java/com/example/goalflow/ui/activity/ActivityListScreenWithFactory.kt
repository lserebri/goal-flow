package com.example.goalflow.ui.activity

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ActivityListScreenWithFactory(
	isGoal: Boolean,
) {
	val viewModel: ActivityViewModel = hiltViewModel(
		key = if (isGoal) { "goal" } else { "distraction" },
		creationCallback = { factory: ActivityViewModel.ActivityViewModelFactory ->
			factory.create(isGoal = isGoal)
		}
	)
	ActivityListScreen(
		isGoal = isGoal,
		activityViewModel = viewModel,
	)
}
