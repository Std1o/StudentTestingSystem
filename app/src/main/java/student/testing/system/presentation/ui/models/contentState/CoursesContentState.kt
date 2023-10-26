package student.testing.system.presentation.ui.models.contentState

import stdio.godofappstates.annotations.ContentState
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.models.CourseResponse

@ContentState
data class CoursesContentState(
    val courses: LoadableData<List<CourseResponse>> = LoadableData.NoState,
    // TODO сделать CoursesState и перенести это туда как sealed interface, затем закинуть CoursesState в UIStateWrapper
    val isLoggedOut: Boolean = false
)