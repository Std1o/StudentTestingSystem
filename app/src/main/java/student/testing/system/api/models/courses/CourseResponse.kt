package student.testing.system.api.models.courses

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CourseResponse(
    var name: String, var id: Int,
    @field:SerializedName("owner_id")
    var ownerId: Int,
    var img: String,
    @field:SerializedName("course_code")
    var courseCode: String,
    var participants: List<Participant>
) : Serializable