package student.testing.system.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import student.testing.system.common.asAny
import student.testing.system.domain.auth.AuthIfPossibleUseCase
import student.testing.system.domain.auth.LoginUseCase
import student.testing.system.domain.states.LoginState
import student.testing.system.models.PrivateUser
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import student.testing.system.presentation.ui.models.LoginContentState
import student.testing.system.presentation.ui.stateWrapper.UIStateWrapper
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val authIfPossibleUseCase: AuthIfPossibleUseCase,
    private val appNavigator: AppNavigator
) : OperationViewModel<PrivateUser>() {

    // TODO rename to _LoginStateWrapper
    private val _uiStateWrapper =
        MutableStateFlow<UIStateWrapper<LoginState<PrivateUser>>>(
            UIStateWrapper(LoginState.AuthStateChecking)
        )
    val uiStateWrapper: StateFlow<UIStateWrapper<LoginState<PrivateUser>>> =
        _uiStateWrapper.asStateFlow()

    val contentState by mutableStateOf(LoginContentState())

    init {
        viewModelScope.launch {
            val requestResult =
                executeOperation(
                    call = { authIfPossibleUseCase() },
                    type = PrivateUser::class.asAny()
                ) {
                    navigateToCourses()
                }
            _uiStateWrapper.value = UIStateWrapper(requestResult)
        }
    }

    fun auth(email: String, password: String) {
        viewModelScope.launch {
            val requestResult = executeOperation({ loginUseCase(email, password) }) {
                navigateToCourses()
            }
            _uiStateWrapper.value = UIStateWrapper(requestResult)
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