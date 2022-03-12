package student.testing.system.models

import com.google.gson.annotations.SerializedName
import student.testing.system.models.Question

data class Test(
    @field:SerializedName("course_id") val courseId: Int,
    val name: String,
    @field:SerializedName("creation_time") val creationTime: String,
    val questions: List<Question>, val id: Int
)