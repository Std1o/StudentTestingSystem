package student.testing.system.data

import kotlinx.coroutines.flow.flow
import student.testing.system.domain.MainRepository
import student.testing.system.models.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : BaseRepository(), MainRepository {

    override suspend fun auth(request: String) =
        flow { emit(apiCall { remoteDataSource.auth(request) }) }

    override suspend fun signUp(request: SignUpReq) =
        flow { emit(apiCall { remoteDataSource.signUp(request) }) }

    override suspend fun getCourses() = flow { emit(apiCall { remoteDataSource.getCourses() }) }
    override suspend fun createCourse(name: String) =
        flow { emit(apiCall { remoteDataSource.createCourse(CourseCreationReq(name)) }) }

    override suspend fun joinCourse(courseCode: String) =
        flow { emit(apiCall { remoteDataSource.joinCourse(courseCode) }) }

    override suspend fun deleteCourse(courseId: Int, courseOwnerId: Int) =
        flow { emit(apiCall { remoteDataSource.deleteCourse(courseId, courseOwnerId) }) }

    override suspend fun getTests(courseId: Int) =
        flow { emit(apiCall { remoteDataSource.getTests(courseId) }) }

    override suspend fun createTest(request: TestCreationReq) = flow {
        emit(apiCall { remoteDataSource.createTest(request) })
    }

    override suspend fun deleteTest(testId: Int, courseId: Int, courseOwnerId: Int) =
        flow { emit(apiCall { remoteDataSource.deleteTest(testId, courseId, courseOwnerId) }) }

    override suspend fun calculateResult(testId: Int, courseId: Int, request: List<UserQuestion>) =
        flow { emit(apiCall { remoteDataSource.calculateResult(testId, courseId, request) }) }

    override suspend fun calculateDemoResult(
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

    override suspend fun getResult(testId: Int, courseId: Int) =
        flow { emit(apiCall { remoteDataSource.getResult(testId, courseId) }) }

    override suspend fun getResults(
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

    override suspend fun addModerator(courseId: Int, courseOwnerId: Int, moderatorId: Int) =
        flow {
            emit(apiCall {
                remoteDataSource.addModerator(
                    courseId,
                    courseOwnerId,
                    moderatorId
                )
            })
        }

    override suspend fun deleteModerator(courseId: Int, courseOwnerId: Int, moderatorId: Int) =
        flow {
            emit(apiCall {
                remoteDataSource.deleteModerator(
                    courseId,
                    courseOwnerId,
                    moderatorId
                )
            })
        }

    override suspend fun deleteParticipant(courseId: Int, courseOwnerId: Int, participantId: Int) =
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