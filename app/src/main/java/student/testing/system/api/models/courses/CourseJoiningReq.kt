package student.testing.system.api.models.courses

import com.google.gson.annotations.SerializedName

data class CourseJoiningReq(@field:SerializedName("course_code") val courseCode: String)