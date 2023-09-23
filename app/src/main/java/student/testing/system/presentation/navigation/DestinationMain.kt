package student.testing.system.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import student.testing.system.R

sealed class DestinationMain(
    protected val route: String,
    @StringRes val stringRes: Int,
    @DrawableRes val drawableId: Int,
    vararg params: String
) {
    val fullRoute: String = if (params.isEmpty()) route else {
        val builder = StringBuilder(route)
        params.forEach { builder.append("/{${it}}") }
        builder.toString()
    }

    sealed class NoArgumentsDestination(
        route: String,
        @StringRes stringRes: Int,
        @DrawableRes drawableId: Int
    ) :
        DestinationMain(route, stringRes, drawableId) {
        operator fun invoke(): String = route
    }

    object TestsScreen :
        NoArgumentsDestination("tests", R.string.tests, R.drawable.ic_tests)

    object ParticipantsScreen :
        NoArgumentsDestination("participants", R.string.participants, R.drawable.ic_users)
}