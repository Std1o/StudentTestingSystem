package student.testing.system.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import student.testing.system.domain.auth.AuthIfPossibleUseCase
import student.testing.system.domain.auth.LoginUseCase
import student.testing.system.domain.states.LoginState
import student.testing.system.models.PrivateUser
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import student.testing.system.presentation.ui.models.LoginContentState
import student.testing.system.presentation.ui.stateWrapper.StateWrapper
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val authIfPossibleUseCase: AuthIfPossibleUseCase,
    private val appNavigator: AppNavigator
) : StatesViewModel() {

    private val _loginStateWrapper =
        StateWrapper.mutableStateFlow<LoginState<PrivateUser>>(LoginState.AuthStateChecking)
    val loginStateWrapper = _loginStateWrapper.asStateFlow()

    val contentState by mutableStateOf(LoginContentState())

    init {
        viewModelScope.launch {
            val requestResult = executeOperationAndIgnoreData({ authIfPossibleUseCase() }) {
                navigateToCourses()
            }
            _loginStateWrapper.value = StateWrapper(requestResult)
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