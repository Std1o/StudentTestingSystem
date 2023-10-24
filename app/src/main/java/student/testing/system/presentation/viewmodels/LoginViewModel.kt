package student.testing.system.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import godofappstates.presentation.stateWrapper.StateWrapper
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import student.testing.system.common.Constants.LAUNCH_NAVIGATION
import student.testing.system.domain.auth.AuthIfPossibleUseCase
import student.testing.system.domain.auth.LoginUseCase
import student.testing.system.domain.states.LoginState
import student.testing.system.domain.states.OperationState
import student.testing.system.models.PrivateUser
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import student.testing.system.presentation.ui.models.screenSession.LoginScreenSession
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val authIfPossibleUseCase: AuthIfPossibleUseCase,
    @Named(LAUNCH_NAVIGATION) private val appNavigator: AppNavigator
) : StatesViewModel() {

    private val _loginStateWrapper =
        StateWrapper.mutableStateFlow<LoginState<PrivateUser>>(LoginState.HiddenUI)
    val loginStateWrapper = _loginStateWrapper.asStateFlow()

    val screenSession by mutableStateOf(LoginScreenSession())

    init {
        authIfPossible()
    }

    fun authIfPossible() {
        _loginStateWrapper.value = StateWrapper(LoginState.HiddenUI)
        viewModelScope.launch {
            val requestResult = executeOperationAndIgnoreData({ authIfPossibleUseCase() }) {
                _loginStateWrapper.value = StateWrapper(LoginState.HiddenUI)
                navigateToCourses()
            }
            if (requestResult !is OperationState.Success) {
                _loginStateWrapper.value = StateWrapper(requestResult)
            }
            if (requestResult is OperationState.Error && requestResult.code != 401) {
                _loginStateWrapper.value = StateWrapper(LoginState.ErrorWhenAuthorized(requestResult.exception))
            }
        }
    }

    fun auth(email: String, password: String) {
        viewModelScope.launch {
            val requestResult = executeOperationAndIgnoreData({ loginUseCase(email, password) }) {
                navigateToCourses()
            }
            _loginStateWrapper.value = StateWrapper(requestResult)
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
}