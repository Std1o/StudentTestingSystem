package student.testing.system.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import student.testing.system.models.Question

@Parcelize
data class Test(
    @field:SerializedName("course_id") val courseId: Int,
    val name: String,
    @field:SerializedName("creation_time") val creationTime: String,
    val questions: List<Question>, val id: Int
): Parcelable