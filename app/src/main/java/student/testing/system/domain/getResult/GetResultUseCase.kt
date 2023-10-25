package student.testing.system.domain.getResult

import student.testing.system.domain.MainRepository
import student.testing.system.domain.states.OperationState
import student.testing.system.domain.states.ResultState
import student.testing.system.models.TestResult
import javax.inject.Inject

class GetResultUseCase @Inject constructor(private val repository: MainRepository) {

    suspend operator fun invoke(testId: Int, courseId: Int): ResultState<TestResult> {
        val requestResult = repository.getResult(testId, courseId)
        if (requestResult is OperationState.Error && requestResult.code == 404) {
            return ResultState.NoResult
        }
        return requestResult
    }
}