package com.example.goalflow.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue



@Composable()
fun HomeScreen(homeViewModel: HomeViewModel= hiltViewModel()) {
    val uiState by homeViewModel.score.collectAsState()

    if (uiState is ScoreUiState.Success) {
        val score = (uiState as ScoreUiState.Success).data

        Box (modifier = Modifier
            .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = score.toString(),
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}