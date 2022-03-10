package student.testing.system

import student.testing.system.api.MainRemoteData
import student.testing.system.api.models.signup.SignUpReq
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val remoteData : MainRemoteData
) {

    suspend fun auth() = remoteData.auth("grant_type=&username=Stdio&password=pass&scope=&client_id=&client_secret=")
    suspend fun signUp(email: String, username: String, password: String) = remoteData.signUp(SignUpReq(email, username, password))
}