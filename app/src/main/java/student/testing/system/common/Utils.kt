package student.testing.system.common

import student.testing.system.models.CourseResponse

object Utils {

    fun isUserAModerator(course: CourseResponse): Boolean {
        val currentParticipant = course.participants
            .first { it.userId == AccountSession.instance.userId }
        return currentParticipant.isModerator || currentParticipant.isOwner
    }
}