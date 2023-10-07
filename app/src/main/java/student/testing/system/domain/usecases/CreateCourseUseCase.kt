package student.testing.system.domain.usecases

import kotlinx.coroutines.flow.flow
import student.testing.system.R
import student.testing.system.domain.MainRepository
import student.testing.system.domain.operationTypes.CourseAddingOperations
import student.testing.system.domain.states.ValidatableOperationState
import student.testing.system.models.CourseResponse
import javax.inject.Inject

class CreateCourseUseCase @Inject constructor(private val repository: MainRepository) {

    suspend operator fun invoke(name: String) = flow {
        if (name.isEmpty()) {
            emit(
                ValidatableOperationState.ValidationError(
                    messageResId = R.string.error_empty_course_name,
                    operationType = CourseAddingOperations.CREATE_COURSE
                )
            )
        } else {
            emit(ValidatableOperationState.SuccessfulValidation(CourseAddingOperations.CREATE_COURSE))
            emit(repository.createCourse(name))
        }
    }
}