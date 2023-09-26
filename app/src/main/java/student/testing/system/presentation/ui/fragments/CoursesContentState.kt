package student.testing.system.presentation.ui.fragments

import student.testing.system.domain.states.LoadableData
import student.testing.system.domain.states.RequestState
import student.testing.system.models.CourseResponse

data class CoursesContentState(val courses: LoadableData<List<CourseResponse>> = RequestState.NoState)