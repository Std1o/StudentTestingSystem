package student.testing.system.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import lilith.presentation.viewmodel.StatesViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import student.testing.system.common.Constants.LAUNCH_NAVIGATION
import student.testing.system.domain.usecases.auth.SignUpUseCase
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.domain.states.operationStates.SignUpState
import student.testing.system.domain.models.PrivateUser
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import student.testing.system.presentation.ui.models.screenSession.SignUpScreenSession
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    @Named(LAUNCH_NAVIGATION) private val appNavigator: AppNavigator
) : StatesViewModel() {

    private val _signUpState = MutableStateFlow<SignUpState<PrivateUser>>(OperationState.NoState)
    val signUpState = _signUpState.asStateFlow()

    var screenSession by mutableStateOf(SignUpScreenSession())

    fun signUp(email: String, username: String, password: String) {
        viewModelScope.launch {
            val requestResult = executeOperationAndIgnoreData({
                signUpUseCase(email = email, username = username, password = password)
            }) {
                navigateToCourses()
            }
            _signUpState.value = requestResult
        }
    }

    private fun navigateToCourses() {
        appNavigator.tryNavigateTo(
            popUpToRoute = Destination.LoginScreen(),
            inclusive = true,
            route = Destination.CoursesScreen()
        )
    }

    fun onTextFieldChanged() {
        _signUpState.value = OperationState.NoState
    }
}