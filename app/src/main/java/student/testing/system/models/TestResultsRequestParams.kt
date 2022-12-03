package student.testing.system.models

import com.google.gson.annotations.SerializedName

data class TestResultsRequestParams(
    @SerializedName("only_max_result") val onlyMaxResult: Boolean? = null,
    val username: String? = null,
    val email: String? = null,
    @SerializedName("upper_bound") val upperBound: Int? = null,
    @SerializedName("lower_bound") val lowerBound: Int? = null,
    @SerializedName("score_equals") val scoreEquals: Int? = null,
    @SerializedName("date_from") val dateFrom: String? = null,
    @SerializedName("date_to") val dateTo: String? = null,
    //ordering enum
)
