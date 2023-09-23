package student.testing.system.domain.auth

sealed interface LoginState<out R> : AuthState<R> {
    object Unauthorized : AuthState<Nothing>, LoginState<Nothing>
}