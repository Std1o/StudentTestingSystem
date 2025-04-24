package student.testing.system.domain.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Course(
    val name: String = "",
    val id: Int = 0,
    val img: String = "",
    val courseCode: String = "",
    val participants: List<Participant> = listOf(),
) : Parcelable