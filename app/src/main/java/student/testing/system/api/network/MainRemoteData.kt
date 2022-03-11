package student.testing.system.api.network

import student.testing.system.api.models.signup.SignUpReq
import student.testing.system.common.AccountSession
import javax.inject.Inject

class MainRemoteData @Inject constructor(private val mainService : MainService) {
    suspend fun auth(request: String) = mainService.auth("application/json", "application/x-www-form-urlencoded", request)
    suspend fun signUp(request: SignUpReq) = mainService.signUp(request)
    suspend fun getCourses() = mainService.getCourses("Bearer ${AccountSession.instance.token!!}")
}