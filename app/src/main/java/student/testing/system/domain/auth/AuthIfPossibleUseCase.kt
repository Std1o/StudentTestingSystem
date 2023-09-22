package student.testing.system.domain.auth

import student.testing.system.models.PrivateUser
import student.testing.system.sharedPreferences.PrefsUtils
import javax.inject.Inject

class AuthIfPossibleUseCase @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val prefsUtils: PrefsUtils
) {

    suspend operator fun invoke(): AuthState<PrivateUser> {
        if (isAuthDataSaved()) {
            return authWithSavedData()
        } else {
            return AuthState.Unauthorized
        }
        return AuthState.Loading
    }

    private fun isAuthDataSaved() = prefsUtils.getEmail().isNotEmpty()

    private suspend fun authWithSavedData(): AuthState<PrivateUser> {
        return loginUseCase(prefsUtils.getEmail(), prefsUtils.getPassword())
    }
}