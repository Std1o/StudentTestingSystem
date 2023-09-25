package student.testing.system.domain.states

import androidx.annotation.StringRes

sealed interface SignUpState<out R> {
    data class NameError(@StringRes val messageResId: Int) : SignUpState<Nothing>
}