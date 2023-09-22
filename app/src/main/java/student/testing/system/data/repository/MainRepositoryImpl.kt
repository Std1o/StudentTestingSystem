package student.testing.system.data.repository

import student.testing.system.data.dataSource.RemoteDataSource
import student.testing.system.domain.MainRepository
import student.testing.system.models.CourseCreationReq
import student.testing.system.models.SignUpReq
import student.testing.system.models.TestCreationReq
import student.testing.system.models.TestResultsRequestParams
import student.testing.system.models.UserQuestion
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : BaseRepository(), MainRepository {

    override suspend fun auth(request: String) = apiCall { remoteDataSource.auth(request) }

    override suspend fun signUp(request: SignUpReq) = apiCall { remoteDataSource.signUp(request) }

    override suspend fun getCourses() = apiCall { remoteDataSource.getCourses() }
    override suspend fun createCourse(name: String) =
        apiCall { remoteDataSource.createCourse(CourseCreationReq(name)) }

    override suspend fun joinCourse(courseCode: String) =
        apiCall { remoteDataSource.joinCourse(courseCode) }

    override suspend fun deleteCourse(courseId: Int) =
        apiCall { remoteDataSource.deleteCourse(courseId) }

    override suspend fun getTests(courseId: Int) = apiCall { remoteDataSource.getTests(courseId) }

    override suspend fun createTest(request: TestCreationReq) =
        apiCall { remoteDataSource.createTest(request) }

    override suspend fun deleteTest(testId: Int, courseId: Int) =
        apiCall { remoteDataSource.deleteTest(testId, courseId) }

    override suspend fun calculateResult(testId: Int, courseId: Int, request: List<UserQuestion>) =
        apiCall { remoteDataSource.calculateResult(testId, courseId, request) }

    override suspend fun calculateDemoResult(
        courseId: Int,
        testId: Int,
        request: List<UserQuestion>
    ) = apiCall {
        remoteDataSource.calculateDemoResult(courseId, testId, request)
    }

    override suspend fun getResult(testId: Int, courseId: Int) =
        apiCall { remoteDataSource.getResult(testId, courseId) }

    override suspend fun getResults(
        testId: Int,
        courseId: Int,
        params: TestResultsRequestParams
    ) = apiCall {
        remoteDataSource.getResults(testId, courseId, params)
    }

    override suspend fun addModerator(courseId: Int, moderatorId: Int) = apiCall {
        remoteDataSource.addModerator(courseId, moderatorId)
    }

    override suspend fun deleteModerator(courseId: Int, moderatorId: Int) = apiCall {
        remoteDataSource.deleteModerator(courseId, moderatorId)
    }

    override suspend fun deleteParticipant(courseId: Int, participantId: Int) = apiCall {
        remoteDataSource.deleteParticipant(courseId, participantId)
    }
}