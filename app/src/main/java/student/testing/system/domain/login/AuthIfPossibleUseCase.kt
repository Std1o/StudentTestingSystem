package student.testing.system.domain.login

import student.testing.system.models.PrivateUser
import student.testing.system.sharedPreferences.PrefsUtils
import javax.inject.Inject

class AuthIfPossibleUseCase @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val prefsUtils: PrefsUtils
) {

    suspend operator fun invoke(): LoginState<PrivateUser> {
        if (isAuthDataSaved()) {
            return authWithSavedData()
        } else {
            return LoginState.Unauthorized
        }
        return LoginState.Loading
    }

    private fun isAuthDataSaved() = prefsUtils.getEmail().isNotEmpty()

    private suspend fun authWithSavedData(): LoginState<PrivateUser> {
        return authUseCase(prefsUtils.getEmail(), prefsUtils.getPassword())
    }
}