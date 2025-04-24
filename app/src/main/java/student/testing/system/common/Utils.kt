package student.testing.system.common

import student.testing.system.domain.models.Course

object Utils {

    fun isUserAModerator(course: Course): Boolean {
        val currentParticipant = course.participants
            .first { it.userId == AccountSession.instance.userId }
        return currentParticipant.isModerator || currentParticipant.isOwner
    }

    fun isUserAnOwner(course: Course): Boolean {
        val currentParticipant = course.participants
            .first { it.userId == AccountSession.instance.userId }
        return currentParticipant.isOwner
    }
}