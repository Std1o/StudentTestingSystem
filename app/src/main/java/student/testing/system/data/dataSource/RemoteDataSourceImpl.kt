package student.testing.system.data.dataSource

import godofappstates.data.dataSource.BaseRemoteDataSource
import student.testing.system.data.MainService
import student.testing.system.data.api.AuthApi
import student.testing.system.data.api.CoursesApi
import student.testing.system.data.api.TestsApi
import student.testing.system.domain.operationTypes.CourseAddingOperations
import student.testing.system.domain.operationTypes.TestsOperations
import student.testing.system.models.CourseCreationReq
import student.testing.system.models.SignUpReq
import student.testing.system.models.TestCreationReq
import student.testing.system.models.TestResultsRequestParams
import student.testing.system.models.UserQuestion
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val mainService: MainService,
    private val authApi: AuthApi,
    private val coursesApi: CoursesApi,
    private val testsApi: TestsApi
) :
    BaseRemoteDataSource(), RemoteDataSource {
    override suspend fun auth(request: String) = executeOperation { authApi.auth(request) }
    override suspend fun signUp(request: SignUpReq) =
        executeOperation { authApi.signUp(request) }

    override suspend fun getCourses() = loadData { coursesApi.getCourses() }
    override suspend fun createCourse(request: CourseCreationReq) =
        executeOperation(CourseAddingOperations.CREATE_COURSE) { coursesApi.createCourse(request) }

    override suspend fun joinCourse(courseCode: String) =
        executeOperation(CourseAddingOperations.JOIN_COURSE) { coursesApi.joinCourse(courseCode) }

    override suspend fun deleteCourse(courseId: Int) =
        executeOperation { coursesApi.deleteCourse(courseId) }

    override suspend fun getTests(courseId: Int) = loadData { testsApi.getTests(courseId) }
    override suspend fun createTest(request: TestCreationReq) =
        executeOperation(TestsOperations.CREATE_TEST) { testsApi.createTest(request) }

    override suspend fun deleteTest(testId: Int, courseId: Int) =
        executeOperation(TestsOperations.DELETE_TEST) { testsApi.deleteTest(testId, courseId) }

    override suspend fun calculateResult(testId: Int, courseId: Int, request: List<UserQuestion>) =
        executeOperation { testsApi.calculateResult(testId, courseId, request) }

    override suspend fun calculateDemoResult(
        courseId: Int,
        testId: Int,
        request: List<UserQuestion>
    ) = executeOperation { testsApi.calculateDemoResult(courseId, testId, request) }

    override suspend fun getResult(testId: Int, courseId: Int) =
        executeOperation(TestsOperations.GET_RESULT) { testsApi.getResult(testId, courseId) }

    override suspend fun getResults(testId: Int, courseId: Int, params: TestResultsRequestParams) =
        loadData { testsApi.getResults(testId, courseId, params) }

    override suspend fun addModerator(courseId: Int, moderatorId: Int) =
        executeOperation { mainService.addModerator(courseId, moderatorId) }

    override suspend fun deleteModerator(courseId: Int, moderatorId: Int) =
        executeOperation { mainService.deleteModerator(courseId, moderatorId) }

    override suspend fun deleteParticipant(courseId: Int, participantId: Int) =
        executeOperation { mainService.deleteParticipant(courseId, participantId) }
}