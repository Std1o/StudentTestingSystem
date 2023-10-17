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
import student.testing.system.common.Constants.LAUNCH_NAVIGATION
import student.testing.system.domain.auth.SignUpUseCase
import student.testing.system.domain.states.LoginState
import student.testing.system.domain.states.SignUpState
import student.testing.system.models.PrivateUser
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import student.testing.system.presentation.ui.models.SignUpContentState
import student.testing.system.presentation.ui.stateWrapper.StateWrapper
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    @Named(LAUNCH_NAVIGATION) private val appNavigator: AppNavigator
) : StatesViewModel() {

    private val _signUpStateWrapper = StateWrapper.mutableStateFlow<SignUpState<PrivateUser>>()
    val signUpStateWrapper = _signUpStateWrapper.asStateFlow()

    var contentState by mutableStateOf(SignUpContentState())

    fun signUp(email: String, username: String, password: String) {
        viewModelScope.launch {
            val requestResult = executeOperationAndIgnoreData({
                signUpUseCase(email = email, username = username, password = password)
            }) {
                navigateToCourses()
            }
            _signUpStateWrapper.value = StateWrapper(requestResult)
        }
    }

    private fun navigateToCourses() {
        appNavigator.tryNavigateTo(
            popUpToRoute = Destination.LoginScreen(),
            inclusive = true,
            route = Destination.CoursesScreen()
        )
    }
}