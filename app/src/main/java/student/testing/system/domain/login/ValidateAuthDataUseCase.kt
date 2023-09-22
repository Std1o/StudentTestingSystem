package student.testing.system.domain.login

import androidx.core.util.PatternsCompat
import student.testing.system.R
import student.testing.system.models.PrivateUser
import javax.inject.Inject

class ValidateAuthDataUseCase @Inject constructor() {
    operator fun invoke(email: String, password: String): LoginState<PrivateUser> {
        return if (email.isEmpty()) {
            LoginState.EmailError(R.string.error_empty_field)
        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            LoginState.EmailError(R.string.error_invalid_email)
        } else if (password.isEmpty()) {
            LoginState.PasswordError(R.string.error_empty_field)
        } else {
            LoginState.ValidationSuccesses
        }
    }
}