package com.example.goalflow.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.goalflow.ui.activity.ActivityListScreen

data class Destination(
    val name: String,
    val route: String
)

val destinations = listOf(
    Destination("Goals", "goals"),
    Destination("Consumables", "consumables")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationTab(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val startDestination = Destination("Goals", "goals")
    var selectedDestination by rememberSaveable {
        mutableIntStateOf(destinations.indexOf(startDestination))
    }

    Column(modifier = modifier) {
        SecondaryTabRow(selectedTabIndex = selectedDestination) {
            destinations.forEachIndexed { index, destination ->
                Tab(
                    selected = selectedDestination == index,
                    onClick = {
                        val route = if (destination.route == "goals") {
                            "goals?isFirstTab=true"
                        } else {
                            "consumables?isFirstTab=false"
                        }
                        navController.navigate(route) {
                            // Avoid building up a back stack of tabs
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                        selectedDestination = index
                    },
                    text = {
                        Text(
                            text = destination.name,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }

        NavHost(
            navController = navController,
            startDestination = "goals",
            modifier = Modifier.weight(1f)
        ) {
            composable(
                "goals?isFirstTab={isFirstTab}",
                arguments = listOf(navArgument("isFirstTab") {
                    type = NavType.BoolType
                    defaultValue = true
                })
            ) {
                ActivityListScreen(isGoal = true)
            }

            composable(
                "consumables?isFirstTab={isFirstTab}",
                arguments = listOf(navArgument("isFirstTab") {
                    type = NavType.BoolType
                    defaultValue = false
                })
            ) {
                ActivityListScreen(isGoal = false)
            }
        }
    }
}

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = hiltViewModel()) {
    val uiState by homeViewModel.score.collectAsState()

    if (uiState is ScoreUiState.Success) {
        val score = (uiState as ScoreUiState.Success).data

        Scaffold { contentPadding ->
            Column(modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .padding(25.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 20.dp)
                        .weight(0.2f), // Top section for score
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = score.toString(),
                        style = MaterialTheme.typography.headlineLarge,
                        fontSize = 60.sp
                    )
                }

//                Spacer(modifier = Modifier.size(100.dp))

                NavigationTab(modifier = Modifier.weight(0.3f))
            }
        }
    }
}


