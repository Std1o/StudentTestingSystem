package student.testing.system.domain.repository

import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.domain.models.Course

interface CoursesRepository {
    suspend fun getCourses(): LoadableData<List<Course>>
    suspend fun createCourse(name: String): OperationState<Course>
    suspend fun joinCourse(courseCode: String): OperationState<Course>
    suspend fun deleteCourse(courseId: Int): OperationState<Void>
}