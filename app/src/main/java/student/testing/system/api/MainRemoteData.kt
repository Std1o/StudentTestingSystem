package student.testing.system.api

import student.testing.system.api.models.signup.SignUpReq
import javax.inject.Inject

class MainRemoteData @Inject constructor(private val mainService : MainService) {
    suspend fun auth(request: String) = mainService.auth("application/json", "application/x-www-form-urlencoded", request)
    suspend fun signUp(request: SignUpReq) = mainService.signUp(request)
}