package student.testing.system.presentation.ui.screens.testCreation

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import student.testing.system.common.NullSharedViewModelException
import student.testing.system.common.viewModelScopedTo
import student.testing.system.domain.states.TestCreationState
import student.testing.system.models.Test
import student.testing.system.presentation.navigation.Destination
import student.testing.system.presentation.navigation.NavHost
import student.testing.system.presentation.navigation.composable
import student.testing.system.presentation.ui.screens.NavigationEffects
import student.testing.system.presentation.ui.screens.QuestionCreationScreen
import student.testing.system.presentation.viewmodels.CourseSharedViewModel
import student.testing.system.presentation.viewmodels.TestCreationHostViewModel
import student.testing.system.presentation.viewmodels.TestCreationViewModel

@Composable
fun TestCreationHostScreen(parentViewModel: CourseSharedViewModel, onTestCreated: (Test) -> Unit) {
    val viewModel = hiltViewModel<TestCreationHostViewModel>()
    val course by parentViewModel.courseFlow.collectAsState()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val sharedViewModel = navBackStackEntry
        ?.viewModelScopedTo<TestCreationViewModel>(
            navController,
            Destination.TestCreationScreen.fullRoute
        )
    sharedViewModel?.setCourse(course)
    val testStateWrapper = sharedViewModel?.testStateWrapper?.collectAsState()
    testStateWrapper?.value?.uiState?.let {
        if (it is TestCreationState.ReadyForPublication) {
            onTestCreated(it.test)
            viewModel.navigateBack()
        }
    }

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