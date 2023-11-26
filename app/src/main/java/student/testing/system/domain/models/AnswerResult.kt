package student.testing.system.domain.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AnswerResult(
    val answer: String,
    @SerializedName("is_right") val isRight: Boolean,
    @SerializedName("is_selected") val isSelected: Boolean, val id: Int
) : Parcelable