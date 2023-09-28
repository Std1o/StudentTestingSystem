package student.testing.system.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import student.testing.system.R

sealed class Destination(
    protected val route: String,
    @StringRes val stringRes: Int = 0,
    @DrawableRes val drawableId: Int = 0,
    vararg params: String
) {
    val fullRoute: String = if (params.isEmpty()) route else {
        val builder = StringBuilder(route)
        params.forEach { builder.append("/{${it}}") }
        builder.toString()
    }

    sealed class NoArgumentsDestination(route: String) : Destination(route) {
        operator fun invoke(): String = route
    }

    sealed class BottomNavigationDestination(
        route: String,
        @StringRes stringRes: Int,
        @DrawableRes drawableId: Int
    ) :
        Destination(route, stringRes, drawableId) {
        operator fun invoke(): String = route
    }

    object LoginScreen : NoArgumentsDestination("login")

    object SignUpScreen : NoArgumentsDestination("sign_up")

    object CoursesScreen : NoArgumentsDestination("courses")

    // Course Review

    object TestsScreen : BottomNavigationDestination("tests", R.string.tests, R.drawable.ic_tests)

    object ParticipantsScreen : BottomNavigationDestination(
        "participants",
        R.string.participants,
        R.drawable.ic_users
    )
}