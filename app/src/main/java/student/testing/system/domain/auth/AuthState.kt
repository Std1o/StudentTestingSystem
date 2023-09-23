package student.testing.system.domain.auth

import androidx.annotation.StringRes

sealed interface AuthState<out R> {
    object Initial : AuthState<Nothing>
    data class Success<out T>(val data: T) : AuthState<T>
    data class Error(val exception: String, val code: Int = -1) : AuthState<Nothing>
    data class EmailError(@StringRes val messageResId: Int) : AuthState<Nothing>
    data class PasswordError(@StringRes val messageResId: Int) : AuthState<Nothing>
    object ValidationSuccesses : AuthState<Nothing>
    object Loading : AuthState<Nothing>
}