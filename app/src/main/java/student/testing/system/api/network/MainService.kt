package student.testing.system.api.network

import retrofit2.Response
import retrofit2.http.*
import student.testing.system.api.models.Token
import student.testing.system.api.models.courses.CourseCreationReq
import student.testing.system.api.models.courses.CourseJoiningReq
import student.testing.system.api.models.courses.CourseResponse
import student.testing.system.api.models.courses.DeleteCourseReq
import student.testing.system.api.models.signup.SignUpReq
import student.testing.system.api.models.tests.TestCreationReq
import student.testing.system.models.Test
import student.testing.system.models.User


interface MainService {

    @POST("auth/sign-in")
    suspend fun auth(@Header("accept") accept: String, @Header("Content-Type") contentType: String, @Body request: String): Response<Token>

    @POST("auth/sign-up")
    suspend fun signUp(@Body request: SignUpReq): Response<Token>

    @GET("auth/user")
    suspend fun getUser(@Header("Authorization") token: String): Response<User>

    @GET("courses/")
    suspend fun getCourses(@Header("Authorization") token: String): Response<List<CourseResponse>>

    @POST("courses/")
    suspend fun createCourse(@Header("Authorization") token: String, @Body request: CourseCreationReq): Response<CourseResponse>

    @POST("courses/{courseCode}")
    suspend fun joinCourse(@Header("Authorization") token: String, @Path("courseCode") courseCode: String, @Body request: CourseJoiningReq): Response<CourseResponse>

    @GET("courses/{courseId}")
    suspend fun getCourse(@Header("Authorization") token: String, @Path("courseId") courseId: Int): Response<CourseResponse>

    @DELETE("courses/{courseId}")
    suspend fun deleteCourse(@Header("Authorization") token: String, @Path("courseId") courseId: Int): Response<Void>

    @GET("tests/")
    suspend fun getTests(@Header("Authorization") token: String, @Query("course_id") courseId: Int): Response<List<Test>>

    @POST("tests/")
    suspend fun createTest(@Header("Authorization") token: String, @Body request: TestCreationReq): Response<Test>
}