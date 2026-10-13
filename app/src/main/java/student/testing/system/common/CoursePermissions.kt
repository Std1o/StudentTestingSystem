package student.testing.system.common

import student.testing.system.domain.models.Course
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoursePermissions @Inject constructor(private val session: AccountSession) {

    fun isUserAModerator(course: Course): Boolean {
        val userId = session.userId ?: return false
        val participant = course.participants.firstOrNull { it.userId == userId }
        return participant?.isModerator == true || participant?.isOwner == true
    }

    fun isUserAnOwner(course: Course): Boolean {
        val userId = session.userId ?: return false
        return course.participants.any { it.userId == userId && it.isOwner }
    }
}