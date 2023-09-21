package student.testing.system.domain.login

import androidx.core.util.PatternsCompat
import student.testing.system.R
import student.testing.system.common.AccountSession
import student.testing.system.domain.DataState
import student.testing.system.domain.MainRepository
import student.testing.system.models.PrivateUser
import student.testing.system.sharedPreferences.PrefsUtils
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val repository: MainRepository,
    private val prefsUtils: PrefsUtils
) {

    suspend operator fun invoke(email: String, password: String): LoginState<PrivateUser> {
        if (email.isEmpty()) {
            return LoginState.EmailError(R.string.error_empty_field)
        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            return LoginState.EmailError(R.string.error_invalid_email)
        } else if (password.isEmpty()) {
            return LoginState.PasswordError(R.string.error_empty_field)
        } else {
            return auth(email, password)
        }
    }

    private suspend fun auth(email: String, password: String): LoginState<PrivateUser> {
        val authRequest =
            "grant_type=&username=$email&password=$password&scope=&client_id=&client_secret="
        val requestResult = repository.auth(authRequest)
        if (requestResult is DataState.Initial) {
            return LoginState.Initial
        } else if (requestResult is DataState.Loading) {
            return LoginState.Loading
        } else if (requestResult is DataState.Success) {
            saveAuthData(email, password)
            createSession(requestResult.data)
            return LoginState.Success(requestResult.data)
        } else if (requestResult is DataState.Error) {
            return LoginState.Error(requestResult.exception, requestResult.code)
        }
        return LoginState.Loading
    }

    private fun createSession(privateUser: PrivateUser) {
        AccountSession.instance.token = privateUser.token
        AccountSession.instance.userId = privateUser.id
        AccountSession.instance.email = privateUser.email
        AccountSession.instance.username = privateUser.username
    }

    private fun saveAuthData(email: String, password: String) {
        prefsUtils.setEmail(email)
        prefsUtils.setPassword(password)
    }
}