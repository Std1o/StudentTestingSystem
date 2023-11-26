package student.testing.system.domain.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Answer(
    val answer: String = "",
    @SerializedName("is_right") var isRight: Boolean = false,
    val id: Int? = null
) : Parcelable