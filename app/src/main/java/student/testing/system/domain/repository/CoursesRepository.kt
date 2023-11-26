package student.testing.system.domain.repository

import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.domain.models.CourseResponse

interface CoursesRepository {
    suspend fun getCourses(): LoadableData<List<CourseResponse>>
    suspend fun createCourse(name: String): OperationState<CourseResponse>
    suspend fun joinCourse(courseCode: String): OperationState<CourseResponse>
    suspend fun deleteCourse(courseId: Int): OperationState<Void>
}