package student.testing.system.domain.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Participant(
    @SerializedName("email") val email: String = "",
    @SerializedName("username") val username: String = "",
    @SerializedName("user_id") val userId: Int = 0,
    @SerializedName("is_moderator") val isModerator: Boolean = false,
    @SerializedName("is_owner") val isOwner: Boolean = false
) : Parcelable