package student.testing.system.api.network

import kotlinx.coroutines.flow.flow
import student.testing.system.api.models.courses.CourseCreationReq
import student.testing.system.api.models.courses.CourseJoiningReq
import student.testing.system.api.network.MainRemoteData
import student.testing.system.api.models.signup.SignUpReq
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val remoteData : MainRemoteData
) {

    suspend fun auth(email: String, password: String) = flow { emit(remoteData.auth("grant_type=&username=$email&password=$password&scope=&client_id=&client_secret="))}
    suspend fun signUp(email: String, username: String, password: String) = flow { emit(remoteData.signUp(SignUpReq(email, username, password)))}
    suspend fun getUser() = flow { emit(remoteData.getUser())}
    suspend fun getCourses() = flow { emit(remoteData.getCourses())}
    suspend fun createCourse(name: String) = flow { emit(remoteData.createCourse(CourseCreationReq(name)))}
    suspend fun joinCourse(courseCode: String) = flow { emit(remoteData.joinCourse(courseCode, CourseJoiningReq(courseCode)))}
    suspend fun getCourse(courseId: Int) = flow { emit(remoteData.getCourse(courseId))}
    suspend fun deleteCourse(courseId: Int) = flow { emit(remoteData.deleteCourse(courseId))}
    suspend fun getTests(courseId: Int) = flow { emit(remoteData.getTests(courseId))}
}