package student.testing.system.data.repository

import student.testing.system.data.mapper.CourseMapper
import student.testing.system.data.mapper.CoursesListMapper
import student.testing.system.data.source.interfaces.CoursesRemoteDataSource
import student.testing.system.domain.models.CourseCreationReq
import student.testing.system.domain.repository.CoursesRepository
import javax.inject.Inject

class CoursesRepositoryImpl @Inject constructor(private val remoteDataSource: CoursesRemoteDataSource) :
    CoursesRepository {

    override suspend fun getCourses() = CoursesListMapper().map(remoteDataSource.getCourses())

    override suspend fun createCourse(name: String) = CourseMapper().map(
        remoteDataSource.createCourse(CourseCreationReq(name))
    )

    override suspend fun joinCourse(courseCode: String) = CourseMapper().map(
        remoteDataSource.joinCourse(courseCode)
    )

    override suspend fun deleteCourse(courseId: Int) =
        remoteDataSource.deleteCourse(courseId)
}