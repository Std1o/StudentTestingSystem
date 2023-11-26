package student.testing.system.data.source.impl

import godofappstates.data.dataSource.BaseRemoteDataSource
import student.testing.system.data.api.CourseManagementApi
import student.testing.system.data.source.interfaces.RemoteDataSource
import student.testing.system.domain.operationTypes.TestsOperations
import student.testing.system.models.TestCreationReq
import student.testing.system.models.TestResultsRequestParams
import student.testing.system.models.UserQuestion
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val courseManagementApi: CourseManagementApi) :
    BaseRemoteDataSource(), RemoteDataSource {

    override suspend fun addModerator(courseId: Int, moderatorId: Int) =
        executeOperation { courseManagementApi.addModerator(courseId, moderatorId) }

    override suspend fun deleteModerator(courseId: Int, moderatorId: Int) =
        executeOperation { courseManagementApi.deleteModerator(courseId, moderatorId) }

    override suspend fun deleteParticipant(courseId: Int, participantId: Int) =
        executeOperation { courseManagementApi.deleteParticipant(courseId, participantId) }
}