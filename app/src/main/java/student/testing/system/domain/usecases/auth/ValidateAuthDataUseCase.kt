package student.testing.system.domain.usecases.auth

import androidx.core.util.PatternsCompat
import student.testing.system.R
import student.testing.system.domain.models.PrivateUser
import student.testing.system.domain.states.operationStates.AuthState
import javax.inject.Inject

class ValidateAuthDataUseCase @Inject constructor() {
    operator fun invoke(email: String, password: String): AuthState<PrivateUser> {
        val emailState = if (email.isEmpty()) {
            AuthState.EmailError(R.string.error_empty_field)
        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            AuthState.EmailError(R.string.error_invalid_email)
        } else AuthState.ValidationSuccesses

        val passwordState = if (password.isEmpty()) {
            AuthState.PasswordError(R.string.error_empty_field)
        } else {
            AuthState.ValidationSuccesses
        }

        return if (emailState is AuthState.ValidationSuccesses && passwordState is AuthState.ValidationSuccesses) {
            AuthState.ValidationSuccesses
        } else if (emailState is AuthState.EmailError && passwordState is AuthState.ValidationSuccesses) {
            emailState
        } else if (passwordState is AuthState.PasswordError && emailState is AuthState.ValidationSuccesses) {
            passwordState
        } else AuthState.InputIsInvalid(
            (emailState as AuthState.EmailError).messageResId,
            (passwordState as AuthState.PasswordError).messageResId
        )
    }
}