package student.testing.system.data.source.interfaces

import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.domain.models.ParticipantsResults
import student.testing.system.domain.models.Test
import student.testing.system.domain.models.TestCreationReq
import student.testing.system.domain.models.TestResult
import student.testing.system.domain.models.TestResultsRequestParams
import student.testing.system.domain.models.UserQuestion

interface TestsRemoteDataSource {
    suspend fun getTests(courseId: Int): LoadableData<List<Test>>

    suspend fun createTest(request: TestCreationReq): OperationState<Test>

    suspend fun deleteTest(testId: Int, courseId: Int): OperationState<Void>

    suspend fun calculateResult(
        testId: Int,
        courseId: Int,
        request: List<UserQuestion>
    ): OperationState<Void>

    suspend fun calculateDemoResult(
        courseId: Int,
        testId: Int,
        request: List<UserQuestion>
    ): OperationState<TestResult>

    suspend fun getResult(testId: Int, courseId: Int): OperationState<TestResult>

    suspend fun getResults(
        testId: Int,
        courseId: Int,
        params: TestResultsRequestParams
    ): LoadableData<ParticipantsResults>
}