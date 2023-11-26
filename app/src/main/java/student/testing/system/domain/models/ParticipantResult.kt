package student.testing.system.domain.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParticipantResult(
    @SerializedName("user_id") val userId: Int = 0,
    val username: String = "",
    val email: String = "",
    val score: Double = 0.0,
    @SerializedName("passing_time") val passingTime: String = ""
): Parcelable
