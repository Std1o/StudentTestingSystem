package student.testing.system.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestionResult(
    val question: String,
    val answers: List<AnswerResult>,
    val score: Double,
    val id: Int
) : Parcelable
