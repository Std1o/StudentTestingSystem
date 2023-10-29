package student.testing.system.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import student.testing.system.presentation.ui.activity.ui.theme.LoginTextColor
import student.testing.system.presentation.ui.components.BigButton
import student.testing.system.presentation.ui.components.CenteredColumn
import student.testing.system.presentation.ui.components.emailTextField
import student.testing.system.presentation.ui.components.ErrorScreen
import student.testing.system.presentation.ui.components.LastOperationStateUIHandler
import student.testing.system.presentation.ui.components.LoadingIndicator
import student.testing.system.presentation.ui.components.passwordTextField
import student.testing.system.presentation.viewmodels.LoginViewModel

@Composable
fun LoginScreen() {
    val viewModel = hiltViewModel<LoginViewModel>()
    val loginState by viewModel.loginState.collectAsState()
    val lastOperationState by viewModel.lastOperationState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val screenSession = viewModel.screenSession
    var needToHideUI = loginState is LoginState.HiddenUI
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
            val email = emailTextField(
                onTextChanged = { viewModel.onTextFieldChanged() },
                emailState = screenSession.emailState,
                isEmailError = isEmailError,
                errorText = if (isEmailError) (loginState as AuthState.EmailError).messageResId else 0
            )
            val password = passwordTextField(
                onTextChanged = { viewModel.onTextFieldChanged() },
                screenSession.passwordState,
                isPasswordError = isPasswordError,
                errorText = if (isPasswordError) (loginState as AuthState.PasswordError).messageResId else 0
            )
            BigButton(text = R.string.sign_in) {
                viewModel.auth(email = email, password = password)
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
    LastOperationStateUIHandler(
        lastOperationState,
        { viewModel.onErrorReceived() },
        snackbarHostState,
        onError = { exception, code, _ ->
            if (loginState !is LoginState.ErrorWhenAuthorized) {
                scope.launch { snackbarHostState.showSnackbar(exception) }
            }
        })
}