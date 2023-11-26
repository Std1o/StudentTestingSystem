package student.testing.system.presentation.ui.screens

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import student.testing.system.domain.models.CourseResponse
import student.testing.system.presentation.navigation.CustomType
import student.testing.system.presentation.navigation.Destination
import student.testing.system.presentation.navigation.Destination.CourseReviewScreen.COURSE_KEY
import student.testing.system.presentation.navigation.NavHost
import student.testing.system.presentation.navigation.composable
import student.testing.system.presentation.ui.components.NavigationEffects
import student.testing.system.presentation.ui.screens.courses.CoursesScreen
import student.testing.system.presentation.viewmodels.LaunchViewModel

@Composable
fun LaunchScreen() {
    val launchViewModel = hiltViewModel<LaunchViewModel>()
    val navController = rememberNavController()

    NavigationEffects(
        navigationChannel = launchViewModel.navigationChannel,
        navHostController = navController
    )
    Surface {
        NavHost(
            navController = navController,
            startDestination = Destination.LoginScreen
        ) {
            composable(destination = Destination.LoginScreen) { LoginScreen() }
            composable(destination = Destination.SignUpScreen) { SignUpScreen() }
            composable(destination = Destination.CoursesScreen) { CoursesScreen() }
            composable(
                destination = Destination.CourseReviewScreen,
                arguments = listOf(navArgument(COURSE_KEY) {
                    type = CustomType(CourseResponse::class)
                })
            ) { CourseReviewScreen() }
        }
    }
}