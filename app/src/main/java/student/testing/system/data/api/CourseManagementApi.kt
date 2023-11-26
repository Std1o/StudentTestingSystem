package student.testing.system.data.api

import retrofit2.Response
import retrofit2.http.*
import student.testing.system.domain.models.Participant
import student.testing.system.models.*


interface CourseManagementApi {

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