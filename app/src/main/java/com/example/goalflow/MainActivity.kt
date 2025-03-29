package com.example.goalflow

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.goalflow.ui.theme.GoalFlowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoalListScreen()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GoalListScreen() {
    var goals by remember { mutableStateOf(listOf<Pair<String, Int>>()) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Text("Add")
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            LazyColumn {
                items(goals) { goal ->
                    GoalItem(goal, onDelete = {
                        goals = goals.filter { it != goal }
                    })
                }
            }
        }
    }

//    if (showDialog) {
//        GoalInputDialog(onDismiss = { showDialog = false }) { name, weight ->
//            goals = goals + (name to weight)
//            showDialog = false
//        }
//    }
}

@Composable
fun GoalItem(goal: Pair<String, Int>, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onDelete() },
        backgroundColor = Color.LightGray
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Text("${goal.first} - Weight: ${goal.second}")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewGoalListScreen() {
    GoalListScreen()
}