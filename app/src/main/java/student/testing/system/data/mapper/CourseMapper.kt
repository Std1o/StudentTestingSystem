package student.testing.system.data.mapper

import student.testing.system.data.dto.CourseDTO
import student.testing.system.domain.OperationStateMapper
import student.testing.system.domain.models.Course
import student.testing.system.domain.states.operationStates.OperationState

class CourseMapper : OperationStateMapper<CourseDTO, Course>() {
    override fun getSuccess(input: OperationState.Success<CourseDTO>): Course = Course(
        name = input.data.name,
        id = input.data.id,
        img = input.data.img,
        courseCode = input.data.courseCode,
        participants = input.data.participantsDTO.map(ParticipantItemMapper()::map)
    )
}