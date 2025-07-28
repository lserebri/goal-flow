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
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.outlined.RocketLaunch
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.goalflow.data.distraction.Distraction
import com.example.goalflow.data.goal.Goal
import com.example.goalflow.ui.activity.ActivityListScreenWithFactory
import com.example.goalflow.ui.activity.ActivityViewModel
import com.example.goalflow.ui.components.ActivityDialog

data class TabItem(
	val title: String,
	val selectedIcon: ImageVector,
	val unselectedIcon: ImageVector,
	val route: String
)

val tabItems = listOf(
	TabItem(
		title = "Goals",
		selectedIcon = Icons.Filled.RocketLaunch,
		unselectedIcon = Icons.Outlined.RocketLaunch,
		route = "goals?isFirstTab=true"
	), TabItem(
		title = "Distractions",
		selectedIcon = Icons.Filled.SportsEsports,
		unselectedIcon = Icons.Outlined.SportsEsports,
		route = "distractions?isFirstTab=false"
	)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityTabPager(modifier: Modifier = Modifier) {
	val startTabIndex = 0
	var selectedDestination by rememberSaveable {
		mutableIntStateOf(startTabIndex)
	}

	val pagerState = rememberPagerState {
		tabItems.size
	}
	LaunchedEffect(selectedDestination) {
		pagerState.animateScrollToPage(selectedDestination)
	}
	LaunchedEffect(pagerState.currentPage) {
		selectedDestination = pagerState.currentPage
	}

	Column(modifier = modifier) {
		TabRow(selectedTabIndex = selectedDestination) {
			tabItems.forEachIndexed { index, tabItem ->
				Tab(selected = selectedDestination == index, onClick = {
					selectedDestination = index
				}, text = {
					Text(
						text = tabItem.title, maxLines = 2, overflow = TextOverflow.Ellipsis
					)
				}, icon = {
					Icon(
						imageVector = if (selectedDestination == index) {
							tabItem.selectedIcon
						} else tabItem.unselectedIcon, contentDescription = tabItem.title
					)
				})
			}
		}

		HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
			val isGoal = (page == 0)
			ActivityListScreenWithFactory(isGoal = isGoal)
		}
	}
}

@Composable
fun ScoreComposable(modifier: Modifier, score: String) {
	Box(
		modifier = modifier
			.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		Text(
			text = score,
			style = MaterialTheme.typography.headlineLarge,
			fontSize = 60.sp
		)
	}
}

@Composable
private fun AddActivityDialog(
    show: Boolean, 
    onDismiss: () -> Unit, 
    onSave: (String, Int) -> Unit
) {
    if (show) {
        ActivityDialog(
            onDismiss = onDismiss, 
            onSave = { name, weight -> onSave(name, weight) }
        )
    }
}

@Composable
fun HomeScreen(
	homeViewModel: HomeViewModel = hiltViewModel(),
	activityViewModel: ActivityViewModel = hiltViewModel(
		key = "goal",
		creationCallback = { factory: ActivityViewModel.ActivityViewModelFactory ->
			factory.create(isGoal = true)
		}
	)
) {
	val uiState by homeViewModel.score.collectAsState()
	val showAddActivityDialog by activityViewModel.showAddActivityDialog.collectAsState()


	if (uiState is ScoreUiState.Success) {
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
					ActivityTabPager(Modifier.weight(2f))
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
		AddActivityDialog(
			show = showAddActivityDialog,
			onDismiss = { activityViewModel.onAddActivityDismiss() },
			onSave = { name, weight ->
				activityViewModel.add(
					if (activityViewModel.isGoal) Goal(name = name, weight = weight)
					else Distraction(name = name, weight = weight)
				)
				activityViewModel.onAddActivityDismiss()
			})
	}
}




