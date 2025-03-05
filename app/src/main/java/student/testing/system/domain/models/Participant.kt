package student.testing.system.domain.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Participant(
    val email: String = "",
    val username: String = "",
    val userId: Int = 0,
    val isModerator: Boolean = false,
    val isOwner: Boolean = false
) : Parcelable