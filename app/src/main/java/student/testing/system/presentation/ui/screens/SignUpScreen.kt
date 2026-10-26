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
import student.testing.system.domain.states.operationStates.AuthState
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.domain.states.operationStates.SignUpState
import student.testing.system.presentation.ui.components.*
import student.testing.system.presentation.viewmodels.SignUpViewModel

@Composable
fun SignUpScreen() {
    val viewModel = hiltViewModel<SignUpViewModel>()
    val signUpState by viewModel.signUpState.collectAsState()
    val lastOperationState by viewModel.lastOperationState.collectAsState()
    val events by viewModel.events.collectAsState(OperationState.NoState)
    val snackbarHostState = remember { SnackbarHostState() }
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
            val isEmailError = signUpState is AuthState.EmailError
            val isPasswordError = signUpState is AuthState.PasswordError
            val isUsernameError = signUpState is SignUpState.NameError
            RequiredTextField(
                onTextChanged = {
                    viewModel.onUsernameChange(it)
                    viewModel.onTextFieldChanged()
                },
                isError = isUsernameError,
                errorText = if (isUsernameError) (signUpState as SignUpState.NameError).messageResId else 0,
                hint = R.string.name
            )
            EmailTextField(
                onTextChanged = {
                    viewModel.onEmailChange(it)
                    viewModel.onTextFieldChanged()
                },
                isEmailError = isEmailError,
                errorText = if (isEmailError) (signUpState as AuthState.EmailError).messageResId else 0
            )
            PasswordTextField(
                onTextChanged = {
                    viewModel.onPasswordChange(it)
                    viewModel.onTextFieldChanged()
                },
                isPasswordError = isPasswordError,
                errorText = if (isPasswordError) (signUpState as AuthState.PasswordError).messageResId else 0
            )
            BigButton(text = R.string.sign_up) {
                viewModel.signUp()
            }
        }
    }
    UIReactionOnLastOperationState(lastOperationState, events, snackbarHostState)
}