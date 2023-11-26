package student.testing.system.domain.models

import com.google.gson.annotations.SerializedName

data class PrivateUser(
    val id: Int,
    val username: String,
    val email: String,
    @SerializedName("access_token") val token: String
)