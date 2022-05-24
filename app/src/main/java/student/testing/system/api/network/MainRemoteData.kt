package student.testing.system.api.network

import student.testing.system.api.models.*
import javax.inject.Inject

class MainRemoteData @Inject constructor(private val mainService : MainService) {
    suspend fun auth(request: String) = mainService.auth(request)
    suspend fun signUp(request: SignUpReq) = mainService.signUp(request)
    suspend fun getUser() = mainService.getUser()
    suspend fun getCourses() = mainService.getCourses()
    suspend fun createCourse(request: CourseCreationReq) = mainService.createCourse(request)
    suspend fun joinCourse(courseCode: String, request: CourseJoiningReq) = mainService.joinCourse(courseCode, request)
    suspend fun getCourse(courseId: Int) = mainService.getCourse(courseId)
    suspend fun deleteCourse(courseId: Int) = mainService.deleteCourse(courseId)
    suspend fun getTests(courseId: Int) = mainService.getTests(courseId)
    suspend fun createTest(request: TestCreationReq) = mainService.createTest(request)
    suspend fun getResult(testId: Int, courseId: Int) = mainService.getResult(testId, courseId)
}