package student.testing.system.domain.usecases

import student.testing.system.domain.repository.TestsRepository
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.domain.states.operationStates.ResultState
import student.testing.system.models.TestResult
import javax.inject.Inject

class GetResultUseCase @Inject constructor(private val repository: TestsRepository) {

    suspend operator fun invoke(testId: Int, courseId: Int): ResultState<TestResult> {
        val requestResult = repository.getResult(testId, courseId)
        if (requestResult is OperationState.Error && requestResult.code == 404) {
            return ResultState.NoResult
        }
        return requestResult
    }
}