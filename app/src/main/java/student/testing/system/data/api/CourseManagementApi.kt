package student.testing.system.data.api

import retrofit2.Response
import retrofit2.http.*
import student.testing.system.data.dto.ParticipantDTO
import student.testing.system.domain.models.Participant

interface CourseManagementApi {

    @POST("course/moderators/")
    suspend fun addModerator(
        @Query("course_id") courseId: Int,
        @Query("moderator_id") moderatorId: Int
    ): Response<List<ParticipantDTO>>

    @DELETE("course/moderators/")
    suspend fun deleteModerator(
        @Query("course_id") courseId: Int,
        @Query("moderator_id") moderatorId: Int
    ): Response<List<ParticipantDTO>>

    @DELETE("/course/management/participants")
    suspend fun deleteParticipant(
        @Query("course_id") courseId: Int,
        @Query("participant_id") participantId: Int
    ): Response<List<ParticipantDTO>>
}