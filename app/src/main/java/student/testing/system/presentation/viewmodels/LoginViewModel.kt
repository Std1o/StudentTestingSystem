package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import student.testing.system.domain.auth.AuthIfPossibleUseCase
import student.testing.system.domain.auth.LoginUseCase
import student.testing.system.domain.auth.AuthState
import student.testing.system.domain.auth.LoginState
import student.testing.system.models.PrivateUser
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val authIfPossibleUseCase: AuthIfPossibleUseCase,
    private val appNavigator: AppNavigator
) : ViewModel(), ResettableViewModel {

    private val _uiState = MutableStateFlow<AuthState<PrivateUser>>(LoginState.AuthStateChecking)
    val uiState: StateFlow<AuthState<PrivateUser>> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = authIfPossibleUseCase()
        }
    }

    fun auth(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthState.Loading
            _uiState.value = loginUseCase(email, password)
        }
    }

    fun onNavigateToSignUp() {
        appNavigator.tryNavigateTo(Destination.SignUpScreen())
    }

    override fun resetState() {
        _uiState.value = AuthState.NoState
    }
}