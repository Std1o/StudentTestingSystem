package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import student.testing.system.data.MainRepository
import student.testing.system.domain.DataState
import student.testing.system.common.AccountSession
import student.testing.system.common.Utils
import student.testing.system.models.PrivateUser
import student.testing.system.sharedPreferences.PrefsUtils
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    val repository: MainRepository,
    val prefsUtils: PrefsUtils
) : ViewModel(){

    fun signUp(email: String, username: String, password: String): StateFlow<DataState<PrivateUser>> {
        val stateFlow = MutableStateFlow<DataState<PrivateUser>>(DataState.Loading)
        viewModelScope.launch {
            repository.signUp(email, username, password).collect {
                if (it is DataState.Success) {
                    val privateUser = it.data
                    AccountSession.instance.token = privateUser.token
                    AccountSession.instance.userId = privateUser.id
                    AccountSession.instance.email = privateUser.email
                    AccountSession.instance.username = privateUser.username
                    prefsUtils.setEmail(email)
                    prefsUtils.setPassword(password)
                }
                stateFlow.emit(it)
            }
        }
        return stateFlow
    }
}