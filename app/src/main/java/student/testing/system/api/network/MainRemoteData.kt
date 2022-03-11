package student.testing.system.api.network

import student.testing.system.api.models.courses.CourseCreationReq
import student.testing.system.api.models.courses.CourseJoiningReq
import student.testing.system.api.models.signup.SignUpReq
import student.testing.system.common.AccountSession
import javax.inject.Inject

class MainRemoteData @Inject constructor(private val mainService : MainService) {
    suspend fun auth(request: String) = mainService.auth("application/json", "application/x-www-form-urlencoded", request)
    suspend fun signUp(request: SignUpReq) = mainService.signUp(request)
    suspend fun getCourses() = mainService.getCourses("Bearer ${AccountSession.instance.token!!}")
    suspend fun createCourse(request: CourseCreationReq) = mainService.createCourse("Bearer ${AccountSession.instance.token!!}", request)
    suspend fun joinCourse(courseCode: String, request: CourseJoiningReq) = mainService.joinCourse("Bearer ${AccountSession.instance.token!!}", courseCode, request)
}