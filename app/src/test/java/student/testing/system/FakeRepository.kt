package student.testing.system

import io.mockk.mockk
import student.testing.system.domain.MainRepository
import student.testing.system.domain.states.LoadableData
import student.testing.system.domain.states.OperationState
import student.testing.system.models.CourseResponse
import student.testing.system.models.Participant
import student.testing.system.models.ParticipantsResults
import student.testing.system.models.PrivateUser
import student.testing.system.models.SignUpReq
import student.testing.system.models.Test
import student.testing.system.models.TestCreationReq
import student.testing.system.models.TestResult
import student.testing.system.models.TestResultsRequestParams
import student.testing.system.models.UserQuestion

class FakeRepository : MainRepository {

    data class MockedUser(val email: String, val password: String)

    override suspend fun auth(request: String): OperationState<PrivateUser> {
        val existingUsers = listOf(MockedUser("test@mail.ru", "pass"))
        for (user in existingUsers) {
            if (request.contains(user.email) && request.contains("password=${user.password}")) {
                return OperationState.Success(PrivateUser(1, "Ivan", user.email, "some_token"))
            }
        }
        return OperationState.Error("Incorrect username or password")
    }

    override suspend fun signUp(request: SignUpReq): OperationState<PrivateUser> {
        TODO("Not yet implemented")
    }

    override suspend fun getCourses(): LoadableData<List<CourseResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun createCourse(name: String): OperationState<CourseResponse> {
        TODO("Not yet implemented")
    }


    override suspend fun joinCourse(courseCode: String): OperationState<CourseResponse> {
        val courses = listOf("5TYHKW", "KASTXJ", "XHYX6U")
        if (courseCode in courses) {
            val course = mockk<CourseResponse>(relaxed = true)
            return OperationState.Success(course)
        } else {
            return OperationState.Error("Not found", 404)
        }
    }

    override suspend fun deleteCourse(courseId: Int): OperationState<Void> {
        TODO("Not yet implemented")
    }

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
    ): LoadableData<TestResult> {
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

    override suspend fun addModerator(
        courseId: Int,
        moderatorId: Int
    ): OperationState<List<Participant>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteModerator(
        courseId: Int,
        moderatorId: Int
    ): OperationState<List<Participant>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteParticipant(
        courseId: Int,
        participantId: Int
    ): OperationState<List<Participant>> {
        TODO("Not yet implemented")
    }

}