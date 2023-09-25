package student.testing.system.domain.states

sealed interface LoginState<out R> {
    object AuthStateChecking : LoginState<Nothing>
}