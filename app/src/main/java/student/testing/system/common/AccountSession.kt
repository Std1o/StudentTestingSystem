package student.testing.system.common

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import student.testing.system.domain.models.PrivateUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountSession @Inject constructor() {

    private val _state = MutableStateFlow(SessionState())
    val state: StateFlow<SessionState> = _state.asStateFlow()

    val token: String?
        get() = _state.value.token

    val userId: Int?
        get() = _state.value.userId

    val email: String?
        get() = _state.value.email

    val username: String?
        get() = _state.value.username

    fun save(user: PrivateUser) {
        _state.value = SessionState(
            token = user.token,
            userId = user.id,
            email = user.email,
            username = user.username
        )
    }

    fun clear() {
        _state.value = SessionState()
    }
}

data class SessionState(
    val token: String? = null,
    val userId: Int? = null,
    val email: String? = null,
    val username: String? = null
)