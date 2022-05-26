package student.testing.system.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import student.testing.system.models.Question

@Parcelize
data class TestResult(
    @SerializedName("course_id") val courseId: Int,
    val name: String,
    @SerializedName("creation_time") val creationTime: String,
    val questions: List<Question>, val id: Int,
    @SerializedName("max_score") val maxScore: Int,
    val score: Double
): Parcelable
