package student.testing.system.data.repository

import student.testing.system.domain.repository.TestsRepository
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.models.ParticipantsResults
import student.testing.system.models.Test
import student.testing.system.models.TestCreationReq
import student.testing.system.models.TestResult
import student.testing.system.models.TestResultsRequestParams
import student.testing.system.models.UserQuestion

class FakeTestsRepository : TestsRepository {

    override suspend fun getTests(courseId: Int): LoadableData<List<Test>> {
        TODO("Not yet implemented")
    }

    override suspend fun createTest(request: TestCreationReq): OperationState<Test> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTest(testId: Int, courseId: Int): OperationState<Void> {
        TODO("Not yet implemented")
    }

    override suspend fun calculateResult(
        testId: Int,
        courseId: Int,
        request: List<UserQuestion>
    ): OperationState<Void> {
        TODO("Not yet implemented")
    }

    override suspend fun calculateDemoResult(
        courseId: Int,
        testId: Int,
        request: List<UserQuestion>
    ): OperationState<TestResult> {
        TODO("Not yet implemented")
    }

    override suspend fun getResult(testId: Int, courseId: Int): OperationState<TestResult> {
        if (testId == -1 || courseId == -1) {
            return OperationState.Error("Access error", 403)
        }
        val passedTests = listOf(12, 24, 13)
        if (passedTests.contains(testId)) {
            return OperationState.Success(TestResult(emptyList(), 10, 8.9))
        } else {
            return OperationState.Error("Not found", 404)
        }
    }

    override suspend fun getResults(
        testId: Int,
        courseId: Int,
        params: TestResultsRequestParams
    ): LoadableData<ParticipantsResults> {
        TODO("Not yet implemented")
    }
}