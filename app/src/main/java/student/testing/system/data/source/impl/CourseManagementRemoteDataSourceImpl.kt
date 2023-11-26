package student.testing.system.data.source.impl

import godofappstates.data.dataSource.BaseRemoteDataSource
import student.testing.system.data.api.CourseManagementApi
import student.testing.system.data.source.interfaces.CourseManagementRemoteDataSource
import javax.inject.Inject

class CourseManagementRemoteDataSourceImpl @Inject constructor(private val courseManagementApi: CourseManagementApi) :
    BaseRemoteDataSource(), CourseManagementRemoteDataSource {

    override suspend fun addModerator(courseId: Int, moderatorId: Int) =
        executeOperation { courseManagementApi.addModerator(courseId, moderatorId) }

    override suspend fun deleteModerator(courseId: Int, moderatorId: Int) =
        executeOperation { courseManagementApi.deleteModerator(courseId, moderatorId) }

    override suspend fun deleteParticipant(courseId: Int, participantId: Int) =
        executeOperation { courseManagementApi.deleteParticipant(courseId, participantId) }
}