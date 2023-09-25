package student.testing.system.domain.states

import student.testing.system.annotations.FunctionalityState

@FunctionalityState
sealed interface LoginState<out R> {
    object AuthStateChecking : LoginState<Nothing>
}