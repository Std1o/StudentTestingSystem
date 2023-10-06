package student.testing.system.domain.usecases

import student.testing.system.R
import student.testing.system.domain.MainRepository
import student.testing.system.domain.operationTypes.CourseAddingOperations
import student.testing.system.domain.states.ValidatableOperationState
import student.testing.system.models.CourseResponse
import javax.inject.Inject

class JoinCourseUseCase @Inject constructor(private val repository: MainRepository) {

    suspend operator fun invoke(courseCode: String): ValidatableOperationState<CourseResponse> {
        return if (courseCode.isEmpty()) {
            ValidatableOperationState.ValidationError(
                R.string.error_empty_course_code,
                CourseAddingOperations.JOIN_COURSE
            )
        } else {
            repository.joinCourse(courseCode)
        }
    }
}