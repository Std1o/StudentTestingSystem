package student.testing.system.data.source.interfaces

import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.domain.models.CourseCreationReq
import student.testing.system.domain.models.CourseResponse

interface CoursesRemoteDataSource {
    suspend fun getCourses(): LoadableData<List<CourseResponse>>
    suspend fun createCourse(request: CourseCreationReq): OperationState<CourseResponse>
    suspend fun joinCourse(courseCode: String): OperationState<CourseResponse>
    suspend fun deleteCourse(courseId: Int): OperationState<Void>
}