package student.testing.system.api.models.courses

import com.google.gson.annotations.SerializedName

data class CourseCreationReq(val name: String)

data class CourseJoiningReq(@SerializedName("course_code") val courseCode: String)