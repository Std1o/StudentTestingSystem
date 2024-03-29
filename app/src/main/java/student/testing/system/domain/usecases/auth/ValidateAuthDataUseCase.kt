package student.testing.system.domain.usecases.auth

import androidx.core.util.PatternsCompat
import student.testing.system.R
import student.testing.system.domain.states.operationStates.AuthState
import student.testing.system.domain.models.PrivateUser
import javax.inject.Inject

class ValidateAuthDataUseCase @Inject constructor() {
    operator fun invoke(email: String, password: String): AuthState<PrivateUser> {
        return if (email.isEmpty()) {
            AuthState.EmailError(R.string.error_empty_field)
        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            AuthState.EmailError(R.string.error_invalid_email)
        } else if (password.isEmpty()) {
            AuthState.PasswordError(R.string.error_empty_field)
        } else {
            AuthState.ValidationSuccesses
        }
    }
}