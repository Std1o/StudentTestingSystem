package student.testing.system.domain.auth

import student.testing.system.R
import student.testing.system.annotations.NotScreenState
import student.testing.system.common.AccountSession
import student.testing.system.domain.MainRepository
import student.testing.system.domain.states.AuthState
import student.testing.system.domain.states.RequestState
import student.testing.system.domain.states.SignUpState
import student.testing.system.models.PrivateUser
import student.testing.system.models.SignUpReq
import student.testing.system.sharedPreferences.PrefsUtils
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: MainRepository,
    private val prefsUtils: PrefsUtils,
    private val validateAuthDataUseCase: ValidateAuthDataUseCase
) {

    suspend operator fun invoke(
        email: String,
        username: String,
        password: String
    ): SignUpState<PrivateUser> {
        if (username.isEmpty()) return SignUpState.NameError(R.string.error_empty_field)
        val validationResult = validateAuthDataUseCase(email = email, password = password)
        return if (validationResult is AuthState.ValidationSuccesses) {
            signUp(email = email, username = username, password = password)
        } else {
            validationResult
        }
    }

    @OptIn(NotScreenState::class)
    private suspend fun signUp(
        email: String,
        username: String,
        password: String
    ): AuthState<PrivateUser> {
        val requestResult = repository.signUp(SignUpReq(email, username, password))
        if (requestResult is RequestState.Success) {
            saveAuthData(email, password)
            createSession(requestResult.data)
        }
        return requestResult
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