package student.testing.system.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.domain.states.operationStates.AuthState
import student.testing.system.domain.states.operationStates.LoginState
import student.testing.system.domain.states.operationStates.OperationState
import student.testing.system.presentation.ui.activity.ui.theme.LoginTextColor
import student.testing.system.presentation.ui.components.*
import student.testing.system.presentation.viewmodels.LoginViewModel

@Composable
fun LoginScreen() {
    val viewModel = hiltViewModel<LoginViewModel>()
    val loginState by viewModel.loginState.collectAsState()
    val lastOperationState by viewModel.lastOperationState.collectAsState()
    val events by viewModel.events.collectAsState(OperationState.NoState)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val needToHideUI = loginState is LoginState.HiddenUI
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { contentPadding ->
        if (needToHideUI) {
            LoadingIndicator()
            return@Scaffold
        }
        with(loginState) {
            if (this is LoginState.ErrorWhenAuthorized) {
                ErrorScreen(message = this.message) {
                    viewModel.authIfPossible()
                }
                return@Scaffold
            }
        }
        CenteredColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Text(
                text = stringResource(R.string.app_name),
                color = LoginTextColor,
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 100.dp)
            )
            val isEmailError = loginState is AuthState.EmailError
            val isPasswordError = loginState is AuthState.PasswordError
            val inputIsInvalid = loginState is AuthState.InputIsInvalid
            EmailTextField(
                onTextChanged = {
                    viewModel.onEmailChange(it)
                    viewModel.onTextFieldChanged()
                },
                isEmailError = isEmailError || inputIsInvalid,
                errorText = if (isEmailError) (loginState as AuthState.EmailError).messageResId
                else if (inputIsInvalid) (loginState as AuthState.InputIsInvalid).mailResId
                else 0
            )
            PasswordTextField(
                onTextChanged = {
                    viewModel.onPasswordChange(it)
                    viewModel.onTextFieldChanged()
                },
                isPasswordError = isPasswordError || inputIsInvalid,
                errorText = if (isPasswordError) (loginState as AuthState.PasswordError).messageResId
                else if (inputIsInvalid) (loginState as AuthState.InputIsInvalid).passResId
                else 0
            )
            BigButton(text = R.string.sign_in) {
                viewModel.auth()
            }
            Text(
                text = stringResource(R.string.registration),
                color = LoginTextColor,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .clickable { viewModel.navigateToSignUp() }
            )
        }
    }
    UIReactionOnLastOperationState(
        lastOperationState,
        events,
        snackbarHostState,
        onError = { exception, code, _ ->
            if (loginState !is LoginState.ErrorWhenAuthorized) {
                scope.launch { snackbarHostState.showSnackbar(exception) }
            }
        })
}