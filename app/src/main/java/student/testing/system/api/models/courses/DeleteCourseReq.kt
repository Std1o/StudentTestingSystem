package student.testing.system.api.models.courses

import com.google.gson.annotations.SerializedName

data class DeleteCourseReq (@field:SerializedName("course_id") val courseId: Int)