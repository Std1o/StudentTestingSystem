package student.testing.system.domain.states

import student.testing.system.annotations.FunctionalityState

@FunctionalityState
sealed interface LoginState<out R> {
    data object HiddenUI : LoginState<Nothing>

    data class ErrorWhenAuthorized(val message: String) : LoginState<Nothing>
}