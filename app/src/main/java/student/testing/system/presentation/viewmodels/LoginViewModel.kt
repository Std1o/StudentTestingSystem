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
class LoginViewModel @Inject constructor(private val repository: MainRepository, val prefsUtils: PrefsUtils) : ViewModel() {

    val authStateFlow = MutableStateFlow<DataState<PrivateUser>>(DataState.Initial)

    init {
        if (isAuthDataSaved()) {
            authWithSavedData()
        }
    }

    fun isAuthDataSaved() = prefsUtils.getEmail().isNotEmpty()

    private fun authWithSavedData() {
        auth(prefsUtils.getEmail(), prefsUtils.getPassword())
    }

    fun auth(email: String, password: String){
        viewModelScope.launch {
            authStateFlow.emit(DataState.Loading)
            repository.auth(email, password).catch {
                authStateFlow.emit(DataState.Error(it.message ?: " "))
            }.collect {
                if (it.isSuccessful) {
                    val privateUser = it.body()!!
                    AccountSession.instance.token = privateUser.token
                    AccountSession.instance.userId = privateUser.id
                    AccountSession.instance.email = privateUser.email
                    AccountSession.instance.username = privateUser.username
                    prefsUtils.setEmail(email)
                    prefsUtils.setPassword(password)
                    authStateFlow.emit(DataState.Success(privateUser))
                } else {
                    val errorMessage = Utils.encodeErrorCode(it.errorBody())
                    authStateFlow.emit(DataState.Error(errorMessage))
                }
            }
        }
    }
}