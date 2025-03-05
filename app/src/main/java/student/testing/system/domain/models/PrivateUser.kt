package student.testing.system.domain.models

data class PrivateUser(
    val id: Int,
    val username: String,
    val email: String,
    val token: String
)