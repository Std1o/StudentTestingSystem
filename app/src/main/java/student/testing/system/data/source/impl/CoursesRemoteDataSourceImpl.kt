package student.testing.system.data.source.impl

import godofappstates.data.dataSource.BaseRemoteDataSource
import student.testing.system.data.api.CoursesApi
import student.testing.system.data.source.interfaces.CoursesRemoteDataSource
import student.testing.system.domain.operationTypes.CourseAddingOperations
import student.testing.system.domain.models.CourseCreationReq
import javax.inject.Inject

class CoursesRemoteDataSourceImpl @Inject constructor(private val coursesApi: CoursesApi) :
    BaseRemoteDataSource(), CoursesRemoteDataSource {

    override suspend fun getCourses() = loadData { coursesApi.getCourses() }

    override suspend fun createCourse(request: CourseCreationReq) =
        executeOperation(CourseAddingOperations.CREATE_COURSE) { coursesApi.createCourse(request) }

    override suspend fun joinCourse(courseCode: String) =
        executeOperation(CourseAddingOperations.JOIN_COURSE) { coursesApi.joinCourse(courseCode) }

    override suspend fun deleteCourse(courseId: Int) =
        executeOperation { coursesApi.deleteCourse(courseId) }
}