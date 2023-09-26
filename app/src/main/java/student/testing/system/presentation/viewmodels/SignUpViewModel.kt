package student.testing.system.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import student.testing.system.data.mapper.ToOperationStateMapper
import student.testing.system.domain.auth.SignUpUseCase
import student.testing.system.domain.states.OperationState
import student.testing.system.domain.states.RequestState
import student.testing.system.domain.states.SignUpState
import student.testing.system.models.PrivateUser
import student.testing.system.presentation.ui.screens.login.LoginContentState
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel(), ResettableViewModel {

    private val _uiState = MutableStateFlow<SignUpState<PrivateUser>>(RequestState.NoState)
    val uiState: StateFlow<SignUpState<PrivateUser>> = _uiState.asStateFlow()

    val lastOperationState: StateFlow<OperationState<PrivateUser>> =
        uiState.map { ToOperationStateMapper<SignUpState<PrivateUser>, PrivateUser>().map(uiState.value) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RequestState.NoState)

    // TODO replace to SignUpContentState which will contains name field
    var contentState by mutableStateOf(
        LoginContentState()
    )

    fun signUp(email: String, username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = RequestState.Loading
            _uiState.value = signUpUseCase(email = email, username = username, password = password)
        }
    }

    override fun resetState() {
        _uiState.value = RequestState.NoState
    }
}