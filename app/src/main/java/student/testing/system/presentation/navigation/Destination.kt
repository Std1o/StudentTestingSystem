package student.testing.system.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.gson.Gson
import student.testing.system.R
import student.testing.system.domain.models.Course
import student.testing.system.presentation.navigation.Destination.CourseReviewScreen.COURSE_KEY

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

    // Launch Navigation

    data object LoginScreen : NoArgumentsDestination("login")

    data object SignUpScreen : NoArgumentsDestination("sign_up")

    data object CoursesScreen : NoArgumentsDestination("courses")

    data object CourseReviewScreen :
        Destination("course_review", params = arrayOf("course")) {
        const val COURSE_KEY = "course"

        operator fun invoke(course: Course): String {
            val courseJson = Gson().toJson(course)
            return route.appendParams(COURSE_KEY to courseJson)
        }
    }

    // Course Review

    data object TestsScreen : BottomNavigationDestination(
        route = "tests",
        stringRes = R.string.tests,
        drawableId = R.drawable.ic_tests
    )

    data object ParticipantsScreen : BottomNavigationDestination(
        "participants",
        R.string.participants,
        R.drawable.ic_users
    )

    data object TestCreationHostScreen : Destination("test_creation_host", params = arrayOf(COURSE_KEY)) {
        operator fun invoke(course: Course): String {
            val courseJson = Gson().toJson(course)
            return route.appendParams(COURSE_KEY to courseJson)
        }
    }

    data object ResultReviewScreen : NoArgumentsDestination("result_review")

    data object ResultsReviewScreen : NoArgumentsDestination("results_review")

    data object TestPassingScreen : NoArgumentsDestination("test_passing")

    // Test Creation

    data object TestCreationScreen : NoArgumentsDestination("test_creation")
    data object QuestionCreationScreen : NoArgumentsDestination("question_creation")
}

internal fun String.appendParams(vararg params: Pair<String, Any?>): String {
    val builder = StringBuilder(this)

    params.forEach {
        it.second?.toString()?.let { arg ->
            builder.append("/$arg")
        }
    }

    return builder.toString()
}