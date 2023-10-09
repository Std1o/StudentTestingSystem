package student.testing.system.domain

import student.testing.system.domain.states.LoadableData
import student.testing.system.domain.states.RequestState
import student.testing.system.models.*

interface MainRepository {
    suspend fun auth(request: String): RequestState<PrivateUser>

    suspend fun signUp(request: SignUpReq): RequestState<PrivateUser>

    suspend fun getCourses(): LoadableData<List<CourseResponse>>

    suspend fun createCourse(name: String): RequestState<CourseResponse>

    suspend fun joinCourse(courseCode: String): RequestState<CourseResponse>

    suspend fun deleteCourse(courseId: Int): RequestState<Void>

    suspend fun getTests(courseId: Int): RequestState<List<Test>>

    suspend fun createTest(request: TestCreationReq): RequestState<Test>

    suspend fun deleteTest(testId: Int, courseId: Int): RequestState<Void>

    suspend fun calculateResult(
        testId: Int,
        courseId: Int,
        request: List<UserQuestion>
    ): RequestState<Void>

    suspend fun calculateDemoResult(
        courseId: Int,
        testId: Int,
        request: List<UserQuestion>
    ): RequestState<TestResult>

    suspend fun getResult(testId: Int, courseId: Int): RequestState<TestResult>

    suspend fun getResults(
        testId: Int,
        courseId: Int,
        params: TestResultsRequestParams
    ): RequestState<ParticipantsResults>

    suspend fun addModerator(
        courseId: Int,
        moderatorId: Int
    ): RequestState<List<Participant>>

    suspend fun deleteModerator(
        courseId: Int,
        moderatorId: Int
    ): RequestState<List<Participant>>

    suspend fun deleteParticipant(
        courseId: Int,
        participantId: Int
    ): RequestState<List<Participant>>
}