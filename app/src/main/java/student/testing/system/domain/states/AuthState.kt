package student.testing.system.domain.states

import androidx.annotation.StringRes

sealed interface AuthState<out R> {
    data class EmailError(@StringRes val messageResId: Int) : AuthState<Nothing>
    data class PasswordError(@StringRes val messageResId: Int) : AuthState<Nothing>

    object ValidationSuccesses : AuthState<Nothing>
}