package student.testing.system.data.source.interfaces

import student.testing.system.data.dto.CourseDTO
import student.testing.system.domain.states.loadableData.LoadableData
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.domain.models.CourseCreationReq
import student.testing.system.domain.models.Course

interface CoursesRemoteDataSource {
    suspend fun getCourses(): LoadableData<List<CourseDTO>>
    suspend fun createCourse(request: CourseCreationReq): OperationState<CourseDTO>
    suspend fun joinCourse(courseCode: String): OperationState<CourseDTO>
    suspend fun deleteCourse(courseId: Int): OperationState<Void>
}