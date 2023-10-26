package student.testing.system.presentation.ui.screens.courses

import androidx.compose.runtime.Composable
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.models.CourseResponse
import student.testing.system.presentation.ui.components.ErrorScreen

@Composable
fun CoursesListStateHandler(
    courses: LoadableData<List<CourseResponse>>,
    onRetry: () -> Unit,
    onHideCoursesList: (Boolean) -> Unit
) {
    when (courses) {
        is LoadableData.Empty204 -> {
            onHideCoursesList(true)
        }

        is LoadableData.Error -> {
            onHideCoursesList(true)
            ErrorScreen(message = courses.exception, onRetry = onRetry)
        }

        is LoadableData.Loading -> {
            onHideCoursesList(false)
        }

        LoadableData.NoState -> {
            onHideCoursesList(false)
        }

        is LoadableData.Success -> {
            // TODO
            /*if (courses.data.isEmpty()) {
                ShowEmptyScreen()
            }*/
            onHideCoursesList(false)
        }
    }
}