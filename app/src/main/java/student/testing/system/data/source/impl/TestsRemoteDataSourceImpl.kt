package student.testing.system.data.source.impl

import lilith.data.dataSource.BaseRemoteDataSource
import student.testing.system.data.api.TestsApi
import student.testing.system.data.source.interfaces.TestsRemoteDataSource
import student.testing.system.domain.operationTypes.TestsOperations
import student.testing.system.domain.models.TestCreationReq
import student.testing.system.domain.models.TestResultsRequestParams
import student.testing.system.domain.models.UserQuestion
import javax.inject.Inject

class TestsRemoteDataSourceImpl @Inject constructor(private val testsApi: TestsApi) :
    BaseRemoteDataSource(), TestsRemoteDataSource {

    override suspend fun getTests(courseId: Int) = loadData { testsApi.getTests(courseId) }
    override suspend fun createTest(request: TestCreationReq) =
        executeOperation(TestsOperations.CREATE_TEST) { testsApi.createTest(request) }

    override suspend fun deleteTest(testId: Int, courseId: Int) =
        executeOperation(TestsOperations.DELETE_TEST) { testsApi.deleteTest(testId, courseId) }

    override suspend fun calculateResult(testId: Int, courseId: Int, request: List<UserQuestion>) =
        executeOperation { testsApi.calculateResult(testId, courseId, request) }

    override suspend fun calculateDemoResult(
        courseId: Int, testId: Int, request: List<UserQuestion>
    ) = executeOperation { testsApi.calculateDemoResult(courseId, testId, request) }

    override suspend fun getResult(testId: Int, courseId: Int) =
        executeOperation(TestsOperations.GET_RESULT) { testsApi.getResult(testId, courseId) }

    override suspend fun getResults(testId: Int, courseId: Int, params: TestResultsRequestParams) =
        loadData { testsApi.getResults(testId, courseId, params) }
}