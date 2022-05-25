package student.testing.system.models

import com.google.gson.annotations.SerializedName
import student.testing.system.models.Question

data class CourseCreationReq(val name: String)

data class CourseJoiningReq(@SerializedName("course_code") val courseCode: String)

data class SignUpReq(val email: String, val username: String, val password: String)

data class TestCreationReq(
    @SerializedName("course_id") val courseId: Int,
    val name: String,
    @SerializedName("creation_time") val creationTIme: String,
    val questions: List<Question>
)

data class UserQuestion(
    @SerializedName("question_id") val questionId: Int,
    val answers: List<UserAnswer>
)

data class UserAnswer(
    @SerializedName("answer_id") val answerId: Int,
    @SerializedName("is_selected") val isSelected: Boolean
)

data class TestResultReq(
    @SerializedName("test_id") val test_id: Int,
    @SerializedName("course_id") val courseId: Int
)
