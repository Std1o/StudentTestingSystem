package student.testing.system.data.repository

import student.testing.system.data.source.interfaces.CourseManagementRemoteDataSource
import student.testing.system.data.source.interfaces.TestsRemoteDataSource
import student.testing.system.domain.repository.MainRepository
import student.testing.system.models.TestCreationReq
import student.testing.system.models.TestResultsRequestParams
import student.testing.system.models.UserQuestion
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val courseManagementRemoteDataSource: CourseManagementRemoteDataSource,
) : MainRepository {

    override suspend fun addModerator(courseId: Int, moderatorId: Int) =
        courseManagementRemoteDataSource.addModerator(courseId, moderatorId)


    override suspend fun deleteModerator(courseId: Int, moderatorId: Int) =
        courseManagementRemoteDataSource.deleteModerator(courseId, moderatorId)


    override suspend fun deleteParticipant(courseId: Int, participantId: Int) =
        courseManagementRemoteDataSource.deleteParticipant(courseId, participantId)

}