package student.testing.system.data.repository

import student.testing.system.data.source.interfaces.CoursesRemoteDataSource
import student.testing.system.domain.repository.CoursesRepository
import student.testing.system.domain.models.CourseCreationReq
import javax.inject.Inject

class CoursesRepositoryImpl @Inject constructor(private val remoteDataSource: CoursesRemoteDataSource) :
    CoursesRepository {

    override suspend fun getCourses() = remoteDataSource.getCourses()

    override suspend fun createCourse(name: String) =
        remoteDataSource.createCourse(CourseCreationReq(name))

    override suspend fun joinCourse(courseCode: String) =
        remoteDataSource.joinCourse(courseCode)

    override suspend fun deleteCourse(courseId: Int) =
        remoteDataSource.deleteCourse(courseId)
}