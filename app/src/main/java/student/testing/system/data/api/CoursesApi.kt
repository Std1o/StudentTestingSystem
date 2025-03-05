package student.testing.system.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import student.testing.system.data.dto.CourseDTO
import student.testing.system.domain.models.CourseCreationReq
import student.testing.system.domain.models.Course

interface CoursesApi {
    @GET("courses/")
    suspend fun getCourses(): Response<List<CourseDTO>>

    @POST("courses/")
    suspend fun createCourse(@Body request: CourseCreationReq): Response<Course>

    @POST("courses/{course_code}")
    suspend fun joinCourse(@Path("course_code") courseCode: String): Response<Course>

    @DELETE("courses/{course_id}")
    suspend fun deleteCourse(
        @Path("course_id") courseId: Int,
    ): Response<Void>
}