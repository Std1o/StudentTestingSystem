package student.testing.system.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TestResult(
    val questions: List<QuestionResult> = emptyList(),
    @SerializedName("max_score") val maxScore: Int = 0,
    val score: Double = 0.0
) : Parcelable
