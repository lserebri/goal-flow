package com.example.goalflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.goalflow.ui.home.HomeScreen
import com.example.goalflow.ui.theme.GoalFlowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoalFlowTheme {
                HomeScreen()
            }
        }
    }
}
