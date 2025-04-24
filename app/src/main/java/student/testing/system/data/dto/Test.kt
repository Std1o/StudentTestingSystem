package student.testing.system.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Test(
    @SerializedName("course_id") val courseId: Int = 0,
    val name: String = "",
    @SerializedName("creation_time") val creationTime: String = "",
    val questions: List<Question> = emptyList(),
    val id: Int = 0
): Parcelable