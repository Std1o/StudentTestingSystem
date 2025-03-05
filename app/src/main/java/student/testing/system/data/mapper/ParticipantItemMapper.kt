package student.testing.system.data.mapper

import student.testing.system.data.dto.ParticipantDTO
import student.testing.system.domain.Mapper
import student.testing.system.domain.models.Participant

class ParticipantItemMapper : Mapper<ParticipantDTO, Participant> {
    override fun map(input: ParticipantDTO): Participant = Participant(
        email = input.email,
        username = input.username,
        userId = input.userId,
        isModerator = input.isModerator,
        isOwner = input.isOwner
    )
}