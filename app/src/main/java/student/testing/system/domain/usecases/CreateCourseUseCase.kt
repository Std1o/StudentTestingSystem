package student.testing.system.domain.usecases

import student.testing.system.R
import student.testing.system.domain.MainRepository
import student.testing.system.domain.operationTypes.CourseAddingOperations
import student.testing.system.domain.states.ValidatableOperationState
import student.testing.system.models.CourseResponse
import javax.inject.Inject

class CreateCourseUseCase @Inject constructor(private val repository: MainRepository) {

    suspend operator fun invoke(name: String): ValidatableOperationState<CourseResponse> {
        return if (name.isEmpty()) {
            ValidatableOperationState.ValidationError(
                R.string.error_empty_course_name,
                CourseAddingOperations.CREATE_COURSE
            )
        } else {
            repository.createCourse(name)
        }
    }
}