package student.testing.system.domain.getResult

import student.testing.system.annotations.NotScreenState
import student.testing.system.domain.states.RequestState
import student.testing.system.domain.MainRepository
import student.testing.system.models.TestResult
import javax.inject.Inject

class GetResultUseCase @Inject constructor(private val repository: MainRepository) {

    @OptIn(NotScreenState::class)
    suspend operator fun invoke(testId: Int, courseId: Int): ResultState<TestResult> {
        val requestResult = repository.getResult(testId, courseId)
        if (requestResult is RequestState.Success) {
            return ResultState.Success(requestResult.data)
        } else if (requestResult is RequestState.Error && requestResult.code == 404) {
            return ResultState.NoResult
        } else if (requestResult is RequestState.Error) {
            return ResultState.Error(requestResult.exception, requestResult.code)
        } else return ResultState.Loading
    }
}