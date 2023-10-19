package student.testing.system.presentation.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import student.testing.system.R
import student.testing.system.domain.states.AuthState
import student.testing.system.domain.states.SignUpState
import student.testing.system.presentation.ui.components.BigButton
import student.testing.system.presentation.ui.components.CenteredColumn
import student.testing.system.presentation.ui.components.emailTextField
import student.testing.system.presentation.ui.components.LastOperationStateUIHandler
import student.testing.system.presentation.ui.components.passwordTextField
import student.testing.system.presentation.ui.components.requiredTextField
import student.testing.system.presentation.viewmodels.SignUpViewModel

@Composable
fun SignUpScreen() {
    val viewModel = hiltViewModel<SignUpViewModel>()
    val signUpStateWrapper by viewModel.signUpStateWrapper.collectAsState()
    val lastOperationState by viewModel.lastOperationStateWrapper.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val screenSession = viewModel.screenSession
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { contentPadding ->
        CenteredColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            val isEmailError = signUpStateWrapper.uiState is AuthState.EmailError
            val isPasswordError = signUpStateWrapper.uiState is AuthState.PasswordError
            val isUsernameError = signUpStateWrapper.uiState is SignUpState.NameError
            val username = requiredTextField(
                onReceiveListener = signUpStateWrapper,
                fieldState = screenSession.nameState,
                isError = isUsernameError,
                errorText = if (isUsernameError) (signUpStateWrapper.uiState as SignUpState.NameError).messageResId else 0,
                hint = R.string.name
            )
            val email = emailTextField(
                onReceiveListener = signUpStateWrapper,
                emailState = screenSession.emailState,
                isEmailError = isEmailError,
                errorText = if (isEmailError) (signUpStateWrapper.uiState as AuthState.EmailError).messageResId else 0
            )
            val password = passwordTextField(
                onReceiveListener = signUpStateWrapper,
                passwordState = screenSession.passwordState,
                isPasswordError = isPasswordError,
                errorText = if (isPasswordError) (signUpStateWrapper.uiState as AuthState.PasswordError).messageResId else 0
            )
            BigButton(text = R.string.sign_up) {
                viewModel.signUp(email = email, username = username, password = password)
            }
        }
    }
    LastOperationStateUIHandler(lastOperationState, snackbarHostState)
}