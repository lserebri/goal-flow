package com.example.goalflow.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.outlined.RocketLaunch
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow


enum class ActivityTabType {
	GOALS, DISTRACTIONS,
}

data class TabItem(
	val title: String,
	val type: ActivityTabType,
	val selectedIcon: ImageVector,
	val unselectedIcon: ImageVector,
	val route: String
)

val tabItems = listOf(
	TabItem(
		title = "Goals",
		type = ActivityTabType.GOALS,
		selectedIcon = Icons.Filled.RocketLaunch,
		unselectedIcon = Icons.Outlined.RocketLaunch,
		route = "goals?isFirstTab=true"
	), TabItem(
		title = "Distractions",
		type = ActivityTabType.DISTRACTIONS,
		selectedIcon = Icons.Filled.SportsEsports,
		unselectedIcon = Icons.Outlined.SportsEsports,
		route = "distractions?isFirstTab=false"
	)
)

@Composable
fun ActivityTabs(
	selectedTab: Int, setSelectedTab: (Int) -> Unit, setActiveActivityTab: (ActivityTabType) -> Unit
) {
	TabRow(selectedTabIndex = selectedTab) {
		tabItems.forEachIndexed { index, tabItem ->
			Tab(selected = selectedTab == index, onClick = {
				setSelectedTab(index)
				setActiveActivityTab(tabItem.type)
			}, text = {
				Text(
					text = tabItem.title, maxLines = 2, overflow = TextOverflow.Ellipsis
				)
			}, icon = {
				Icon(
					imageVector = if (selectedTab == index) {
						tabItem.selectedIcon
					} else tabItem.unselectedIcon, contentDescription = tabItem.title
				)
			})
		}
	}
}