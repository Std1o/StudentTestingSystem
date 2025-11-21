package student.testing.system.domain.repository

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import student.testing.system.domain.models.ParticipantsResults
import student.testing.system.domain.models.Test
import student.testing.system.domain.models.TestCreationReq
import student.testing.system.domain.models.TestResult
import student.testing.system.domain.models.TestResultsRequestParams
import student.testing.system.domain.models.UserQuestion
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.domain.webSockets.WebsocketEvent

interface TestsRepository {
    suspend fun getTests(courseId: Int): LoadableData<List<Test>>

    suspend fun getAIQuestion(request: String): OperationState<String>

    suspend fun createTest(request: TestCreationReq): OperationState<Test>

    suspend fun deleteTest(testId: Int, courseId: Int): OperationState<Void>

    suspend fun calculateResult(
        testId: Int, courseId: Int, request: List<UserQuestion>
    ): OperationState<Void>

    suspend fun calculateDemoResult(
        courseId: Int, testId: Int, request: List<UserQuestion>
    ): OperationState<TestResult>

    suspend fun getResult(testId: Int, courseId: Int): OperationState<TestResult>

    suspend fun getParticipantResult(
        testId: Int,
        courseId: Int,
        participantId: Int
    ): OperationState<TestResult>

    fun getResults(
        testId: Int, courseId: Int, params: TestResultsRequestParams
    ): Flow<WebsocketEvent<ParticipantsResults>>

    fun closeResultsConnection(): Job
}