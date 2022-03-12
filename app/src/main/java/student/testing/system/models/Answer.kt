package student.testing.system.models

import com.google.gson.annotations.SerializedName

data class Answer(
    val answer: String,
    @field:SerializedName("is_right") val isRight: Boolean, val id: Int
)