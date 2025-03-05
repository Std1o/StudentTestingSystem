package student.testing.system.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParticipantDTO(
    @SerializedName("email") val email: String = "",
    @SerializedName("username") val username: String = "",
    @SerializedName("user_id") val userId: Int = 0,
    @SerializedName("is_moderator") val isModerator: Boolean = false,
    @SerializedName("is_owner") val isOwner: Boolean = false
) : Parcelable