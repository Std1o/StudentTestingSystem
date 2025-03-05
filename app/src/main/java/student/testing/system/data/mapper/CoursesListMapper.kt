package student.testing.system.data.mapper

import student.testing.system.data.dto.CourseDTO
import student.testing.system.domain.LoadableDataListMapper
import student.testing.system.domain.models.Course

class CoursesListMapper : LoadableDataListMapper<CourseDTO, Course>() {
    override fun getSuccess(input: CourseDTO): Course = Course(
        name = input.name,
        id = input.id,
        img = input.img,
        courseCode = input.courseCode,
        participants = input.participantsDTO.map(ParticipantItemMapper()::map)
    )
}