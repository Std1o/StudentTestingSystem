package student.testing.system.domain.repository

import student.testing.system.domain.models.Participant
import student.testing.system.domain.states.operationStates.OperationState

interface CourseManagementRepository {

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