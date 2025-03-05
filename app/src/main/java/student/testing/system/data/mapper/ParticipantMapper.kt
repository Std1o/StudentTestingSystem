package student.testing.system.data.mapper

import student.testing.system.data.dto.ParticipantDTO
import student.testing.system.domain.OperationStateListMapper
import student.testing.system.domain.models.Participant

class ParticipantMapper : OperationStateListMapper<ParticipantDTO, Participant>() {
    override fun getSuccess(input: ParticipantDTO): Participant =
        Participant(
            email = input.email,
            username = input.username,
            userId = input.userId,
            isModerator = input.isModerator,
            isOwner = input.isOwner
        )
}