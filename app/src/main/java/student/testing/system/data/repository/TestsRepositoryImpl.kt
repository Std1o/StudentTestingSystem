package student.testing.system.data.repository

import student.testing.system.data.source.interfaces.TestResultsRemoteDataSource
import student.testing.system.data.source.interfaces.TestsRemoteDataSource
import student.testing.system.domain.repository.TestsRepository
import student.testing.system.domain.models.TestCreationReq
import student.testing.system.domain.models.TestResultsRequestParams
import student.testing.system.domain.models.UserQuestion
import javax.inject.Inject

class TestsRepositoryImpl @Inject constructor(
    private val remoteDataSource: TestsRemoteDataSource,
    private val resultsRemoteDataSource: TestResultsRemoteDataSource
) : TestsRepository {

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
    ) = remoteDataSource.calculateDemoResult(courseId, testId, request)


    override suspend fun getResult(testId: Int, courseId: Int) =
        remoteDataSource.getResult(testId, courseId)

    override fun getResults(
        testId: Int,
        courseId: Int,
        params: TestResultsRequestParams
    ) = resultsRemoteDataSource.getResults(testId, courseId, params)
}