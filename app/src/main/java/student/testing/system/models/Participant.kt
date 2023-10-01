package student.testing.system.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Participant(
    @SerializedName("email") val email: String,
    @SerializedName("username") val username: String,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("is_moderator") val isModerator: Boolean,
    @SerializedName("is_owner") val isOwner: Boolean
) : Parcelable