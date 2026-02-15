package lserebri.goalflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import lserebri.goalflow.ui.theme.GoalFlowTheme
import dagger.hilt.android.AndroidEntryPoint
import lserebri.goalflow.ui.home.HomeScreen

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
