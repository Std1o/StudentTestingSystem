package student.testing.system.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import student.testing.system.common.AccountSession
import student.testing.system.domain.login.AuthIfPossibleUseCase
import student.testing.system.domain.login.AuthUseCase
import student.testing.system.domain.login.LoginState
import student.testing.system.models.PrivateUser
import student.testing.system.sharedPreferences.PrefsUtils
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val authIfPossibleUseCase: AuthIfPossibleUseCase
) : ViewModel() {

    val authStateFlow = MutableStateFlow<LoginState<PrivateUser>>(LoginState.Loading)

    init {
        viewModelScope.launch {
            authIfPossibleUseCase().collect {
                authStateFlow.emit(it)
            }
        }
    }

    fun auth(email: String, password: String) {
        viewModelScope.launch {
            authStateFlow.emit(LoginState.Loading)
            authUseCase(email, password).collect {
                authStateFlow.emit(it)
            }
        }
    }
}