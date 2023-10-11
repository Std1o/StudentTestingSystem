@file:OptIn(ExperimentalMaterial3Api::class)

package student.testing.system.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import student.testing.system.domain.states.AuthState
import student.testing.system.domain.states.LoginState
import student.testing.system.presentation.ui.activity.ui.theme.LoginTextColor
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
    val loginStateWrapper by viewModel.loginStateWrapper.collectAsState()
    val lastOperationStateWrapper by viewModel.lastOperationStateWrapper.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val contentState = viewModel.contentState
    var needToHideUI = loginStateWrapper.uiState is LoginState.HiddenUI
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { contentPadding ->
        if (needToHideUI) {
            LoadingIndicator()
            return@Scaffold
        }
        with(loginStateWrapper.uiState) {
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
            val isEmailError = loginStateWrapper.uiState is AuthState.EmailError
            val isPasswordError = loginStateWrapper.uiState is AuthState.PasswordError
            val email = emailTextField(
                onReceiveListener = loginStateWrapper,
                contentState = contentState.emailContentState,
                isEmailError = isEmailError,
                errorText = if (isEmailError) (loginStateWrapper.uiState as AuthState.EmailError).messageResId else 0
            )
            val password = passwordTextField(
                onReceiveListener = loginStateWrapper,
                contentState.passwordContentState,
                isPasswordError = isPasswordError,
                errorText = if (isPasswordError) (loginStateWrapper.uiState as AuthState.PasswordError).messageResId else 0
            )
            Button(
                onClick = {
                    viewModel.auth(email = email, password = password)
                }, modifier = Modifier
                    .padding(top = 30.dp)
                    .height(45.dp)
                    .width(250.dp)
            ) { Text(stringResource(R.string.sign_in)) }
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
        lastOperationStateWrapper,
        snackbarHostState,
        onError = { exception, code, _ ->
            if (loginStateWrapper.uiState !is LoginState.ErrorWhenAuthorized) {
                scope.launch { snackbarHostState.showSnackbar(exception) }
            }
        })
}