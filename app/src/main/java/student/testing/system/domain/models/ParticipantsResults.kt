package student.testing.system.domain.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParticipantsResults(
    val results: List<ParticipantResult>,
    @SerializedName("max_score") val maxScore: Int
) : Parcelable
