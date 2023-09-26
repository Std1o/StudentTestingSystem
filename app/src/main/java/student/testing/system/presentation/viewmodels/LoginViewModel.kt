package student.testing.system.presentation.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import student.testing.system.data.mapper.ToOperationStateMapper
import student.testing.system.domain.auth.AuthIfPossibleUseCase
import student.testing.system.domain.auth.LoginUseCase
import student.testing.system.domain.states.RequestState
import student.testing.system.domain.states.LoginState
import student.testing.system.domain.states.OperationState
import student.testing.system.models.PrivateUser
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import student.testing.system.presentation.ui.screens.login.LoginContentState
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val authIfPossibleUseCase: AuthIfPossibleUseCase,
    private val appNavigator: AppNavigator
) : ViewModel(), ResettableViewModel {

    private val _uiState = MutableStateFlow<LoginState<PrivateUser>>(LoginState.AuthStateChecking)
    val uiState: StateFlow<LoginState<PrivateUser>> = _uiState.asStateFlow()

    val lastOperationState: StateFlow<OperationState<PrivateUser>> =
        uiState.map { ToOperationStateMapper<LoginState<PrivateUser>, PrivateUser>().map(uiState.value) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RequestState.NoState)

    var contentState by mutableStateOf(
        LoginContentState()
    )

    init {
        viewModelScope.launch {
            _uiState.value = authIfPossibleUseCase()
        }
    }

    fun auth(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = RequestState.Loading
            _uiState.value = loginUseCase(email, password)
        }
    }

    fun onNavigateToSignUp() {
        appNavigator.tryNavigateTo(Destination.SignUpScreen())
    }

    override fun resetState() {
        _uiState.value = RequestState.NoState
    }
}