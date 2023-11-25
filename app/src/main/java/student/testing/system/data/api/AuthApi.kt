package student.testing.system.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import student.testing.system.models.PrivateUser
import student.testing.system.models.SignUpReq

interface AuthApi {
    @POST("auth/sign-in")
    @Headers("accept: application/json", "Content-Type: application/x-www-form-urlencoded")
    suspend fun auth(@Body request: String): Response<PrivateUser>

    @POST("auth/sign-up")
    suspend fun signUp(@Body request: SignUpReq): Response<PrivateUser>
}