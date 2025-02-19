package student.testing.system.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import lilith.presentation.viewmodel.StatesViewModel
import student.testing.system.common.LaunchNavigation
import student.testing.system.domain.models.PrivateUser
import student.testing.system.domain.states.operationStates.LoginState
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.domain.usecases.auth.AuthIfPossibleUseCase
import student.testing.system.domain.usecases.auth.LoginUseCase
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import student.testing.system.presentation.ui.models.screenSession.LoginScreenSession
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val authIfPossibleUseCase: AuthIfPossibleUseCase,
    @LaunchNavigation private val appNavigator: AppNavigator
) : StatesViewModel() {

    private val _loginState = MutableStateFlow<LoginState<PrivateUser>>(LoginState.HiddenUI)
    val loginState = _loginState.asStateFlow()

    val screenSession by mutableStateOf(LoginScreenSession())

    init {
        authIfPossible()
    }

    fun authIfPossible() {
        _loginState.value = LoginState.HiddenUI
        viewModelScope.launch {
            val requestResult = executeOperationAndIgnoreData({ authIfPossibleUseCase() }) {
                _loginState.value = LoginState.HiddenUI
                navigateToCourses()
            }
            if (requestResult !is OperationState.Success) {
                _loginState.value = requestResult
            }
            if (requestResult is OperationState.Error && requestResult.code != 401) {
                _loginState.value = LoginState.ErrorWhenAuthorized(requestResult.exception)
            }
        }
    }

    fun auth(email: String, password: String) {
        viewModelScope.launch {
            val requestResult = executeOperationAndIgnoreData({ loginUseCase(email, password) }) {
                navigateToCourses()
            }
            _loginState.value = requestResult
        }
    }

    fun navigateToSignUp() {
        appNavigator.tryNavigateTo(Destination.SignUpScreen())
    }

    private fun navigateToCourses() {
        appNavigator.tryNavigateTo(
            popUpToRoute = Destination.LoginScreen(),
            inclusive = true,
            route = Destination.CoursesScreen()
        )
    }

    fun onTextFieldChanged() {
        _loginState.value = OperationState.NoState
    }
}