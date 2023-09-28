package student.testing.system.presentation.ui.models

import student.testing.system.domain.states.LoadableData
import student.testing.system.domain.states.RequestState
import student.testing.system.models.CourseResponse

data class CoursesContentState(
    val courses: LoadableData<List<CourseResponse>> = RequestState.NoState,
    val isLoggedOut: Boolean = false
)