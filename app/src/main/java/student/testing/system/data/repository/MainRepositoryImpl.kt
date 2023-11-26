package student.testing.system.data.repository

import student.testing.system.data.source.interfaces.AuthRemoteDataSource
import student.testing.system.data.source.interfaces.CoursesRemoteDataSource
import student.testing.system.data.source.interfaces.RemoteDataSource
import student.testing.system.data.source.interfaces.TestsRemoteDataSource
import student.testing.system.domain.repository.MainRepository
import student.testing.system.models.CourseCreationReq
import student.testing.system.models.SignUpReq
import student.testing.system.models.TestCreationReq
import student.testing.system.models.TestResultsRequestParams
import student.testing.system.models.UserQuestion

class MainRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val coursesRemoteDataSource: CoursesRemoteDataSource,
    private val testsRemoteDataSource: TestsRemoteDataSource
) : MainRepository {

    override suspend fun auth(request: String) = authRemoteDataSource.auth(request)

    override suspend fun signUp(request: SignUpReq) = authRemoteDataSource.signUp(request)

    override suspend fun getCourses() = coursesRemoteDataSource.getCourses()
    override suspend fun createCourse(name: String) =
        coursesRemoteDataSource.createCourse(CourseCreationReq(name))

    override suspend fun joinCourse(courseCode: String) =
        coursesRemoteDataSource.joinCourse(courseCode)

    override suspend fun deleteCourse(courseId: Int) =
        coursesRemoteDataSource.deleteCourse(courseId)

    override suspend fun getTests(courseId: Int) = testsRemoteDataSource.getTests(courseId)

    override suspend fun createTest(request: TestCreationReq) =
        testsRemoteDataSource.createTest(request)

    override suspend fun deleteTest(testId: Int, courseId: Int) =
        testsRemoteDataSource.deleteTest(testId, courseId)

    override suspend fun calculateResult(testId: Int, courseId: Int, request: List<UserQuestion>) =
        testsRemoteDataSource.calculateResult(testId, courseId, request)

    override suspend fun calculateDemoResult(
        courseId: Int,
        testId: Int,
        request: List<UserQuestion>
    ) =
        testsRemoteDataSource.calculateDemoResult(courseId, testId, request)


    override suspend fun getResult(testId: Int, courseId: Int) =
        testsRemoteDataSource.getResult(testId, courseId)

    override suspend fun getResults(
        testId: Int,
        courseId: Int,
        params: TestResultsRequestParams
    ) =
        testsRemoteDataSource.getResults(testId, courseId, params)


    override suspend fun addModerator(courseId: Int, moderatorId: Int) =
        remoteDataSource.addModerator(courseId, moderatorId)


    override suspend fun deleteModerator(courseId: Int, moderatorId: Int) =
        remoteDataSource.deleteModerator(courseId, moderatorId)


    override suspend fun deleteParticipant(courseId: Int, participantId: Int) =
        remoteDataSource.deleteParticipant(courseId, participantId)

}