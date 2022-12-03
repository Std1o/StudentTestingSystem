package student.testing.system.models

import com.google.gson.annotations.SerializedName

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
    @SerializedName("upper_bound") val upperBound: Int? = null,
    @SerializedName("lower_bound") val lowerBound: Int? = null,
    @SerializedName("score_equals") val scoreEquals: Int? = null,
    @SerializedName("date_from") val dateFrom: String? = null,
    @SerializedName("date_to") val dateTo: String? = null,
    //ordering enum
)