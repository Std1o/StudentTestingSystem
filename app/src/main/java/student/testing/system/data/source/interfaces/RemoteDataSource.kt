package student.testing.system.data.source.interfaces

import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.models.Participant

interface RemoteDataSource {
    suspend fun addModerator(
        courseId: Int,
        moderatorId: Int
    ): OperationState<List<Participant>>

    suspend fun deleteModerator(
        courseId: Int,
        moderatorId: Int
    ): OperationState<List<Participant>>

    suspend fun deleteParticipant(
        courseId: Int,
        participantId: Int
    ): OperationState<List<Participant>>
}