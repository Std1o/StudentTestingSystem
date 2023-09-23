package student.testing.system.domain.auth

import androidx.annotation.StringRes

sealed interface SignUpState<out R> : AuthState<R> {
    data class NameError(@StringRes val messageResId: Int) : AuthState<Nothing>,
        SignUpState<Nothing>
}