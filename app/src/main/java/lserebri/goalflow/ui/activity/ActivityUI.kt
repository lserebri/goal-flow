package lserebri.goalflow.ui.activity

import lserebri.goalflow.data.activity.ActivityItem

data class ActivityUI(
	val activity: ActivityItem,
	val isOptionsRevealed: Boolean = false
)
