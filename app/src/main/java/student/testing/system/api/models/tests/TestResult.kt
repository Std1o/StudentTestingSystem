package student.testing.system.api.models.tests

import com.google.gson.annotations.SerializedName
import student.testing.system.models.Question

data class TestResult(
    @SerializedName("course_id") val courseId: Int,
    val name: String,
    @SerializedName("creation_time") val creationTime: String,
    val questions: List<Question>, val id: Int,
    @SerializedName("max_score") val maxScore: Int,
    val score: Int
)
