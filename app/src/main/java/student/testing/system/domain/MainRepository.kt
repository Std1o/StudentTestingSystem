package student.testing.system.domain

import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.models.*

interface MainRepository {
    suspend fun auth(request: String): OperationState<PrivateUser>

    suspend fun signUp(request: SignUpReq): OperationState<PrivateUser>

    suspend fun getCourses(): LoadableData<List<CourseResponse>>

    suspend fun createCourse(name: String): OperationState<CourseResponse>

    suspend fun joinCourse(courseCode: String): OperationState<CourseResponse>

    suspend fun deleteCourse(courseId: Int): OperationState<Void>

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
    ): LoadableData<TestResult>

    suspend fun getResult(testId: Int, courseId: Int): OperationState<TestResult>

    suspend fun getResults(
        testId: Int,
        courseId: Int,
        params: TestResultsRequestParams
    ): LoadableData<ParticipantsResults>

    suspend fun addModerator(
        courseId: Int,
        moderatorId: Int
    ): OperationState<List<Participant>>

    suspend fun deleteModerator(
        courseId: Int,
        moderatorId: Int
    ): OperationState<List<Participant>>

    suspend fun deleteParticipant(
        courseId: Int,
        participantId: Int
    ): OperationState<List<Participant>>
}