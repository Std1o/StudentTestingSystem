package student.testing.system.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import lilith.presentation.viewmodel.StatesViewModel
import student.testing.system.common.LaunchNavigation
import student.testing.system.domain.models.PrivateUser
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.domain.states.operationStates.SignUpState
import student.testing.system.domain.usecases.auth.SignUpUseCase
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.Destination
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    @LaunchNavigation private val appNavigator: AppNavigator
) : StatesViewModel() {

    private val _signUpState = MutableStateFlow<SignUpState<PrivateUser>>(OperationState.NoState)
    val signUpState = _signUpState.asStateFlow()

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var username by mutableStateOf("")
        private set

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun onUsernameChange(newUsername: String) {
        username = newUsername
    }

    fun signUp() {
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