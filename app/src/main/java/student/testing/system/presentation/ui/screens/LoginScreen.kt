@file:OptIn(ExperimentalMaterial3Api::class)

package student.testing.system.presentation.ui.screens

import android.app.Activity
import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import student.testing.system.R
import student.testing.system.annotations.NotScreenState
import student.testing.system.data.mapper.ToDataStateMapper
import student.testing.system.domain.states.AuthState
import student.testing.system.domain.states.DataState
import student.testing.system.domain.states.LoginState
import student.testing.system.models.PrivateUser
import student.testing.system.presentation.ui.activity.MainActivity
import student.testing.system.presentation.ui.activity.MainActivityNew
import student.testing.system.presentation.ui.activity.ui.theme.LoginTextColor
import student.testing.system.presentation.ui.components.CenteredColumn
import student.testing.system.presentation.ui.components.EmailTextField
import student.testing.system.presentation.ui.components.LoadingIndicator
import student.testing.system.presentation.ui.components.PasswordTextField
import student.testing.system.presentation.ui.components.SimpleUIStateHandler
import student.testing.system.presentation.viewmodels.LoginViewModel

@Composable
fun LoginScreen() {
    val viewModel = hiltViewModel<LoginViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val contentState = viewModel.contentState
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { contentPadding ->
        if (needToHideUI(uiState)) {
            LoadingIndicator()
            return@Scaffold
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
            val isEmailError = uiState is AuthState.EmailError
            val isPasswordError = uiState is AuthState.PasswordError
            val email = EmailTextField(
                viewModel = viewModel,
                contentState = contentState.emailContentState,
                isEmailError = isEmailError,
                errorText = if (isEmailError) (uiState as AuthState.EmailError).messageResId else 0
            )
            val password = PasswordTextField(
                viewModel = viewModel,
                contentState.passwordContentState,
                isPasswordError = isPasswordError,
                errorText = if (isPasswordError) (uiState as AuthState.PasswordError).messageResId else 0
            )
            Button(
                onClick = {
                    scope.launch { viewModel.auth(email = email, password = password) }
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
                    .clickable { viewModel.onNavigateToSignUp() }
            )
        }
    }
    val dataState = ToDataStateMapper<AuthState<PrivateUser>, PrivateUser>().map(uiState)
    SimpleUIStateHandler(dataState, snackbarHostState, viewModel) {
        val activity = (LocalContext.current as? Activity)
        activity?.finish()
        activity?.startActivity(Intent(activity, MainActivity::class.java))
    }
}

@OptIn(NotScreenState::class)
fun <T> needToHideUI(uiState: AuthState<T>) =
    uiState is LoginState.AuthStateChecking || uiState is DataState.Success