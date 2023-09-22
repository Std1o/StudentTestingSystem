package student.testing.system.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import student.testing.system.common.AccountSession
import student.testing.system.domain.DataState
import student.testing.system.domain.MainRepository
import student.testing.system.models.PrivateUser
import student.testing.system.models.SignUpReq
import student.testing.system.sharedPreferences.PrefsUtils
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: MainRepository,
    private val prefsUtils: PrefsUtils
) : BaseViewModel<PrivateUser>() {

    fun signUp(email: String, username: String, password: String) {
        viewModelScope.launch {
            launchRequest(repository.signUp(SignUpReq(email, username, password))) {
                if (it is DataState.Success) {
                    val privateUser = it.data
                    AccountSession.instance.token = privateUser.token
                    AccountSession.instance.userId = privateUser.id
                    AccountSession.instance.email = privateUser.email
                    AccountSession.instance.username = privateUser.username
                    prefsUtils.setEmail(email)
                    prefsUtils.setPassword(password)
                }
            }
        }
    }
}