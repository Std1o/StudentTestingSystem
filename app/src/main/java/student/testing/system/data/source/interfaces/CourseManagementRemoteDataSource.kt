package student.testing.system.data.source.interfaces

import student.testing.system.data.dto.ParticipantDTO
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.domain.models.Participant

interface CourseManagementRemoteDataSource {
    suspend fun addModerator(
        courseId: Int,
        moderatorId: Int
    ): OperationState<List<ParticipantDTO>>

    suspend fun deleteModerator(
        courseId: Int,
        moderatorId: Int
    ): OperationState<List<ParticipantDTO>>

    suspend fun deleteParticipant(
        courseId: Int,
        participantId: Int
    ): OperationState<List<ParticipantDTO>>
}