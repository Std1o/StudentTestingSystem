package student.testing.system.data.repository

import student.testing.system.data.dataSource.RemoteDataSource
import student.testing.system.domain.repository.MainRepository
import student.testing.system.models.CourseCreationReq
import student.testing.system.models.SignUpReq
import student.testing.system.models.TestCreationReq
import student.testing.system.models.TestResultsRequestParams
import student.testing.system.models.UserQuestion

class MainRepositoryImpl(private val remoteDataSource: RemoteDataSource) : MainRepository {

    override suspend fun auth(request: String) = remoteDataSource.auth(request)

    override suspend fun signUp(request: SignUpReq) = remoteDataSource.signUp(request)

    override suspend fun getCourses() = remoteDataSource.getCourses()
    override suspend fun createCourse(name: String) =
        remoteDataSource.createCourse(CourseCreationReq(name))

    override suspend fun joinCourse(courseCode: String) =
        remoteDataSource.joinCourse(courseCode)

    override suspend fun deleteCourse(courseId: Int) =
        remoteDataSource.deleteCourse(courseId)

    override suspend fun getTests(courseId: Int) = remoteDataSource.getTests(courseId)

    override suspend fun createTest(request: TestCreationReq) =
        remoteDataSource.createTest(request)

    override suspend fun deleteTest(testId: Int, courseId: Int) =
        remoteDataSource.deleteTest(testId, courseId)

    override suspend fun calculateResult(testId: Int, courseId: Int, request: List<UserQuestion>) =
        remoteDataSource.calculateResult(testId, courseId, request)

    override suspend fun calculateDemoResult(
        courseId: Int,
        testId: Int,
        request: List<UserQuestion>
    ) =
        remoteDataSource.calculateDemoResult(courseId, testId, request)


    override suspend fun getResult(testId: Int, courseId: Int) =
        remoteDataSource.getResult(testId, courseId)

    override suspend fun getResults(
        testId: Int,
        courseId: Int,
        params: TestResultsRequestParams
    ) =
        remoteDataSource.getResults(testId, courseId, params)


    override suspend fun addModerator(courseId: Int, moderatorId: Int) =
        remoteDataSource.addModerator(courseId, moderatorId)


    override suspend fun deleteModerator(courseId: Int, moderatorId: Int) =
        remoteDataSource.deleteModerator(courseId, moderatorId)


    override suspend fun deleteParticipant(courseId: Int, participantId: Int) =
        remoteDataSource.deleteParticipant(courseId, participantId)

}