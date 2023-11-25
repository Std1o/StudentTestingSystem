package student.testing.system.data

import retrofit2.Response
import retrofit2.http.*
import student.testing.system.models.*
import student.testing.system.models.CourseResponse
import student.testing.system.models.TestResult
import student.testing.system.models.Test


interface MainService {

    @POST("course/moderators/")
    suspend fun addModerator(
        @Query("course_id") courseId: Int,
        @Query("moderator_id") moderatorId: Int
    ): Response<List<Participant>>

    @DELETE("course/moderators/")
    suspend fun deleteModerator(
        @Query("course_id") courseId: Int,
        @Query("moderator_id") moderatorId: Int
    ): Response<List<Participant>>

    @DELETE("/course/management/participants")
    suspend fun deleteParticipant(
        @Query("course_id") courseId: Int,
        @Query("participant_id") participantId: Int
    ): Response<List<Participant>>
}