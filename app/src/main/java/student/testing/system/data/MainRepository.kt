package student.testing.system.data

import kotlinx.coroutines.flow.flow
import student.testing.system.models.*
import student.testing.system.models.Question
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : BaseRepository() {

    suspend fun auth(authRequest: String) =
        flow { emit(apiCall { remoteDataSource.auth(authRequest) }) }

    suspend fun signUp(email: String, username: String, password: String) =
        flow { emit(apiCall { remoteDataSource.signUp(SignUpReq(email, username, password)) }) }

    suspend fun getCourses() = flow { emit(apiCall { remoteDataSource.getCourses() }) }
    suspend fun createCourse(name: String) =
        flow { emit(apiCall { remoteDataSource.createCourse(CourseCreationReq(name)) }) }

    suspend fun joinCourse(courseCode: String) =
        flow { emit(apiCall { remoteDataSource.joinCourse(courseCode) }) }

    suspend fun deleteCourse(courseId: Int, courseOwnerId: Int) =
        flow { emit(apiCall { remoteDataSource.deleteCourse(courseId, courseOwnerId) }) }

    suspend fun getTests(courseId: Int) = flow { emit(apiCall { remoteDataSource.getTests(courseId) }) }
    suspend fun createTest(
        courseId: Int,
        name: String,
        creationTIme: String,
        questions: List<Question>
    ) = flow {
        emit(
            apiCall {
                remoteDataSource.createTest(
                    TestCreationReq(courseId, name, creationTIme, questions)
                )
            }
        )
    }

    suspend fun deleteTest(testId: Int, courseId: Int, courseOwnerId: Int) =
        flow { emit(apiCall { remoteDataSource.deleteTest(testId, courseId, courseOwnerId) }) }

    suspend fun calculateResult(testId: Int, courseId: Int, request: List<UserQuestion>) =
        flow { emit(apiCall { remoteDataSource.calculateResult(testId, courseId, request) }) }

    suspend fun calculateDemoResult(
        courseId: Int,
        testId: Int,
        courseOwnerId: Int,
        request: List<UserQuestion>
    ) =
        flow {
            emit(apiCall {
                remoteDataSource.calculateDemoResult(
                    courseId,
                    testId,
                    courseOwnerId,
                    request
                )
            })
        }

    suspend fun getResult(testId: Int, courseId: Int) =
        flow { emit(apiCall { remoteDataSource.getResult(testId, courseId) }) }

    suspend fun getResults(
        testId: Int,
        courseId: Int,
        courseOwnerId: Int,
        showOnlyMaxResults: Boolean
    ) =
        flow {
            emit(apiCall {
                remoteDataSource.getResults(
                    testId,
                    courseId,
                    courseOwnerId,
                    showOnlyMaxResults
                )
            })
        }

    suspend fun addModerator(courseId: Int, courseOwnerId: Int, moderatorId: Int) =
        flow { emit(apiCall { remoteDataSource.addModerator(courseId, courseOwnerId, moderatorId) }) }

    suspend fun deleteModerator(courseId: Int, courseOwnerId: Int, moderatorId: Int) =
        flow { emit(apiCall { remoteDataSource.deleteModerator(courseId, courseOwnerId, moderatorId) }) }

    suspend fun deleteParticipant(courseId: Int, courseOwnerId: Int, participantId: Int) =
        flow {
            emit(apiCall {
                remoteDataSource.deleteParticipant(
                    courseId,
                    courseOwnerId,
                    participantId
                )
            })
        }
}