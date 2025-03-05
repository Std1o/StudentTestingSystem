package student.testing.system.data.repository

import io.mockk.mockk
import student.testing.system.domain.repository.CoursesRepository
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.domain.models.Course

class FakeCoursesRepository : CoursesRepository {

    override suspend fun getCourses(): LoadableData<List<Course>> {
        TODO("Not yet implemented")
    }

    override suspend fun createCourse(name: String): OperationState<Course> {
        TODO("Not yet implemented")
    }

    override suspend fun joinCourse(courseCode: String): OperationState<Course> {
        val courses = listOf("5TYHKW", "KASTXJ", "XHYX6U")
        if (courseCode in courses) {
            val course = mockk<Course>(relaxed = true)
            return OperationState.Success(course)
        } else {
            return OperationState.Error("Not found", 404)
        }
    }

    override suspend fun deleteCourse(courseId: Int): OperationState<Void> {
        TODO("Not yet implemented")
    }
}