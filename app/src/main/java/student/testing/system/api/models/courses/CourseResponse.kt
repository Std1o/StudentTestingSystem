package student.testing.system.api.models.courses

import com.google.gson.annotations.SerializedName

data class CourseResponse(
    var name: String, var id: Int,
    @field:SerializedName("owner_id")
    var ownerId: String,
    var img: String,
    @field:SerializedName("course_code")
    var courseCode: String,
    var participants: List<Participant>
)