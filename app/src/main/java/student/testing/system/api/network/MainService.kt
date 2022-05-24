package student.testing.system.api.network

import retrofit2.Response
import retrofit2.http.*
import student.testing.system.api.models.*
import student.testing.system.api.models.Token
import student.testing.system.api.models.courses.CourseResponse
import student.testing.system.api.models.tests.TestResult
import student.testing.system.models.Test
import student.testing.system.models.User


interface MainService {

    @POST("auth/sign-in")
    @Headers("accept: application/json", "Content-Type: application/x-www-form-urlencoded")
    suspend fun auth(@Body request: String): Response<Token>

    @POST("auth/sign-up")
    suspend fun signUp(@Body request: SignUpReq): Response<Token>

    @GET("auth/user")
    suspend fun getUser(): Response<User>

    @GET("courses/")
    suspend fun getCourses(): Response<List<CourseResponse>>

    @POST("courses/")
    suspend fun createCourse(@Body request: CourseCreationReq): Response<CourseResponse>

    @POST("courses/{courseCode}")
    suspend fun joinCourse(
        @Path("courseCode") courseCode: String,
        @Body request: CourseJoiningReq
    ): Response<CourseResponse>

    @GET("courses/{courseId}")
    suspend fun getCourse(@Path("courseId") courseId: Int): Response<CourseResponse>

    @DELETE("courses/{courseId}")
    suspend fun deleteCourse(@Path("courseId") courseId: Int): Response<Void>

    @GET("tests/")
    suspend fun getTests(@Query("course_id") courseId: Int): Response<List<Test>>

    @POST("tests/")
    suspend fun createTest(@Body request: TestCreationReq): Response<Test>

    @GET("tests/result/{test_id}")
    suspend fun getResult(
        @Path("test_id") testId: Int,
        @Query("course_id") courseId: Int
    ): Response<TestResult>
}