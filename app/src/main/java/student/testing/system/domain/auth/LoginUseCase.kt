package student.testing.system.domain.auth

import student.testing.system.common.AccountSession
import student.testing.system.data.mapper.DataStateToLoadingStateMapper
import student.testing.system.domain.MainRepository
import student.testing.system.models.PrivateUser
import student.testing.system.sharedPreferences.PrefsUtils
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: MainRepository,
    private val prefsUtils: PrefsUtils,
    private val validateAuthDataUseCase: ValidateAuthDataUseCase
) {

    suspend operator fun invoke(email: String, password: String): AuthState<PrivateUser> {
        val validationResult = validateAuthDataUseCase(email = email, password = password)
        return if (validationResult is AuthState.ValidationSuccesses) {
            auth(email, password)
        } else {
            validationResult
        }
    }

    private suspend fun auth(email: String, password: String): AuthState<PrivateUser> {
        val authRequest =
            "grant_type=&username=$email&password=$password&scope=&client_id=&client_secret="
        val requestResult = repository.auth(authRequest)
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