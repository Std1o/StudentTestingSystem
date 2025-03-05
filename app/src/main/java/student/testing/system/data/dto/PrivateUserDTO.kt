package student.testing.system.data.dto

import com.google.gson.annotations.SerializedName

data class PrivateUserDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("access_token") val token: String
)