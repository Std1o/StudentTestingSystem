package student.testing.system.data.source.impl

import godofappstates.data.dataSource.BaseRemoteDataSource
import student.testing.system.data.api.CourseManagementApi
import student.testing.system.data.api.CoursesApi
import student.testing.system.data.api.TestsApi
import student.testing.system.data.source.interfaces.RemoteDataSource
import student.testing.system.domain.operationTypes.CourseAddingOperations
import student.testing.system.domain.operationTypes.TestsOperations
import student.testing.system.models.CourseCreationReq
import student.testing.system.models.TestCreationReq
import student.testing.system.models.TestResultsRequestParams
import student.testing.system.models.UserQuestion
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val testsApi: TestsApi,
    private val courseManagementApi: CourseManagementApi
) :
    BaseRemoteDataSource(), RemoteDataSource {



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
        executeOperation { courseManagementApi.addModerator(courseId, moderatorId) }

    override suspend fun deleteModerator(courseId: Int, moderatorId: Int) =
        executeOperation { courseManagementApi.deleteModerator(courseId, moderatorId) }

    override suspend fun deleteParticipant(courseId: Int, participantId: Int) =
        executeOperation { courseManagementApi.deleteParticipant(courseId, participantId) }
}