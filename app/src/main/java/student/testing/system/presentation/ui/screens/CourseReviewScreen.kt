package student.testing.system.presentation.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import student.testing.system.common.NullSharedViewModelException
import student.testing.system.common.viewModelScopedTo
import student.testing.system.models.Test
import student.testing.system.models.TestResult
import student.testing.system.presentation.navigation.Destination
import student.testing.system.presentation.navigation.NavHost
import student.testing.system.presentation.navigation.composable
import student.testing.system.presentation.ui.activity.ui.theme.Purple500
import student.testing.system.presentation.ui.components.NavigationEffects
import student.testing.system.presentation.ui.screens.participants.ParticipantsScreen
import student.testing.system.presentation.ui.screens.resultReview.ResultReviewScreen
import student.testing.system.presentation.ui.screens.resultsReview.ResultsReviewScreen
import student.testing.system.presentation.ui.screens.testCreation.TestCreationHostScreen
import student.testing.system.presentation.ui.screens.tests.TestsScreen
import student.testing.system.presentation.viewmodels.CourseReviewViewModel
import student.testing.system.presentation.viewmodels.CourseSharedViewModel

@Composable
fun CourseReviewScreen() {
    val viewModel = hiltViewModel<CourseReviewViewModel>()
    val navController = rememberNavController()
    val items = listOf(
        Destination.TestsScreen,
        Destination.ParticipantsScreen,
    )
    val showBottomBar = navController
        .currentBackStackEntryAsState().value?.destination?.route in items.map { it.fullRoute }


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val sharedViewModel = navBackStackEntry
        ?.viewModelScopedTo<CourseSharedViewModel>(navController, Destination.TestsScreen.fullRoute)
    sharedViewModel?.setCourse(viewModel.course)
    var clickedTest by remember { mutableStateOf(Test()) }
    var testResult by remember { mutableStateOf(TestResult()) }
    NavigationEffects(
        navigationChannel = viewModel.navigationChannel,
        navHostController = navController
    )
    Scaffold(
        bottomBar = {
            if (!showBottomBar) return@Scaffold
            BottomNavigation(backgroundColor = Color.White) {
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
        NavHost(
            navController,
            startDestination = Destination.TestsScreen,
            Modifier.padding(innerPadding)
        ) {
            composable(Destination.TestsScreen) {
                TestsScreen(
                    sharedViewModel ?: throw NullSharedViewModelException(),
                    onTestClicked = { clickedTest = it }) {
                    testResult = it
                }
            }
            composable(Destination.ParticipantsScreen) {
                ParticipantsScreen(sharedViewModel ?: throw NullSharedViewModelException())
            }
            composable(destination = Destination.TestCreationHostScreen) {
                TestCreationHostScreen(
                    sharedViewModel ?: throw NullSharedViewModelException()
                ) {
                    sharedViewModel.onTestAdded(it)
                }
            }
            composable(Destination.ResultReviewScreen) { ResultReviewScreen(testResult) }
            composable(Destination.ResultsReviewScreen) { ResultsReviewScreen(clickedTest) }
            composable(Destination.TestPassingScreen) {
                TestPassingScreen(clickedTest, viewModel.isUserAModerator) {
                    testResult = it
                }
            }
        }
    }
}