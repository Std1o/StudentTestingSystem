package student.testing.system.presentation.ui.screens

import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import student.testing.system.common.NullSharedViewModelException
import student.testing.system.common.viewModelScopedTo
import student.testing.system.presentation.navigation.Destination
import student.testing.system.presentation.navigation.NavHost
import student.testing.system.presentation.navigation.composable
import student.testing.system.presentation.ui.components.CenteredColumn
import student.testing.system.presentation.viewmodels.TestCreationHostViewModel
import student.testing.system.presentation.viewmodels.TestCreationViewModel

@Composable
fun TestCreationHostScreen() {
    val viewModel = hiltViewModel<TestCreationHostViewModel>()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val sharedViewModel = navBackStackEntry
        ?.viewModelScopedTo<TestCreationViewModel>(
            navController,
            Destination.TestCreationScreen.fullRoute
        )
    sharedViewModel?.setCourse(viewModel.course)

    NavigationEffects(
        navigationChannel = viewModel.navigationChannel,
        navHostController = navController
    )
    Surface {
        NavHost(
            navController = navController,
            startDestination = Destination.TestCreationScreen
        ) {
            composable(Destination.TestCreationScreen) {
                TestCreationScreen(sharedViewModel ?: throw NullSharedViewModelException())
            }
            composable(Destination.QuestionCreationScreen) {
                QuestionCreationScreen(sharedViewModel ?: throw NullSharedViewModelException())
            }
        }
    }
}