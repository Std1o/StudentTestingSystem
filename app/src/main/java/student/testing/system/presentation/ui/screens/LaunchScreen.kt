package student.testing.system.presentation.ui.screens

import android.app.Activity
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import student.testing.system.models.CourseResponse
import student.testing.system.presentation.navigation.CustomType
import student.testing.system.presentation.navigation.Destination
import student.testing.system.presentation.navigation.Destination.CourseReviewScreen.COURSE_KEY
import student.testing.system.presentation.navigation.NavHost
import student.testing.system.presentation.navigation.NavigationIntent
import student.testing.system.presentation.navigation.composable
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

@Composable
fun NavigationEffects(
    navigationChannel: Channel<NavigationIntent>,
    navHostController: NavHostController
) {
    val activity = (LocalContext.current as? Activity)
    LaunchedEffect(activity, navHostController, navigationChannel) {
        navigationChannel.receiveAsFlow().collect { intent ->
            if (activity?.isFinishing == true) {
                return@collect
            }
            when (intent) {
                is NavigationIntent.NavigateBack -> {
                    if (intent.route != null) {
                        navHostController.popBackStack(intent.route, intent.inclusive)
                    } else {
                        navHostController.popBackStack()
                    }
                }

                is NavigationIntent.NavigateTo -> {
                    navHostController.navigate(intent.route) {
                        launchSingleTop = intent.isSingleTop
                        intent.popUpToRoute?.let { popUpToRoute ->
                            popUpTo(popUpToRoute) { inclusive = intent.inclusive }
                        }
                    }
                }
            }
        }
    }
}
