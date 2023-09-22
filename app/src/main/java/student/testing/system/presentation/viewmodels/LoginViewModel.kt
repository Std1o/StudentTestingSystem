package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import student.testing.system.domain.login.AuthIfPossibleUseCase
import student.testing.system.domain.login.AuthUseCase
import student.testing.system.domain.login.LoginState
import student.testing.system.models.PrivateUser
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val authIfPossibleUseCase: AuthIfPossibleUseCase,
    private val appNavigator: AppNavigator
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginState<PrivateUser>>(LoginState.Loading)
    val uiState: StateFlow<LoginState<PrivateUser>> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = authIfPossibleUseCase()
        }
    }

    fun auth(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginState.Loading
            _uiState.value = authUseCase(email, password)
        }
    }

    fun onNavigateToSignUp() {
        appNavigator.tryNavigateTo(Destination.SignUpScreen())
    }

    fun resetState() {
        _uiState.value = LoginState.Initial
    }
}