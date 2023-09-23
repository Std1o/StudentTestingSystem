package student.testing.system.domain.auth

sealed interface LoginState<out R> : AuthState<R> {
    object AuthStateChecking : AuthState<Nothing>, LoginState<Nothing>
}