package student.testing.system.experimantal

import androidx.annotation.StringRes
import stdio.godofappstates.annotations.OperationState

@OperationState
sealed interface AuthState<out R> {
    data class EmailError(@StringRes val messageResId: Int) : AuthState<Nothing>
    data class PasswordError(@StringRes val messageResId: Int) : AuthState<Nothing>

    data object ValidationSuccesses : AuthState<Nothing>
}