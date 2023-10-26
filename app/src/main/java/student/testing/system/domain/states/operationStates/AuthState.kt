package student.testing.system.domain.states.operationStates

import androidx.annotation.StringRes
import stdio.godofappstates.annotations.OperationState

@OperationState
sealed interface AuthState<out R> : SignUpState<R>, LoginState<R> {
    data class EmailError(@StringRes val messageResId: Int) : AuthState<Nothing>
    data class PasswordError(@StringRes val messageResId: Int) : AuthState<Nothing>

    object ValidationSuccesses : AuthState<Nothing>
}