package student.testing.system.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import student.testing.system.domain.auth.SignUpUseCase
import student.testing.system.domain.states.SignUpState
import student.testing.system.models.PrivateUser
import student.testing.system.presentation.ui.screens.login.LoginContentState
import student.testing.system.presentation.ui.stateWrappers.UIStateWrapper
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : OperationViewModel<SignUpState<PrivateUser>, PrivateUser>() {

    private val _uiStateWrapper =
        MutableStateFlow<UIStateWrapper<SignUpState<PrivateUser>, PrivateUser>>(UIStateWrapper())
    val uiStateWrapper: StateFlow<UIStateWrapper<SignUpState<PrivateUser>, PrivateUser>> =
        _uiStateWrapper.asStateFlow()

    // TODO replace to SignUpContentState which will contains name field
    var contentState by mutableStateOf(
        LoginContentState()
    )

    fun signUp(email: String, username: String, password: String) {
        viewModelScope.launch {
            val requestResult = executeOperation({
                signUpUseCase(email = email, username = username, password = password)
            })
            _uiStateWrapper.value = UIStateWrapper(requestResult)
        }
    }
}