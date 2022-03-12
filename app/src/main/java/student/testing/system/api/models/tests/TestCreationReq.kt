package student.testing.system.api.models.tests

import com.google.gson.annotations.SerializedName
import student.testing.system.models.Question

data class TestCreationReq(
    @field:SerializedName("course_id") val courseId: Int,
    val name: String,
    @field:SerializedName("creation_time") val creationTIme: String,
    val questions: List<Question>
)
