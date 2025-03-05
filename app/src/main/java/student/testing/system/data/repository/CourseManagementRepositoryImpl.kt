package student.testing.system.data.repository

import student.testing.system.data.mapper.ParticipantMapper
import student.testing.system.data.source.interfaces.CourseManagementRemoteDataSource
import student.testing.system.domain.repository.CourseManagementRepository
import javax.inject.Inject

class CourseManagementRepositoryImpl @Inject constructor(
    private val courseManagementRemoteDataSource: CourseManagementRemoteDataSource,
) : CourseManagementRepository {

    override suspend fun addModerator(courseId: Int, moderatorId: Int) =
        ParticipantMapper().map(
            courseManagementRemoteDataSource.addModerator(courseId, moderatorId)
        )


    override suspend fun deleteModerator(courseId: Int, moderatorId: Int) =
        ParticipantMapper().map(
            courseManagementRemoteDataSource.deleteModerator(courseId, moderatorId)
        )

    override suspend fun deleteParticipant(courseId: Int, participantId: Int) =
        ParticipantMapper().map(
            courseManagementRemoteDataSource.deleteParticipant(courseId, participantId)
        )

}