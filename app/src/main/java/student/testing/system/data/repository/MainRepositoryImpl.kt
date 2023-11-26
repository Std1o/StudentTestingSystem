package student.testing.system.data.repository

import student.testing.system.data.source.interfaces.CourseManagementRemoteDataSource
import student.testing.system.data.source.interfaces.TestsRemoteDataSource
import student.testing.system.domain.repository.MainRepository
import student.testing.system.models.TestCreationReq
import student.testing.system.models.TestResultsRequestParams
import student.testing.system.models.UserQuestion
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val testsRemoteDataSource: TestsRemoteDataSource,
    private val courseManagementRemoteDataSource: CourseManagementRemoteDataSource,
) : MainRepository {

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
        courseManagementRemoteDataSource.addModerator(courseId, moderatorId)


    override suspend fun deleteModerator(courseId: Int, moderatorId: Int) =
        courseManagementRemoteDataSource.deleteModerator(courseId, moderatorId)


    override suspend fun deleteParticipant(courseId: Int, participantId: Int) =
        courseManagementRemoteDataSource.deleteParticipant(courseId, participantId)

}