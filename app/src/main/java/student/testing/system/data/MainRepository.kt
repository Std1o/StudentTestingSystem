package student.testing.system.data

import kotlinx.coroutines.flow.flow
import student.testing.system.models.*
import student.testing.system.models.Question
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val remoteData: MainRemoteData
) : BaseRepository() {

    suspend fun auth(authRequest: String) =
        flow { emit(apiCall { remoteData.auth(authRequest) }) }

    suspend fun signUp(email: String, username: String, password: String) =
        flow { emit(apiCall { remoteData.signUp(SignUpReq(email, username, password)) }) }

    suspend fun getCourses() = flow { emit(apiCall { remoteData.getCourses() }) }
    suspend fun createCourse(name: String) =
        flow { emit(apiCall { remoteData.createCourse(CourseCreationReq(name)) }) }

    suspend fun joinCourse(courseCode: String) =
        flow { emit(apiCall { remoteData.joinCourse(courseCode) }) }

    suspend fun deleteCourse(courseId: Int, courseOwnerId: Int) =
        flow { emit(apiCall { remoteData.deleteCourse(courseId, courseOwnerId) }) }

    suspend fun getTests(courseId: Int) = flow { emit(apiCall { remoteData.getTests(courseId) }) }
    suspend fun createTest(
        courseId: Int,
        name: String,
        creationTIme: String,
        questions: List<Question>
    ) = flow {
        emit(
            apiCall {
                remoteData.createTest(
                    TestCreationReq(courseId, name, creationTIme, questions)
                )
            }
        )
    }

    suspend fun deleteTest(testId: Int, courseId: Int, courseOwnerId: Int) =
        flow { emit(apiCall { remoteData.deleteTest(testId, courseId, courseOwnerId) }) }

    suspend fun calculateResult(testId: Int, courseId: Int, request: List<UserQuestion>) =
        flow { emit(apiCall { remoteData.calculateResult(testId, courseId, request) }) }

    suspend fun calculateDemoResult(
        courseId: Int,
        testId: Int,
        courseOwnerId: Int,
        request: List<UserQuestion>
    ) =
        flow {
            emit(apiCall {
                remoteData.calculateDemoResult(
                    courseId,
                    testId,
                    courseOwnerId,
                    request
                )
            })
        }

    suspend fun getResult(testId: Int, courseId: Int) =
        flow { emit(apiCall { remoteData.getResult(testId, courseId) }) }

    suspend fun getResults(
        testId: Int,
        courseId: Int,
        courseOwnerId: Int,
        showOnlyMaxResults: Boolean
    ) =
        flow {
            emit(apiCall {
                remoteData.getResults(
                    testId,
                    courseId,
                    courseOwnerId,
                    showOnlyMaxResults
                )
            })
        }

    suspend fun addModerator(courseId: Int, courseOwnerId: Int, moderatorId: Int) =
        flow { emit(apiCall { remoteData.addModerator(courseId, courseOwnerId, moderatorId) }) }

    suspend fun deleteModerator(courseId: Int, courseOwnerId: Int, moderatorId: Int) =
        flow { emit(apiCall { remoteData.deleteModerator(courseId, courseOwnerId, moderatorId) }) }

    suspend fun deleteParticipant(courseId: Int, courseOwnerId: Int, participantId: Int) =
        flow {
            emit(apiCall {
                remoteData.deleteParticipant(
                    courseId,
                    courseOwnerId,
                    participantId
                )
            })
        }
}