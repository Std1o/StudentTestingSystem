package student.testing.system.domain.usecases

import kotlinx.coroutines.flow.flow
import student.testing.system.R
import student.testing.system.domain.operationTypes.CourseAddingOperations
import student.testing.system.domain.repository.CoursesRepository
import student.testing.system.domain.states.operationStates.ValidatableOperationState
import javax.inject.Inject

class JoinCourseUseCase @Inject constructor(private val repository: CoursesRepository) {

    suspend operator fun invoke(courseCode: String) = flow {
        if (courseCode.isEmpty()) {
            emit(
                ValidatableOperationState.ValidationError(
                    R.string.error_empty_course_code,
                    CourseAddingOperations.JOIN_COURSE
                )
            )
        } else {
            emit(ValidatableOperationState.SuccessfulValidation(CourseAddingOperations.JOIN_COURSE))
            emit(repository.joinCourse(courseCode))
        }
    }
}