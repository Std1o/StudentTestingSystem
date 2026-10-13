package student.testing.system.domain.usecases.auth

import student.testing.system.R
import student.testing.system.common.AccountSession
import student.testing.system.domain.repository.AuthRepository
import student.testing.system.domain.states.operationStates.AuthState
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.domain.states.operationStates.SignUpState
import student.testing.system.domain.models.PrivateUser
import student.testing.system.domain.models.SignUpReq
import student.testing.system.sharedPreferences.PrefsUtils
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val prefsUtils: PrefsUtils,
    private val accountSession: AccountSession,
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

    private suspend fun signUp(
        email: String,
        username: String,
        password: String
    ): AuthState<PrivateUser> {
        val requestResult = repository.signUp(SignUpReq(email, username, password))
        if (requestResult is OperationState.Success) {
            saveAuthData(email, password)
            accountSession.save(requestResult.data)
        }
        return requestResult
    }

    private fun saveAuthData(email: String, password: String) {
        prefsUtils.setEmail(email)
        prefsUtils.setPassword(password)
    }
}