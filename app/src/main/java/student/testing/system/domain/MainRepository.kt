package student.testing.system.domain

import student.testing.system.domain.states.DataState
import student.testing.system.models.*

interface MainRepository {
    suspend fun auth(request: String): DataState<PrivateUser>

    suspend fun signUp(request: SignUpReq): DataState<PrivateUser>

    suspend fun getCourses(): DataState<List<CourseResponse>>

    suspend fun createCourse(name: String): DataState<CourseResponse>

    suspend fun joinCourse(courseCode: String): DataState<CourseResponse>

    suspend fun deleteCourse(courseId: Int): DataState<Void>

    suspend fun getTests(courseId: Int): DataState<List<Test>>

    suspend fun createTest(request: TestCreationReq): DataState<Test>

    suspend fun deleteTest(testId: Int, courseId: Int): DataState<Void>

    suspend fun calculateResult(
        testId: Int,
        courseId: Int,
        request: List<UserQuestion>
    ): DataState<Void>

    suspend fun calculateDemoResult(
        courseId: Int,
        testId: Int,
        request: List<UserQuestion>
    ): DataState<TestResult>

    suspend fun getResult(testId: Int, courseId: Int): DataState<TestResult>

    suspend fun getResults(
        testId: Int,
        courseId: Int,
        params: TestResultsRequestParams
    ): DataState<ParticipantsResults>

    suspend fun addModerator(
        courseId: Int,
        moderatorId: Int
    ): DataState<List<Participant>>

    suspend fun deleteModerator(
        courseId: Int,
        moderatorId: Int
    ): DataState<List<Participant>>

    suspend fun deleteParticipant(
        courseId: Int,
        participantId: Int
    ): DataState<List<Participant>>
}