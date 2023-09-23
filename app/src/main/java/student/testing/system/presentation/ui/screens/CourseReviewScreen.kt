@file:OptIn(ExperimentalMaterial3Api::class)

package student.testing.system.presentation.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import student.testing.system.presentation.navigation.DestinationMain
import student.testing.system.presentation.navigation.NavHostMain
import student.testing.system.presentation.navigation.composable
import student.testing.system.presentation.ui.activity.ui.theme.Purple500

@Composable
fun CoursesScreen() {
    val navController = rememberNavController()
    val items = listOf(
        DestinationMain.TestsScreen,
        DestinationMain.ParticipantsScreen,
    )
    Scaffold(
        bottomBar = {
            BottomNavigation(backgroundColor = Color.White) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    val selected = currentDestination?.route == screen.fullRoute
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                painter = painterResource(screen.drawableId),
                                contentDescription = null,
                                tint = if (selected) Purple500 else Color.Black
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(screen.stringRes),
                                fontSize = 12.sp,
                                color = if (selected) Purple500 else Color.Black,
                            )
                        },
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.fullRoute) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHostMain(
            navController,
            startDestination = DestinationMain.TestsScreen,
            Modifier.padding(innerPadding)
        ) {
            composable(DestinationMain.TestsScreen) { TestsScreen() }
            composable(DestinationMain.ParticipantsScreen) { ParticipantsScreen() }
        }
    }
}