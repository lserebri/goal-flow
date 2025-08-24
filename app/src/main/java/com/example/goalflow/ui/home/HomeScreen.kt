package com.example.goalflow.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.goalflow.data.distraction.Distraction
import com.example.goalflow.data.goal.Goal
import com.example.goalflow.ui.activity.ActivityListScreen
import com.example.goalflow.ui.activity.provideActivityViewModel
import com.example.goalflow.ui.components.ActivityDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityTabPager(
	modifier: Modifier = Modifier, setActiveActivityTab: (ActivityTabType) -> Unit
) {
	val startTabIndex = 0
	var selectedTab by rememberSaveable {
		mutableIntStateOf(startTabIndex)
	}

	val pagerState = rememberPagerState {
		tabItems.size
	}
	LaunchedEffect(selectedTab) {
		pagerState.animateScrollToPage(selectedTab)
	}
	LaunchedEffect(pagerState.currentPage) {
		selectedTab = pagerState.currentPage
	}

	Column(modifier = modifier) {
		ActivityTabs(
			selectedTab = selectedTab,
			setSelectedTab = { selectedTab = it },
			setActiveActivityTab = setActiveActivityTab
		)
		HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
			val activityViewModel = provideActivityViewModel(tabItems[page].type)
			ActivityListScreen(activityViewModel = activityViewModel)
		}
	}
}

@Composable
fun ScoreComposable(modifier: Modifier, score: String) {
	Box(
		modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
	) {
		Text(
			text = score, style = MaterialTheme.typography.headlineLarge, fontSize = 60.sp
		)
	}
}

@Composable
private fun AddActivityDialogHandler(
	show: Boolean, onDismiss: () -> Unit, onSave: (String, Int) -> Unit
) {
	if (show) {
		ActivityDialog(
			onDismiss = onDismiss, onSave = { name, weight -> onSave(name, weight) })
	}
}

@Composable
fun HomeScreen(
	homeViewModel: HomeViewModel = hiltViewModel(),
) {
	val uiState by homeViewModel.score.collectAsState()

	val activityViewModel = provideActivityViewModel(homeViewModel.currentActivityTab.value)

	val showAddActivityDialog by activityViewModel.showAddActivityDialog.collectAsState()



	when (uiState) {
		is ScoreUiState.Loading -> {
			Scaffold { contentPadding ->
				Box(
					modifier = Modifier
						.padding(contentPadding)
						.fillMaxSize(),
					contentAlignment = Alignment.Center
				) {
					Text("Loading…")
				}
			}
		}

		is ScoreUiState.Error -> {
			Scaffold { contentPadding ->
				Box(
					modifier = Modifier
						.padding(contentPadding)
						.fillMaxSize(),
					contentAlignment = Alignment.Center
				) {
					Text("Failed to load score")
				}
			}
		}

		is ScoreUiState.Success -> {
			val score = (uiState as ScoreUiState.Success).data

			Scaffold { contentPadding ->
				Box(
					modifier = Modifier
						.padding(contentPadding)
						.fillMaxSize()
				) {
					Column(
						modifier = Modifier
							.fillMaxSize()
							.padding(10.dp)
					) {
						ScoreComposable(modifier = Modifier.weight(1f), score.toString())
						ActivityTabPager(
							Modifier.weight(2f),
							setActiveActivityTab = homeViewModel::setCurrentActivityTab
						)
					}

					FloatingActionButton(
						shape = RoundedCornerShape(16.dp),
						onClick = { activityViewModel.onAddActivityClick() },
						containerColor = MaterialTheme.colorScheme.secondary,
						modifier = Modifier
							.align(Alignment.BottomEnd)
							.padding(26.dp)
					) {
						Icon(Icons.Filled.Add, "Floating action button.")
					}
				}
			}

			AddActivityDialogHandler(
				show = showAddActivityDialog,
				onDismiss = { activityViewModel.onAddActivityDismiss() },
				onSave = { name, weight ->
					activityViewModel.addActivity(
						if (activityViewModel.isGoal) Goal(name = name, weight = weight)
						else Distraction(name = name, weight = weight)
					)
					activityViewModel.onAddActivityDismiss()
				})
		}
	}
}




