package student.testing.system.data.dataSource

import student.testing.system.data.MainService
import student.testing.system.domain.operationTypes.TestsOperations
import student.testing.system.models.CourseCreationReq
import student.testing.system.models.SignUpReq
import student.testing.system.models.TestCreationReq
import student.testing.system.models.TestResultsRequestParams
import student.testing.system.models.UserQuestion
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val mainService: MainService) :
    BaseRemoteDataSource(), RemoteDataSource {
    override suspend fun auth(request: String) = apiCall { mainService.auth(request) }
    override suspend fun signUp(request: SignUpReq) = apiCall { mainService.signUp(request) }
    override suspend fun getCourses() = apiCall { mainService.getCourses() }
    override suspend fun createCourse(request: CourseCreationReq) =
        apiCall { mainService.createCourse(request) }

    override suspend fun joinCourse(courseCode: String) =
        apiCall { mainService.joinCourse(courseCode) }

    override suspend fun deleteCourse(courseId: Int) =
        apiCall { mainService.deleteCourse(courseId) }

    override suspend fun getTests(courseId: Int) = apiCall { mainService.getTests(courseId) }
    override suspend fun createTest(request: TestCreationReq) =
        apiCall(TestsOperations.CREATE_TEST) { mainService.createTest(request) }

    override suspend fun deleteTest(testId: Int, courseId: Int) =
        apiCall(TestsOperations.DELETE_TEST) { mainService.deleteTest(testId, courseId) }

    override suspend fun calculateResult(testId: Int, courseId: Int, request: List<UserQuestion>) =
        apiCall { mainService.calculateResult(testId, courseId, request) }

    override suspend fun calculateDemoResult(
        courseId: Int,
        testId: Int,
        request: List<UserQuestion>
    ) = apiCall { mainService.calculateDemoResult(courseId, testId, request) }

    override suspend fun getResult(testId: Int, courseId: Int) =
        apiCall(TestsOperations.GET_RESULT) { mainService.getResult(testId, courseId) }

    override suspend fun getResults(testId: Int, courseId: Int, params: TestResultsRequestParams) =
        apiCall { mainService.getResults(testId, courseId, params) }

    override suspend fun addModerator(courseId: Int, moderatorId: Int) =
        apiCall { mainService.addModerator(courseId, moderatorId) }

    override suspend fun deleteModerator(courseId: Int, moderatorId: Int) =
        apiCall { mainService.deleteModerator(courseId, moderatorId) }

    override suspend fun deleteParticipant(courseId: Int, participantId: Int) =
        apiCall { mainService.deleteParticipant(courseId, participantId) }
}