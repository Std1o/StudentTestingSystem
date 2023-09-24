package student.testing.system.domain.usecases

import student.testing.system.R
import student.testing.system.domain.states.DataState
import student.testing.system.domain.MainRepository
import student.testing.system.models.CourseResponse
import javax.inject.Inject

class JoinCourseUseCase @Inject constructor(private val repository: MainRepository) {

    suspend operator fun invoke(courseCode: String): DataState<CourseResponse> {
        return if (courseCode.isEmpty()) {
            DataState.ValidationError(R.string.error_empty_course_code)
        } else {
            repository.joinCourse(courseCode)
        }
    }
}