package student.testing.system.presentation.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavHostMain(
    navController: NavHostController,
    startDestination: DestinationMain,
    modifier: Modifier = Modifier,
    route: String? = null,
    builder: NavGraphBuilder.() -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.fullRoute,
        modifier = modifier,
        route = route,
        builder = builder
    )
}

private val enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?) =
    {
        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
    }

private val exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?) =
    {
        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
    }

fun NavGraphBuilder.composable(
    destination: DestinationMain,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = destination.fullRoute,
        arguments = arguments,
        deepLinks = deepLinks,
        content = content,
        enterTransition = enterTransition,
        exitTransition = exitTransition
    )
}