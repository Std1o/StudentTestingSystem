package student.testing.system.models

import com.google.gson.annotations.SerializedName
import student.testing.system.models.enums.OrderingType

data class CourseCreationReq(val name: String)

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

data class TestResultsRequestParams(
    @SerializedName("only_max_result") val onlyMaxResult: Boolean? = null,
    @SerializedName("search_prefix") val searchPrefix: String? = null,
    @SerializedName("upper_bound") val upperBound: Float? = null,
    @SerializedName("lower_bound") val lowerBound: Float? = null,
    @SerializedName("score_equals") val scoreEquals: Float? = null,
    @SerializedName("date_from") val dateFrom: String? = null,
    @SerializedName("date_to") val dateTo: String? = null,
    val ordering: OrderingType? = null
)