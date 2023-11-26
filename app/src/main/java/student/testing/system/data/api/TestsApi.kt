package student.testing.system.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import student.testing.system.domain.models.ParticipantsResults
import student.testing.system.domain.models.Test
import student.testing.system.domain.models.TestCreationReq
import student.testing.system.domain.models.TestResult
import student.testing.system.domain.models.TestResultsRequestParams
import student.testing.system.domain.models.UserQuestion

interface TestsApi {
    @GET("tests/")
    suspend fun getTests(@Query("course_id") courseId: Int): Response<List<Test>>

    @POST("tests/")
    suspend fun createTest(@Body request: TestCreationReq): Response<Test>

    @DELETE("tests/{test_id}")
    suspend fun deleteTest(
        @Path("test_id") testId: Int,
        @Query("course_id") courseId: Int,
    ): Response<Void>

    @POST("tests/{test_id}")
    suspend fun calculateResult(
        @Path("test_id") testId: Int,
        @Query("course_id") courseId: Int,
        @Body request: List<UserQuestion>
    ): Response<Void>

    @POST("tests/demo_result/")
    suspend fun calculateDemoResult(
        @Query("course_id") courseId: Int,
        @Query("test_id") testId: Int,
        @Body request: List<UserQuestion>
    ): Response<TestResult>

    @GET("tests/result/{test_id}")
    suspend fun getResult(
        @Path("test_id") testId: Int,
        @Query("course_id") courseId: Int
    ): Response<TestResult>

    @POST("tests/results/{test_id}")
    suspend fun getResults(
        @Path("test_id") testId: Int,
        @Query("course_id") courseId: Int,
        @Body params: TestResultsRequestParams
    ): Response<ParticipantsResults>
}