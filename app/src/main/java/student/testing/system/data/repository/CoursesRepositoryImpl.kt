package student.testing.system.data.repository

import student.testing.system.data.source.interfaces.CoursesRemoteDataSource
import student.testing.system.domain.repository.CoursesRepository
import student.testing.system.models.CourseCreationReq
import javax.inject.Inject

class CoursesRepositoryImpl @Inject constructor(private val coursesRemoteDataSource: CoursesRemoteDataSource) :
    CoursesRepository {

    override suspend fun getCourses() = coursesRemoteDataSource.getCourses()

    override suspend fun createCourse(name: String) =
        coursesRemoteDataSource.createCourse(CourseCreationReq(name))

    override suspend fun joinCourse(courseCode: String) =
        coursesRemoteDataSource.joinCourse(courseCode)

    override suspend fun deleteCourse(courseId: Int) =
        coursesRemoteDataSource.deleteCourse(courseId)
}