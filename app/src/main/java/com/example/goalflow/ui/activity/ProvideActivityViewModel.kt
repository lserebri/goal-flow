package com.example.goalflow.ui.activity

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.goalflow.ui.home.ActivityTabType

@Composable
fun provideActivityViewModel(currentActivityTab: ActivityTabType): ActivityViewModel {
	return hiltViewModel(
		key = currentActivityTab.name,
		creationCallback = { factory: ActivityViewModel.ActivityViewModelFactory ->
			factory.create(isGoal = currentActivityTab == ActivityTabType.GOALS)
		})
}
