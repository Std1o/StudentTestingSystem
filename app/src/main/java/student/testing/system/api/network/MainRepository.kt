package student.testing.system.api.network

import kotlinx.coroutines.flow.flow
import student.testing.system.models.*
import student.testing.system.models.Question
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(private val remoteData: MainRemoteData) {

    suspend fun auth(email: String, password: String) =
        flow { emit(remoteData.auth("grant_type=&username=$email&password=$password&scope=&client_id=&client_secret=")) }

    suspend fun signUp(email: String, username: String, password: String) =
        flow { emit(remoteData.signUp(SignUpReq(email, username, password))) }

    suspend fun getCourses() = flow { emit(remoteData.getCourses()) }
    suspend fun createCourse(name: String) =
        flow { emit(remoteData.createCourse(CourseCreationReq(name))) }

    suspend fun joinCourse(courseCode: String) = flow { emit(remoteData.joinCourse(courseCode)) }
    suspend fun deleteCourse(courseId: Int, courseOwnerId: Int) =
        flow { emit(remoteData.deleteCourse(courseId, courseOwnerId)) }

    suspend fun getTests(courseId: Int) = flow { emit(remoteData.getTests(courseId)) }
    suspend fun createTest(
        courseId: Int,
        name: String,
        creationTIme: String,
        questions: List<Question>
    ) = flow {
        emit(
            remoteData.createTest(
                TestCreationReq(courseId, name, creationTIme, questions)
            )
        )
    }

    suspend fun deleteTest(testId: Int, courseId: Int, courseOwnerId: Int) =
        flow { emit(remoteData.deleteTest(testId, courseId, courseOwnerId)) }

    suspend fun calculateResult(testId: Int, courseId: Int, request: List<UserQuestion>) =
        flow { emit(remoteData.calculateResult(testId, courseId, request)) }

    suspend fun calculateDemoResult(courseId: Int, testId: Int, courseOwnerId: Int, request: List<UserQuestion>) =
        flow { emit(remoteData.calculateDemoResult(courseId, testId, courseOwnerId, request)) }

    suspend fun getResult(testId: Int, courseId: Int) =
        flow { emit(remoteData.getResult(testId, courseId)) }

    suspend fun getResults(testId: Int, courseId: Int, courseOwnerId: Int, showOnlyMaxResults: Boolean) =
        flow { emit(remoteData.getResults(testId, courseId, courseOwnerId, showOnlyMaxResults)) }

    suspend fun addModerator(courseId: Int, courseOwnerId: Int, moderatorId: Int) =
        flow { emit(remoteData.addModerator(courseId, courseOwnerId, moderatorId)) }

    suspend fun deleteModerator(courseId: Int, courseOwnerId: Int, moderatorId: Int) =
        flow { emit(remoteData.deleteModerator(courseId, courseOwnerId, moderatorId)) }

    suspend fun deleteParticipant(courseId: Int, courseOwnerId: Int, participantId: Int) =
        flow { emit(remoteData.deleteParticipant(courseId, courseOwnerId, participantId)) }
}