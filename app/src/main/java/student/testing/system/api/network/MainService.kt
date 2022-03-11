package student.testing.system.api.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import student.testing.system.api.models.Token
import student.testing.system.api.models.courses.CourseCreationReq
import student.testing.system.api.models.courses.CourseResponse
import student.testing.system.api.models.signup.SignUpReq


interface MainService {

    @POST("auth/sign-in")
    suspend fun auth(@Header("accept") accept: String, @Header("Content-Type") contentType: String, @Body request: String): Response<Token>

    @POST("auth/sign-up")
    suspend fun signUp(@Body request: SignUpReq): Response<Token>

    @GET("courses/")
    suspend fun getCourses(@Header("Authorization") token: String): Response<List<CourseResponse>>

    @POST("courses/")
    suspend fun createCourse(@Header("Authorization") token: String, @Body request: CourseCreationReq): Response<CourseResponse>
}