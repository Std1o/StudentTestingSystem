package student.testing.system.api.models.tests

import com.google.gson.annotations.SerializedName

data class TestResultReq(
    @SerializedName("test_id") val test_id: Int,
    @SerializedName("course_id") val courseId: Int
)
