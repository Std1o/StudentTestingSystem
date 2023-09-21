package student.testing.system.domain.usecases

import student.testing.system.R
import student.testing.system.domain.DataState
import student.testing.system.domain.MainRepository
import student.testing.system.models.CourseResponse
import javax.inject.Inject

class CreateCourseUseCase @Inject constructor(private val repository: MainRepository) {

    suspend operator fun invoke(name: String): DataState<CourseResponse> {
        return if (name.isEmpty()) {
            DataState.ValidationError(R.string.error_empty_course_name)
        } else {
            repository.createCourse(name)
        }
    }
}