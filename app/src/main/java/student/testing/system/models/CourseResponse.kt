package student.testing.system.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CourseResponse(
    @SerializedName("name") var name: String = "",
    @SerializedName("id") var id: Int = 0,
    @SerializedName("img") var img: String = "",
    @SerializedName("course_code") var courseCode: String = "",
    @SerializedName("participants") var participants: List<Participant> = listOf(),
) : Parcelable