package student.testing.system.api.network

import kotlinx.coroutines.flow.flow
import student.testing.system.api.network.MainRemoteData
import student.testing.system.api.models.signup.SignUpReq
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val remoteData : MainRemoteData
) {

    suspend fun auth(email: String, password: String) = flow { emit(remoteData.auth("grant_type=&username=$email&password=$password&scope=&client_id=&client_secret="))}
    suspend fun signUp(email: String, username: String, password: String) = remoteData.signUp(SignUpReq(email, username, password))
}