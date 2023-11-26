package student.testing.system.data.repository

import student.testing.system.data.source.interfaces.CourseManagementRemoteDataSource
import student.testing.system.domain.repository.CourseManagementRepository
import javax.inject.Inject

class CourseManagementRepositoryImpl @Inject constructor(
    private val courseManagementRemoteDataSource: CourseManagementRemoteDataSource,
) : CourseManagementRepository {

    override suspend fun addModerator(courseId: Int, moderatorId: Int) =
        courseManagementRemoteDataSource.addModerator(courseId, moderatorId)


    override suspend fun deleteModerator(courseId: Int, moderatorId: Int) =
        courseManagementRemoteDataSource.deleteModerator(courseId, moderatorId)


    override suspend fun deleteParticipant(courseId: Int, participantId: Int) =
        courseManagementRemoteDataSource.deleteParticipant(courseId, participantId)

}