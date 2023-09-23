package student.testing.system.domain.auth

import student.testing.system.common.AccountSession
import student.testing.system.data.mapper.DataStateToLoadingStateMapper
import student.testing.system.domain.MainRepository
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
    ): AuthState<PrivateUser> {
        val validationResult = validateAuthDataUseCase(email = email, password = password)
        return if (validationResult is AuthState.ValidationSuccesses) {
            signUp(email = email, username = username, password = password)
        } else {
            validationResult
        }
    }

    private suspend fun signUp(
        email: String,
        username: String,
        password: String
    ): AuthState<PrivateUser> {
        val requestResult = repository.signUp(SignUpReq(email, username, password))
        return DataStateToLoadingStateMapper<PrivateUser> {
            saveAuthData(email, password)
            createSession(it)
        }.map(requestResult)
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