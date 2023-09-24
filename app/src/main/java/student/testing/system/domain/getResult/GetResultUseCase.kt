package student.testing.system.domain.getResult

import student.testing.system.domain.states.DataState
import student.testing.system.domain.MainRepository
import student.testing.system.models.TestResult
import javax.inject.Inject

class GetResultUseCase @Inject constructor(private val repository: MainRepository) {

    suspend operator fun invoke(testId: Int, courseId: Int): ResultState<TestResult> {
        val requestResult = repository.getResult(testId, courseId)
        if (requestResult is DataState.Success) {
            return ResultState.Success(requestResult.data)
        } else if (requestResult is DataState.Error && requestResult.code == 404) {
            return ResultState.NoResult
        } else if (requestResult is DataState.Error) {
            return ResultState.Error(requestResult.exception, requestResult.code)
        } else return ResultState.Loading
    }
}