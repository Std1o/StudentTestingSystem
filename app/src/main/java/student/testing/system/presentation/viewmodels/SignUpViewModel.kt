package student.testing.system.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import student.testing.system.domain.auth.AuthState
import student.testing.system.domain.auth.SignUpUseCase
import student.testing.system.models.PrivateUser
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel(), ResettableViewModel {

    private val _uiState = MutableStateFlow<AuthState<PrivateUser>>(AuthState.NoState)
    val uiState: StateFlow<AuthState<PrivateUser>> = _uiState.asStateFlow()

    fun signUp(email: String, username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthState.Loading
            _uiState.value = signUpUseCase(email = email, username = username, password = password)
        }
    }

    override fun resetState() {
        _uiState.value = AuthState.NoState
    }
}