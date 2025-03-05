package student.testing.system.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import student.testing.system.data.dto.PrivateUserDTO
import student.testing.system.domain.models.PrivateUser
import student.testing.system.domain.models.SignUpReq

interface AuthApi {
    @POST("auth/sign-in")
    @Headers("accept: application/json", "Content-Type: application/x-www-form-urlencoded")
    suspend fun auth(@Body request: String): Response<PrivateUserDTO>

    @POST("auth/sign-up")
    suspend fun signUp(@Body request: SignUpReq): Response<PrivateUserDTO>
}