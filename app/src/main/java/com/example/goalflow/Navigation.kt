//package com.example.goalflow
//
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.NavigationBar
//import androidx.compose.material3.NavigationBarItem
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material.icons.filled.List
//import androidx.compose.material.icons.filled.ShoppingCart
//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.navigation.NavGraph.Companion.findStartDestination
//import androidx.navigation.compose.*
//import com.example.goalflow.ui.home.HomeScreen
//
//data class TopLevelRoute(
//    val name: String,
//    val route: String,
//    val icon: ImageVector
//)
//
//val topLevelRoutes = listOf(
//    TopLevelRoute("Goals", "goals", Icons.Default.List),
//    TopLevelRoute("Home", "home", Icons.Default.Home),
//    TopLevelRoute("Consumables", "consumables", Icons.Default.ShoppingCart)
//)
//
//@Composable
//fun Navigation() {
//    val navController = rememberNavController()
//
//    Scaffold(
//        bottomBar = {
//            NavigationBar {
//                val navBackStackEntry by navController.currentBackStackEntryAsState()
//                val currentDestination = navBackStackEntry?.destination
//                topLevelRoutes.forEach { topLevelRoute ->
//                    NavigationBarItem(
//                        icon = { Icon(topLevelRoute.icon, contentDescription = topLevelRoute.name) },
//                        label = { Text(topLevelRoute.name) },
//                        selected = currentDestination?.route == topLevelRoute.route,
//                        onClick = {
//                            navController.navigate(topLevelRoute.route) {
//                                popUpTo(navController.graph.findStartDestination().id) {
//                                    saveState = true
//                                }
//                                launchSingleTop = true
//                                restoreState = true
//                            }
//                        }
//                    )
//                }
//            }
//        }
//    ) { innerPadding ->
//        NavHost(
//            navController = navController,
//            startDestination = "home",
//            modifier = Modifier.padding(innerPadding)
//        ) {
//            composable("home") { HomeScreen() }
//            composable("goals") { GoalListScreen() }
//            composable("consumables") { ConsumableListScreen() }
//        }
//    }
//}
