package student.testing.system.domain.states

sealed interface LoginState<out R> : AuthState<R> {
    object AuthStateChecking : AuthState<Nothing>, LoginState<Nothing>
}